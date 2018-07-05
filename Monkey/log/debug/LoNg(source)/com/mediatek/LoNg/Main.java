/*    */ package com.mediatek.LoNg;
/*    */ 
/*    */ import com.mediatek.LoNg.utils.log.Log;
/*    */ import java.io.File;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.HashSet;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   private static final String VERISON = "v2.1710.5";
/* 14 */   private static final ConfigDescriptor config = new Configuration().getConfig();
/*    */   
/*    */ 
/*    */ 
/* 18 */   private static final SimpleDateFormat MTBF_LOG_FOLDER_FORMATER = new SimpleDateFormat(
/* 19 */     "yyyyMMdd_HHmmss");
/*    */   
/*    */   private static final String MTBF_LOG_FOLDER = "MTBF-log";
/* 22 */   private static final String rootFolder = config.getPcStoragePath() + "/" + 
/* 23 */     "MTBF-log" + "/" + 
/* 24 */     MTBF_LOG_FOLDER_FORMATER.format(new Date());
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 28 */     if (args.length == 0) {
/* 29 */       return;
/*    */     }
/* 31 */     if ((args.length >= 1) && 
/* 32 */       (args[0].compareTo("start") != 0)) {
/* 33 */       return;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */     Log.logger.info("LoNg version: v2.1710.5");
/*    */     
/* 48 */     new File(rootFolder).mkdirs();
/*    */     
/*    */ 
/* 51 */     ThreadGroup t_group = new ThreadGroup("main thread_group");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     for (;;)
/*    */     {
/* 59 */       Thread[] threads = new Thread[t_group.activeCount()];
/* 60 */       t_group.enumerate(threads, false);
/* 61 */       HashSet<String> td_devices = new HashSet();
/* 62 */       Thread[] arrayOfThread1; int j = (arrayOfThread1 = threads).length; for (int i = 0; i < j; i++) { Thread td = arrayOfThread1[i];
/* 63 */         td_devices.add(td.getName());
/*    */       }
/*    */       
/* 66 */       HashSet<String> devices = AdbOP.getOnlineDevices();
/*    */       Thread[] arrayOfThread2;
/* 68 */       int k = (arrayOfThread2 = threads).length; for (j = 0; j < k; j++) { Thread td = arrayOfThread2[j];
/* 69 */         Log.logger.info("current thread:" + td.getName() + td.getId());
/*    */       }
/*    */       
/* 72 */       for (String sn : devices)
/*    */       {
/* 74 */         if (!td_devices.contains("thread_" + sn))
/*    */         {
/* 76 */           ProcessRun p = new ProcessRun(sn, config, rootFolder);
/* 77 */           new Thread(t_group, p, "thread_" + sn).start();
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 82 */       if (devices.size() == 0) {
/* 83 */         Log.logger.info("No devices!!!");
/*    */       }
/*    */       try
/*    */       {
/* 87 */         Thread.sleep(360000L);
/*    */       }
/*    */       catch (InterruptedException e) {
/* 90 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */