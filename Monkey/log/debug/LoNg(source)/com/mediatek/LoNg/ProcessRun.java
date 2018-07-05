/*     */ package com.mediatek.LoNg;
/*     */ 
/*     */ import com.mediatek.LoNg.utils.log.Log;
/*     */ import com.mediatek.LoNg.utils.log.ParseLogFile;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessRun
/*     */   implements Runnable
/*     */ {
/*     */   private String sn;
/*  29 */   private boolean rootPrivilege = true;
/*     */   
/*     */   private final ConfigDescriptor config;
/*     */   private final String rootFolder;
/*  33 */   private ParseLogFile plf = null;
/*     */   
/*  35 */   private final String AEE_EXP_TEMP = "/aee_exp/temp/";
/*     */   
/*  37 */   private final int MINUTS = 60000;
/*  38 */   private final String NETLOG_SUFFIX_CAP = ".cap";
/*  39 */   private final String AEE_DBG_FILE = ".dbg";
/*  40 */   private final String DEBUGGERD_ZCORE_FILE = ".zip";
/*     */   
/*  42 */   private final SimpleDateFormat MTBF_LOG_FOLDER_FORMATER = new SimpleDateFormat(
/*  43 */     "yyyyMMdd_HHmmss");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProcessRun(String deviceSN, ConfigDescriptor config, String rootFolder)
/*     */   {
/*  51 */     this.sn = deviceSN;
/*  52 */     this.config = config;
/*  53 */     this.rootFolder = rootFolder;
/*     */     
/*  55 */     this.plf = new ParseLogFile();
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     for (;;) {
/*  61 */       long starttime = new Date().getTime();
/*     */       
/*  63 */       AdbOP.startMtkloggerAndDisableTaglog(this.sn);
/*     */       
/*  65 */       processOneDevice();
/*     */       
/*  67 */       long endtime = new Date().getTime();
/*  68 */       long nexttime = starttime + this.config.getSleep() * 60000;
/*  69 */       long sleeptime = nexttime - endtime;
/*  70 */       if (sleeptime > 0L) {
/*     */         try {
/*  72 */           Log.logger.info("Sleep " + sleeptime / 1000L + "s");
/*  73 */           Thread.sleep(sleeptime);
/*     */         } catch (InterruptedException e) {
/*  75 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void processOneDevice()
/*     */   {
/*  84 */     ArrayList<DeviceFileDetail> list = AdbOP.getAllLogList(this.sn, this.config);
/*     */     
/*  86 */     ArrayList<DeviceFileDetail> successList = new ArrayList();
/*     */     
/*  88 */     ArrayList<DeviceFileDetail> repeatList = new ArrayList();
/*     */     
/*  90 */     if (!AdbOP.isRootShell(this.sn))
/*     */     {
/*     */ 
/*  93 */       list = DataFolderCache.filterOutRepeatDataDBandCoredump(this.sn, list, repeatList);
/*  94 */       this.rootPrivilege = false;
/*     */     } else {
/*  96 */       this.rootPrivilege = true;
/*     */     }
/*     */     
/*  99 */     boolean restartMtkLogger = isException(list);
/* 100 */     if (restartMtkLogger) {
/* 101 */       AdbOP.restartMtklogger(this.sn, this.config.isModemLogAlwaysOn());
/* 102 */       list = AdbOP.getAllLogList(this.sn, this.config);
/* 103 */       list = DataFolderCache.removeRepeatDB(list, repeatList);
/*     */     } else {
/* 105 */       AdbOP.startMtklogger(this.sn);
/*     */     }
/*     */     
/*     */ 
/* 109 */     list = filterOutCurrentLoggingFolders(this.sn, list);
/*     */     
/*     */ 
/* 112 */     String pcDeviceRootFolder = this.rootFolder + "/" + this.sn + "/";
/* 113 */     String pcDeviceThisFolder = pcDeviceRootFolder + "mtklog_" + 
/* 114 */       this.MTBF_LOG_FOLDER_FORMATER.format(new Date()) + "/";
/* 115 */     new File(pcDeviceThisFolder).mkdirs();
/*     */     
/*     */ 
/* 118 */     String zipPath = pcDeviceThisFolder.substring(0, 
/* 119 */       pcDeviceThisFolder.length() - 1);
/*     */     
/*     */ 
/* 122 */     if (list.size() > 0) {
/* 123 */       successList = renamePullRemove(this.sn, this.rootPrivilege, pcDeviceThisFolder, list);
/*     */     }
/*     */     
/*     */ 
/* 127 */     if ((isFileTypeExist(new File(pcDeviceThisFolder), ".cap")) || 
/* 128 */       (isFileTypeExist(new File(pcDeviceThisFolder), ".cap.LoNg"))) {
/* 129 */       zipPath = zipPath + "_netlog";
/*     */     }
/* 131 */     if ((isFileTypeExist(new File(pcDeviceThisFolder), ".dbg")) || 
/* 132 */       (isFileTypeExist(new File(pcDeviceThisFolder), ".zip"))) {
/* 133 */       zipPath = zipPath + "_exception.zip";
/*     */     } else {
/* 135 */       zipPath = zipPath + ".zip";
/*     */     }
/*     */     
/* 138 */     mtbfInfoTag(this.sn, successList, pcDeviceThisFolder, zipPath);
/*     */     
/* 140 */     zipAndDeleteLogFolder(zipPath, pcDeviceThisFolder);
/*     */   }
/*     */   
/*     */ 
/*     */   private void mtbfInfoTag(String sn, ArrayList<DeviceFileDetail> list, String pcFolder, String zipPath)
/*     */   {
/* 146 */     String logFileOnPC = null;
/*     */     
/* 148 */     if (new File(pcFolder).listFiles().length == 0) {
/* 149 */       return;
/*     */     }
/*     */     
/* 152 */     for (DeviceFileDetail devFile : list) {
/* 153 */       logFileOnPC = pcFolder + devFile.getDeviceFilePath().toString();
/* 154 */       logFileOnPC = logFileOnPC.replace("//", "/");
/*     */       
/*     */ 
/* 157 */       this.plf.startParse(sn, logFileOnPC);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 162 */     this.plf.write(sn, zipPath);
/*     */   }
/*     */   
/*     */   private boolean isFileTypeExist(File path, String extName) {
/* 166 */     boolean exist = false;
/* 167 */     if ((path != null) && (path.isDirectory())) {
/* 168 */       File[] list = path.listFiles();
/* 169 */       if ((list == null) || (list.length == 0))
/* 170 */         return false;
/*     */       File[] arrayOfFile1;
/* 172 */       int j = (arrayOfFile1 = list).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/* 173 */         if (file.isDirectory()) {
/* 174 */           if (isFileTypeExist(file, extName)) {
/* 175 */             return true;
/*     */           }
/*     */         }
/* 178 */         else if (file.getAbsolutePath().endsWith(extName))
/*     */         {
/*     */ 
/* 181 */           if (!extName.equals(".zip"))
/* 182 */             return true;
/* 183 */           if ((extName.equals(".zip")) && 
/* 184 */             (file.getAbsolutePath().replace("\\", "/").contains("/data/core/"))) {
/* 185 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 191 */       Log.logger.error("path not exsit: " + path);
/*     */     }
/* 193 */     return exist;
/*     */   }
/*     */   
/*     */   private void pcMoveFolder(String sourceFolder, String destPCFolder)
/*     */   {
/* 198 */     if (new File(sourceFolder).isDirectory()) {
/*     */       try {
/* 200 */         pcCopyDirectory(new File(sourceFolder), new File(destPCFolder));
/* 201 */         pcDeleteDirectory(new File(sourceFolder));
/*     */       }
/*     */       catch (IOException e) {
/* 204 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void pcCopyDirectory(File sourceLocation, File targetLocation) throws IOException
/*     */   {
/* 211 */     if (sourceLocation.isDirectory()) {
/* 212 */       if (!targetLocation.exists()) {
/* 213 */         targetLocation.mkdir();
/*     */       }
/*     */       
/* 216 */       String[] children = sourceLocation.list();
/* 217 */       for (int i = 0; i < children.length; i++) {
/* 218 */         pcCopyDirectory(new File(sourceLocation, children[i]), 
/* 219 */           new File(targetLocation, children[i]));
/*     */       }
/*     */     }
/*     */     else {
/* 223 */       InputStream in = new FileInputStream(sourceLocation);
/* 224 */       OutputStream out = new FileOutputStream(targetLocation);
/*     */       
/*     */ 
/* 227 */       byte[] buf = new byte['Ѐ'];
/*     */       int len;
/* 229 */       while ((len = in.read(buf)) > 0) { int len;
/* 230 */         out.write(buf, 0, len);
/*     */       }
/* 232 */       in.close();
/* 233 */       out.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean pcDeleteDirectory(File directory) {
/* 238 */     if (directory.getAbsolutePath().length() < 5) {
/* 239 */       return false;
/*     */     }
/* 241 */     if (directory.exists()) {
/* 242 */       File[] files = directory.listFiles();
/* 243 */       if (files != null) {
/* 244 */         for (int i = 0; i < files.length; i++) {
/* 245 */           if (files[i].isDirectory()) {
/* 246 */             pcDeleteDirectory(files[i]);
/*     */           } else {
/* 248 */             files[i].delete();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 253 */     return directory.delete();
/*     */   }
/*     */   
/*     */   public void testPull(String sn, String pcPath, ArrayList<DeviceFileDetail> list)
/*     */   {
/* 258 */     AdbOP.testpull10Files(sn, pcPath, list);
/*     */   }
/*     */   
/*     */   public ArrayList<DeviceFileDetail> renamePullRemove(String sn, boolean root, String pcPath, ArrayList<DeviceFileDetail> list)
/*     */   {
/* 263 */     ArrayList<DeviceFileDetail> renamelist = renameLogFiles(sn, list);
/* 264 */     list = AdbOP.pullFiles(sn, pcPath, renamelist);
/* 265 */     AdbOP.removeFiles(sn, root, renamelist);
/* 266 */     return list;
/*     */   }
/*     */   
/*     */   private void zipAndDeleteLogFolder(String zipPath, String pcPath) {
/* 270 */     if (new File(pcPath).listFiles().length != 0) {
/* 271 */       FileOpHelper.doZip(zipPath, pcPath);
/*     */     } else {
/* 273 */       Log.logger.info("NO logs pulled out in this check !!!");
/*     */     }
/* 275 */     Log.logger.info("delete dir: " + pcPath);
/* 276 */     FileOpHelper.deleteDirectory(new File(pcPath));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ArrayList<DeviceFileDetail> filterOutCurrentLoggingFolders(String sn, ArrayList<DeviceFileDetail> list)
/*     */   {
/* 283 */     HashSet<String> filterSet = getCurrentLoggingFolderSet(sn, list);
/*     */     
/*     */ 
/* 286 */     ArrayList<DeviceFileDetail> newList = new ArrayList();
/*     */     
/* 288 */     for (DeviceFileDetail deviceFile : list) {
/* 289 */       if (!deviceFile.getFilename().endsWith("file_tree.txt"))
/*     */       {
/*     */ 
/*     */ 
/* 293 */         String folder = deviceFile.getDeviceFolder();
/* 294 */         if (!folder.contains("/aee_exp/temp/"))
/*     */         {
/*     */ 
/* 297 */           if ((!folder.contains("mdlog")) || (this.config.isPullModemLog()))
/*     */           {
/*     */ 
/* 300 */             if ((!folder.contains("/data/core/")) || (!deviceFile.getFilename().endsWith(".tmp")))
/*     */             {
/*     */ 
/*     */ 
/* 304 */               if (filterSet.contains(folder)) {
/* 305 */                 if (folder.contains("/mtklog/mobilelog/"))
/*     */                 {
/* 307 */                   if ((!deviceFile.getFilename().endsWith(".curf")) && 
/* 308 */                     (!deviceFile.getFilename().endsWith("_log")))
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/* 313 */                     if (deviceFile.getFilename().contains("_log"))
/*     */                     {
/* 315 */                       newList.add(deviceFile); }
/*     */                   }
/* 317 */                 } else if (folder.contains("/mtklog/netlog/"))
/*     */                 {
/*     */ 
/* 320 */                   if ((deviceFile.getFilename().contains(".tmp")) || 
/* 321 */                     (deviceFile.getFilename().endsWith(".txt")))
/*     */                   {
/* 323 */                     Log.logger.info("current netlog: " + 
/* 324 */                       deviceFile.getDeviceFilePath().toString());
/*     */                   } else {
/* 326 */                     newList.add(deviceFile);
/*     */                   }
/* 328 */                 } else if (folder.contains("/mtklog/mdlog/")) {
/* 329 */                   if ((deviceFile.getFilename().endsWith(".dmp.dmp")) || 
/* 330 */                     (deviceFile.getFilename().endsWith(".bin.bin")) || 
/* 331 */                     (deviceFile.getFilename().endsWith(".txt")))
/*     */                   {
/* 333 */                     Log.logger.info("current mdlog: " + 
/* 334 */                       deviceFile.getDeviceFilePath().toString());
/*     */                   } else {
/* 336 */                     newList.add(deviceFile);
/*     */                   }
/* 338 */                 } else if (folder.contains("/mtklog/mdlog1/")) {
/* 339 */                   if ((deviceFile.getFilename().endsWith(".tmp")) || 
/* 340 */                     (deviceFile.getFilename().endsWith(".txt")))
/*     */                   {
/* 342 */                     Log.logger.info("current mdlog1: " + 
/* 343 */                       deviceFile.getDeviceFilePath().toString());
/*     */                   } else {
/* 345 */                     newList.add(deviceFile);
/*     */                   }
/* 347 */                 } else if (folder.contains("/mtklog/mdlog3/")) {
/* 348 */                   if ((deviceFile.getFilename().endsWith(".tmp")) || 
/* 349 */                     (deviceFile.getFilename().endsWith(".txt")))
/*     */                   {
/* 351 */                     Log.logger.info("current mdlog3: " + 
/* 352 */                       deviceFile.getDeviceFilePath().toString());
/*     */                   } else {
/* 354 */                     newList.add(deviceFile);
/*     */                   }
/* 356 */                 } else if (folder.contains("/data/mdlog/bootupLog/")) {
/* 357 */                   if ((deviceFile.getFilename().endsWith(".tmp")) || 
/* 358 */                     (deviceFile.getFilename().endsWith(".txt")))
/*     */                   {
/* 360 */                     Log.logger.info("current bootupLog: " + 
/* 361 */                       deviceFile.getDeviceFilePath().toString());
/*     */                   } else {
/* 363 */                     newList.add(deviceFile);
/*     */                   }
/*     */                 }
/*     */                 else {
/* 367 */                   Log.logger.info("current log: " + 
/* 368 */                     deviceFile.getDeviceFilePath().toString());
/*     */                 }
/*     */               } else
/* 371 */                 newList.add(deviceFile); } } }
/*     */       }
/*     */     }
/* 374 */     return newList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HashSet<String> getCurrentLoggingFolderSet(String sn, ArrayList<DeviceFileDetail> list)
/*     */   {
/* 385 */     String currentMobilelogFolder = "";
/* 386 */     long currentMobilelogTime = Long.MAX_VALUE;
/*     */     
/*     */ 
/* 389 */     String currentNetlogFolder = "";
/* 390 */     long currentNetlogTime = Long.MAX_VALUE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 397 */     String currentModemlogFolder = "";
/* 398 */     long currentModemlogTime = Long.MAX_VALUE;
/*     */     
/*     */ 
/* 401 */     String currentModemlog_1_Folder = "";
/* 402 */     long currentModemlog_1_Time = Long.MAX_VALUE;
/*     */     
/*     */ 
/* 405 */     String currentModemlog_3_Folder = "";
/* 406 */     long currentModemlog_3_Time = Long.MAX_VALUE;
/*     */     
/*     */ 
/* 409 */     String currentBootUpModemlogFolder = "";
/* 410 */     long currentBootUpModemlogTime = Long.MAX_VALUE;
/*     */     
/*     */ 
/* 413 */     long deviceNow = AdbOP.getDeviceTime(sn);
/*     */     
/* 415 */     if (deviceNow <= 0L) {
/* 416 */       deviceNow = Long.MAX_VALUE;
/* 417 */       Log.logger.warn("devices now: can not get device time!!");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 422 */     for (DeviceFileDetail deviceFile : list) {
/* 423 */       long timeToNow = deviceNow - deviceFile.getTime();
/* 424 */       if (timeToNow < 0L) {
/* 425 */         Log.logger.warn("skip file! modify in future: " + 
/* 426 */           deviceFile.getDeviceFilePath().toString() + 
/* 427 */           " ll: " + deviceFile.getRaw() + 
/* 428 */           " devices now: " + new Date(deviceNow).toLocaleString());
/*     */ 
/*     */ 
/*     */       }
/* 432 */       else if ((deviceFile.getDeviceFolder().endsWith("/mtklog/mobilelog/")) || 
/* 433 */         (deviceFile.getDeviceFolder().endsWith("/mtklog/mdlog/")) || 
/* 434 */         (deviceFile.getDeviceFolder().endsWith("/mtklog/mdlog1/")) || 
/* 435 */         (deviceFile.getDeviceFolder().endsWith("/mtklog/mdlog3/")) || 
/*     */         
/* 437 */         (deviceFile.getDeviceFolder().endsWith("/mtklog/netlog/")) || 
/* 438 */         (deviceFile.getDeviceFolder().endsWith("/data/mdlog/bootupLog/"))) {
/* 439 */         Log.logger.warn("skip root folder: " + deviceFile.getDeviceFolder());
/*     */ 
/*     */ 
/*     */       }
/* 443 */       else if (deviceFile.getDeviceFolder().contains("/mtklog/mobilelog/"))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 453 */         if (timeToNow < currentMobilelogTime) {
/* 454 */           currentMobilelogTime = timeToNow;
/* 455 */           currentMobilelogFolder = deviceFile.getDeviceFolder();
/*     */         }
/*     */       }
/* 458 */       else if (deviceFile.getDeviceFolder().contains("/mtklog/netlog/")) {
/* 459 */         if (timeToNow < currentNetlogTime) {
/* 460 */           currentNetlogTime = timeToNow;
/* 461 */           currentNetlogFolder = deviceFile.getDeviceFolder();
/*     */         }
/* 463 */       } else if (deviceFile.getDeviceFolder().contains("/mtklog/mdlog/")) {
/* 464 */         if (timeToNow < currentModemlogTime) {
/* 465 */           currentModemlogTime = timeToNow;
/* 466 */           currentModemlogFolder = deviceFile.getDeviceFolder();
/*     */         }
/* 468 */       } else if (deviceFile.getDeviceFolder().contains("/mtklog/mdlog1/")) {
/* 469 */         if (timeToNow < currentModemlog_1_Time) {
/* 470 */           currentModemlog_1_Time = timeToNow;
/* 471 */           currentModemlog_1_Folder = deviceFile.getDeviceFolder();
/*     */         }
/* 473 */       } else if (deviceFile.getDeviceFolder().contains("/mtklog/mdlog3/")) {
/* 474 */         if (timeToNow < currentModemlog_3_Time) {
/* 475 */           currentModemlog_3_Time = timeToNow;
/* 476 */           currentModemlog_3_Folder = deviceFile.getDeviceFolder();
/*     */         }
/*     */       }
/* 479 */       else if ((deviceFile.getDeviceFolder().contains("/data/mdlog/bootupLog/")) && 
/* 480 */         (timeToNow < currentBootUpModemlogTime)) {
/* 481 */         currentBootUpModemlogTime = timeToNow;
/* 482 */         currentBootUpModemlogFolder = deviceFile.getDeviceFolder();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 487 */     HashSet<String> filterSet = new HashSet();
/* 488 */     if (currentMobilelogFolder.length() > 0) {
/* 489 */       filterSet.add(currentMobilelogFolder);
/* 490 */       Log.logger.info("skip currentMobilelogFolder: " + currentMobilelogFolder);
/*     */     }
/* 492 */     if (currentNetlogFolder.length() > 0) {
/* 493 */       filterSet.add(currentNetlogFolder);
/* 494 */       Log.logger.info("skip currentNetlogFolder: " + currentNetlogFolder);
/*     */     }
/* 496 */     if (currentModemlogFolder.length() > 0) {
/* 497 */       filterSet.add(currentModemlogFolder);
/* 498 */       Log.logger.info("skip currentModemlogFolder: " + currentModemlogFolder);
/*     */     }
/*     */     
/* 501 */     if (currentModemlog_1_Folder.length() > 0) {
/* 502 */       filterSet.add(currentModemlog_1_Folder);
/* 503 */       Log.logger.info("skip currentModemlog_1_Folder: " + currentModemlog_1_Folder);
/*     */     }
/* 505 */     if (currentModemlog_3_Folder.length() > 0) {
/* 506 */       filterSet.add(currentModemlog_3_Folder);
/* 507 */       Log.logger.info("skip currentModemlog_3_Folder: " + currentModemlog_3_Folder);
/*     */     }
/* 509 */     if (currentBootUpModemlogFolder.length() > 0) {
/* 510 */       filterSet.add(currentBootUpModemlogFolder);
/* 511 */       Log.logger.info("skip currentBootUpModemlogFolder: " + currentBootUpModemlogFolder);
/*     */     }
/* 513 */     return filterSet;
/*     */   }
/*     */   
/*     */   private ArrayList<DeviceFileDetail> renameLogFiles(String sn, ArrayList<DeviceFileDetail> list)
/*     */   {
/* 518 */     ArrayList<DeviceFileDetail> newList = new ArrayList();
/*     */     
/* 520 */     for (DeviceFileDetail deviceFile : list) {
/* 521 */       String oldName = deviceFile.getFilename();
/* 522 */       String folderPath = deviceFile.getDeviceFolder();
/* 523 */       String newName = oldName;
/*     */       
/* 525 */       if (deviceFile.getFilename().endsWith(".dbg")) {
/* 526 */         int index = DevicesFileIdexManager.getNextIndexOfPC(sn, 
/* 527 */           deviceFile.getDeviceFilePath());
/* 528 */         if (index != 20) {
/* 529 */           newName = oldName + "." + index + ".dbg";
/*     */         }
/* 531 */       } else if ((folderPath.contains("netlog")) && 
/* 532 */         (deviceFile.getFilename().contains("_V2_"))) {
/* 533 */         newName = oldName + ".LoNg";
/*     */       }
/* 535 */       else if ((deviceFile.getFilename().endsWith("1.cap")) || 
/* 536 */         (deviceFile.getFilename().endsWith("2.cap")) || 
/* 537 */         (deviceFile.getFilename().endsWith("3.cap")) || 
/* 538 */         (deviceFile.getFilename().endsWith("4.cap"))) {
/* 539 */         String repeatStr = oldName.substring(0, oldName.length() - 5) + 
/* 540 */           ".cap";
/* 541 */         int index = DevicesFileIdexManager.getNextIndexOfPC(sn, 
/* 542 */           new DeviceFilePath(deviceFile.getDeviceFolder(), 
/* 543 */           repeatStr));
/* 544 */         newName = repeatStr + index + ".cap";
/*     */       }
/* 546 */       else if ((folderPath.contains("mobilelog")) && 
/* 547 */         (deviceFile.getFilename().contains("_log_"))) {
/* 548 */         newName = oldName + ".LoNg";
/*     */       }
/* 550 */       else if ((oldName.endsWith(".1")) || (oldName.endsWith(".2")) || 
/*     */       
/* 552 */         (oldName.endsWith(".3")) || (oldName.endsWith(".4"))) {
/* 553 */         String repeatStr = oldName.substring(0, oldName.length() - 2);
/*     */         
/*     */ 
/* 556 */         int index = DevicesFileIdexManager.getNextIndexOfPC(sn, 
/* 557 */           new DeviceFilePath(deviceFile.getDeviceFolder(), 
/* 558 */           repeatStr));
/* 559 */         newName = repeatStr + "." + index;
/*     */       }
/* 561 */       else if ((oldName.endsWith("main_log")) || 
/* 562 */         (oldName.endsWith("radio_log")) || 
/* 563 */         (oldName.endsWith("kernel_log")) || 
/* 564 */         (oldName.endsWith("events_log"))) {
/* 565 */         int index = DevicesFileIdexManager.getNextIndexOfPC(sn, 
/* 566 */           deviceFile.getDeviceFilePath());
/* 567 */         if (index != 20) {
/* 568 */           newName = oldName + "." + index;
/*     */         }
/*     */       }
/* 571 */       else if ((deviceFile.getFilename().contains("_V2_")) && 
/* 572 */         (oldName.endsWith(".cap"))) {
/* 573 */         int index = DevicesFileIdexManager.getNextIndexOfPC(sn, 
/* 574 */           deviceFile.getDeviceFilePath());
/* 575 */         if (index != 20) {
/* 576 */           newName = oldName.replace(".cap", index + 
/* 577 */             ".cap");
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 584 */         int index = DevicesFileIdexManager.getNextIndexOfPC(sn, 
/* 585 */           deviceFile.getDeviceFilePath());
/* 586 */         if (index != 20) {
/* 587 */           String suffix = getSuffix(deviceFile.getFilename());
/* 588 */           if (suffix.length() > 10)
/* 589 */             suffix = ".long_suffix";
/* 590 */           newName = oldName + "." + index + suffix;
/*     */         }
/*     */       }
/*     */       
/* 594 */       if (newName.compareTo(oldName) != 0) {
/* 595 */         boolean result = 
/* 596 */           AdbOP.renameTargetFile(sn, deviceFile, newName);
/* 597 */         if (result) {
/* 598 */           newList.add(new DeviceFileDetail(deviceFile.getDeviceFolder(), newName, 
/* 599 */             deviceFile.getModifyTime(), deviceFile.getTime(), 
/* 600 */             deviceFile.getFileSize(), "rename files"));
/*     */         }
/*     */         else {
/* 603 */           newList.add(deviceFile);
/*     */         }
/*     */       } else {
/* 606 */         newList.add(deviceFile);
/*     */       }
/*     */     }
/* 609 */     return newList;
/*     */   }
/*     */   
/*     */   private String getSuffix(String name) {
/* 613 */     String suffix = "";
/* 614 */     int index = name.lastIndexOf(".");
/* 615 */     if (index != -1) {
/* 616 */       suffix = name.substring(index, name.length());
/*     */     }
/* 618 */     return suffix;
/*     */   }
/*     */   
/*     */   private boolean isException(ArrayList<DeviceFileDetail> list) {
/* 622 */     boolean dbExsit = false;
/* 623 */     for (DeviceFileDetail deviceFile : list) {
/* 624 */       if ((deviceFile.getFilename().endsWith(".dbg")) && 
/* 625 */         (!deviceFile.getDeviceFolder().contains("/aee_exp/temp/"))) {
/* 626 */         dbExsit = true;
/* 627 */         Log.logger.info("found .dbg file: " + 
/* 628 */           deviceFile.getDeviceFilePath());
/* 629 */         break; }
/* 630 */       if ((deviceFile.getFilename().endsWith(".zip")) && 
/* 631 */         (deviceFile.getDeviceFolder().contains("/data/core/"))) {
/* 632 */         dbExsit = true;
/* 633 */         Log.logger.info("found zcore-debuggerd-xxx file: " + 
/* 634 */           deviceFile.getDeviceFilePath());
/* 635 */         break;
/*     */       }
/*     */     }
/* 638 */     return dbExsit;
/*     */   }
/*     */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/ProcessRun.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */