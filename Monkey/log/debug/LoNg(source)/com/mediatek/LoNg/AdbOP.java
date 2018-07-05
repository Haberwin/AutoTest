/*     */ package com.mediatek.LoNg;
/*     */ 
/*     */ import com.mediatek.LoNg.utils.log.Log;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class AdbOP
/*     */ {
/*  18 */   static final Pattern pLSTime = Pattern.compile(".* (\\d+) (\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d) (.*)");
/*     */   
/*     */   static boolean restartMtklogger(String sn, boolean modemLogAlwaysOn)
/*     */   {
/*  22 */     Log.logger.info("restart mtklogger SN: " + sn);
/*     */     try {
/*  24 */       Thread.sleep(40000L);
/*     */     } catch (InterruptedException e) {
/*  26 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*  30 */     ArrayList<String> command = new ArrayList();
/*  31 */     command.add("shell");
/*  32 */     command.add("am");
/*  33 */     command.add("broadcast");
/*  34 */     command.add("-a");
/*  35 */     command.add("com.mediatek.mtklogger.ADB_CMD");
/*  36 */     command.add("-e");
/*  37 */     command.add("cmd_name");
/*  38 */     command.add("stop");
/*  39 */     command.add("--ei");
/*  40 */     command.add("cmd_target");
/*  41 */     if (modemLogAlwaysOn) {
/*  42 */       command.add("5");
/*     */     } else {
/*  44 */       command.add("7");
/*     */     }
/*  46 */     command.add("-f");
/*  47 */     command.add("0x04000000");
/*  48 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/*     */     try
/*     */     {
/*  51 */       Thread.sleep(15000L);
/*     */     } catch (InterruptedException e) {
/*  53 */       e.printStackTrace();
/*     */     }
/*  55 */     startMtklogger(sn);
/*  56 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startMtklogger(String sn)
/*     */   {
/*  64 */     ArrayList<String> command = new ArrayList();
/*  65 */     command.add("shell");
/*  66 */     command.add("am");
/*  67 */     command.add("broadcast");
/*  68 */     command.add("-a");
/*  69 */     command.add("com.mediatek.mtklogger.ADB_CMD");
/*  70 */     command.add("-e");
/*  71 */     command.add("cmd_name");
/*  72 */     command.add("start");
/*  73 */     command.add("--ei");
/*  74 */     command.add("cmd_target");
/*  75 */     command.add("7");
/*  76 */     command.add("-f");
/*  77 */     command.add("0x04000000");
/*  78 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/*     */     try
/*     */     {
/*  81 */       Thread.sleep(5000L);
/*     */     } catch (InterruptedException e) {
/*  83 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   static HashSet<String> getOnlineDevices()
/*     */   {
/*  89 */     ArrayList<String> command = new ArrayList();
/*  90 */     command.add("devices");
/*  91 */     ArrayList<String> adbResult = AdbAdapter.Execute(command);
/*     */     
/*  93 */     HashSet<String> devices = new HashSet();
/*  94 */     for (String string : adbResult)
/*     */     {
/*  96 */       String STR_DEVICE = "\tdevice";
/*  97 */       String STR_DEVICE_OFFLINE = "offline";
/*  98 */       if (string.endsWith("\tdevice")) {
/*  99 */         String sn = string.replace("\tdevice", "").trim();
/* 100 */         if (sn.length() > 1) {
/* 101 */           Log.logger.info("device SN: " + sn);
/* 102 */           devices.add(sn);
/*     */         }
/* 104 */       } else if (string.endsWith("offline")) {
/* 105 */         String sn = string.replace("offline", "").trim();
/* 106 */         if (sn.length() > 1) {
/* 107 */           Log.logger.warn("device offline: " + sn);
/*     */         }
/*     */       }
/*     */     }
/* 111 */     return devices;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean startMtkloggerAndDisableTaglog(String sn)
/*     */   {
/* 121 */     ArrayList<String> command = new ArrayList();
/* 122 */     command.add("shell");
/* 123 */     command.add("am");
/* 124 */     command.add("broadcast");
/* 125 */     command.add("-a");
/* 126 */     command.add("com.mediatek.mtklogger.ADB_CMD");
/* 127 */     command.add("-e");
/* 128 */     command.add("cmd_name");
/* 129 */     command.add("switch_taglog");
/* 130 */     command.add("--ei");
/* 131 */     command.add("cmd_target");
/* 132 */     command.add("0");
/* 133 */     command.add("-f");
/* 134 */     command.add("0x04000000");
/* 135 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/*     */     
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   static ArrayList<DeviceFileDetail> pullFiles(String sn, String pcPath, ArrayList<DeviceFileDetail> list)
/*     */   {
/* 142 */     ArrayList<DeviceFileDetail> sucessList = new ArrayList();
/* 143 */     for (DeviceFileDetail deviceFileDetail : list) {
/* 144 */       String pcFolder = 
/* 145 */         (pcPath + deviceFileDetail.getDeviceFolder()).replaceAll("//", "/");
/* 146 */       new File(pcFolder).mkdirs();
/* 147 */       String pcTargetFile = pcFolder + deviceFileDetail.getFilename();
/*     */       
/* 149 */       ArrayList<String> command = new ArrayList();
/* 150 */       command.add("pull");
/* 151 */       command.add(deviceFileDetail.getDeviceFilePath().toString());
/* 152 */       command.add(pcTargetFile);
/*     */       
/*     */ 
/* 155 */       ArrayList<String> adbResult = new ArrayList();
/* 156 */       boolean isEqual = false;
/* 157 */       for (int again = 0; (!isEqual) && (again < 2); again++) {
/* 158 */         adbResult = AdbAdapter.ExecuteSN(sn, command);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */         File pcFile = new File(pcTargetFile);
/* 172 */         if ((pcFile.exists()) && (pcFile.isFile())) {
/* 173 */           isEqual = deviceFileDetail.getFileSize() == pcFile.length();
/*     */         } else {
/* 175 */           isEqual = false;
/*     */         }
/* 177 */         if (!isEqual) {
/* 178 */           Log.logger.warn("File: " + deviceFileDetail.getFilename() + 
/* 179 */             " file_size: " + deviceFileDetail.getFileSize() + 
/* 180 */             " pulled_size: " + pcFile.length());
/* 181 */           for (String string : adbResult) {
/* 182 */             Log.logger.warn("Size not equal ! sn: " + sn + " " + string);
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 190 */           sucessList.add(deviceFileDetail);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 210 */         Thread.sleep(1000L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */     }
/* 214 */     return sucessList;
/*     */   }
/*     */   
/* 217 */   static void testpull10Files(String sn, String pcPath, ArrayList<DeviceFileDetail> list) { for (int i = 10; (i < list.size()) && (i < 20); i++) {
/* 218 */       DeviceFileDetail deviceFileDetail = (DeviceFileDetail)list.get(i);
/* 219 */       String pcFolder = pcPath + deviceFileDetail.getDeviceFolder();
/* 220 */       new File(pcFolder).mkdirs();
/*     */       
/* 222 */       ArrayList<String> command = new ArrayList();
/* 223 */       command.add("pull");
/* 224 */       command.add(deviceFileDetail.getDeviceFilePath().toString());
/* 225 */       command.add(pcFolder + "/" + deviceFileDetail.getFilename());
/* 226 */       ArrayList localArrayList = AdbAdapter.ExecuteSN(sn, command);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ArrayList<DeviceFileDetail> getAllLogList(String sn, ConfigDescriptor config) {
/* 231 */     ArrayList<DeviceFileDetail> list = lsAll(sn, config);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 239 */     list = sortLogFiles(list);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 249 */     return list;
/*     */   }
/*     */   
/*     */   private static ArrayList<DeviceFileDetail> sortLogFiles(ArrayList<DeviceFileDetail> list)
/*     */   {
/* 254 */     java.util.Collections.sort(list, new Comparator() {
/*     */       public int compare(DeviceFileDetail o1, DeviceFileDetail o2) {
/* 256 */         String str1 = o1.getDeviceFolder() + o1.getFilename();
/* 257 */         String str2 = o2.getDeviceFolder() + o2.getFilename();
/* 258 */         return str2.compareTo(str1);
/*     */       }
/*     */       
/* 261 */     });
/* 262 */     return list;
/*     */   }
/*     */   
/*     */ 
/* 266 */   static final SimpleDateFormat shellDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*     */   
/*     */   static long getDeviceTime(String sn) {
/* 269 */     ArrayList<String> command = new ArrayList();
/* 270 */     command.add("shell");
/* 271 */     command.add("date");
/* 272 */     command.add("+%Y-%m-%d %H:%M:%S");
/* 273 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/* 274 */     for (String string : adbResult) {
/*     */       try {
/* 276 */         Date date = shellDateFormatter.parse(string);
/* 277 */         return date.getTime();
/*     */       }
/*     */       catch (ParseException localParseException) {}
/*     */     }
/*     */     
/* 282 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ArrayList<DeviceFileDetail> lsAll(String sn, ConfigDescriptor config)
/*     */   {
/* 288 */     ArrayList<String> command = new ArrayList();
/* 289 */     command.add("shell");
/* 290 */     command.add("ls");
/* 291 */     command.add("-R");
/* 292 */     command.add("-l");
/* 293 */     for (String string : config.getLogPath()) {
/* 294 */       command.add(string);
/*     */     }
/* 296 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 306 */     String currentDir = "";
/* 307 */     SimpleDateFormat lsFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
/* 308 */     ArrayList<DeviceFileDetail> allFiles = new ArrayList();
/* 309 */     for (String line : adbResult) {
/* 310 */       if (line.startsWith("/")) {
/* 311 */         for (String expPath : config.getLogPath()) {
/* 312 */           if (line.startsWith(expPath)) {
/* 313 */             currentDir = line.replace("//", "/").replace(":", "");
/* 314 */             if (!currentDir.endsWith("/")) {
/* 315 */               currentDir = currentDir + "/";
/*     */             }
/*     */           }
/*     */         }
/* 319 */       } else if (line.startsWith("-"))
/*     */       {
/*     */ 
/* 322 */         Matcher m = pLSTime.matcher(line);
/* 323 */         if (m.matches()) {
/* 324 */           String strSize = m.group(1);
/* 325 */           long size = Long.parseLong(strSize);
/* 326 */           String modifyTime = m.group(2);
/*     */           try
/*     */           {
/* 329 */             Date date = lsFormatter.parse(modifyTime);
/* 330 */             String filename = m.group(3).trim();
/* 331 */             long time = date.getTime();
/* 332 */             allFiles.add(new DeviceFileDetail(currentDir, filename, 
/* 333 */               modifyTime, time, size, line));
/*     */           } catch (ParseException e) {
/* 335 */             e.printStackTrace();
/* 336 */             System.out.println("modifyTime:  " + modifyTime);
/* 337 */             Log.logger.fatal("date format changed!!");
/*     */           }
/*     */         } else {
/* 340 */           Log.logger.fatal("shell ls -l format unkown!!");
/*     */         }
/* 342 */       } else { line.startsWith("d");
/*     */       }
/*     */     }
/*     */     
/* 346 */     return allFiles;
/*     */   }
/*     */   
/*     */   public static boolean renameTargetFile(String sn, DeviceFileDetail deviceFile, String newName)
/*     */   {
/* 351 */     String oldName = deviceFile.getDeviceFolder() + "/" + 
/* 352 */       deviceFile.getFilename();
/* 353 */     oldName = oldName.replaceAll("//", "/");
/*     */     
/* 355 */     newName = deviceFile.getDeviceFolder() + "/" + newName;
/* 356 */     newName = newName.replaceAll("//", "/");
/*     */     
/* 358 */     ArrayList<String> command = new ArrayList();
/* 359 */     command.add("shell");
/* 360 */     command.add("mv");
/* 361 */     command.add(oldName);
/* 362 */     command.add(newName);
/* 363 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/* 364 */     for (String string : adbResult) {
/* 365 */       if (string.startsWith("failed")) {
/* 366 */         Log.logger.warn("sn: " + sn + " " + string);
/* 367 */         return false;
/*     */       }
/*     */     }
/* 370 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   static boolean removeFiles(String sn, boolean root, ArrayList<DeviceFileDetail> list)
/*     */   {
/* 376 */     for (DeviceFileDetail deviceFileDetail : list) {
/* 377 */       String filePath = deviceFileDetail.getDeviceFilePath().toString();
/* 378 */       if ((!root) && (filePath.startsWith("/data/"))) {
/* 379 */         Log.logger.info("Shell cmd will not rm ReadOnly file: " + filePath);
/*     */       }
/*     */       else {
/* 382 */         ArrayList<String> command = new ArrayList();
/* 383 */         command.add("shell");
/* 384 */         command.add("rm");
/* 385 */         command.add(filePath);
/* 386 */         ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/* 387 */         for (String string : adbResult) {
/* 388 */           if (string.startsWith("rm failed"))
/* 389 */             Log.logger.warn("sn: " + sn + " " + string);
/*     */         }
/*     */       }
/*     */     }
/* 393 */     return true;
/*     */   }
/*     */   
/*     */   static boolean isRootShell(String sn) {
/* 397 */     ArrayList<String> command = new ArrayList();
/* 398 */     command.add("shell");
/* 399 */     command.add("id");
/* 400 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/* 401 */     for (String string : adbResult) {
/* 402 */       if (string.contains("root")) {
/* 403 */         return true;
/*     */       }
/*     */     }
/* 406 */     return false;
/*     */   }
/*     */   
/*     */   static String shellCatFile(String sn, String file) {
/* 410 */     ArrayList<String> command = new ArrayList();
/* 411 */     command.add("shell");
/* 412 */     command.add("cat");
/* 413 */     command.add(file);
/* 414 */     ArrayList<String> adbResult = AdbAdapter.ExecuteSN(sn, command);
/* 415 */     String result = "";
/* 416 */     for (String string : adbResult) {
/* 417 */       result = result + string;
/*     */     }
/* 419 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/AdbOP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */