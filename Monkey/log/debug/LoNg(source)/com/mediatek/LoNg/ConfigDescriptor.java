/*    */ package com.mediatek.LoNg;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ 
/*    */ public class ConfigDescriptor
/*    */ {
/*  7 */   private int sleep = 5;
/*  8 */   private boolean modemLogAlwaysOn = false;
/*  9 */   private boolean pullModemLog = true;
/*    */   
/* 11 */   private HashSet<String> logPath = new HashSet();
/* 12 */   private String pcStoragePath = new java.io.File("").getAbsolutePath();
/*    */   
/*    */   public boolean isModemLogAlwaysOn() {
/* 15 */     return this.modemLogAlwaysOn;
/*    */   }
/*    */   
/*    */   public void setModemLogAlwaysOn(boolean modemLogUSBMode) {
/* 19 */     this.modemLogAlwaysOn = modemLogUSBMode;
/*    */   }
/*    */   
/*    */   public int getSleep() {
/* 23 */     return this.sleep;
/*    */   }
/*    */   
/*    */   public void setSleep(int sleep) {
/* 27 */     this.sleep = sleep;
/*    */   }
/*    */   
/*    */   public void setPullModemLog(boolean pullModemLog) {
/* 31 */     this.pullModemLog = pullModemLog;
/*    */   }
/*    */   
/*    */   public boolean isPullModemLog() {
/* 35 */     return this.pullModemLog;
/*    */   }
/*    */   
/*    */   public void addLogPath(String path) {
/* 39 */     this.logPath.add(path);
/*    */   }
/*    */   
/*    */   public String getPcStoragePath() {
/* 43 */     return this.pcStoragePath;
/*    */   }
/*    */   
/*    */   public HashSet<String> getLogPath() {
/* 47 */     HashSet<String> fixedPath = new HashSet();
/* 48 */     for (String path : this.logPath) {
/* 49 */       if (!path.endsWith("/")) {
/* 50 */         fixedPath.add(path + "/");
/*    */       } else {
/* 52 */         fixedPath.add(path);
/*    */       }
/*    */     }
/* 55 */     return fixedPath;
/*    */   }
/*    */   
/*    */ 
/*    */   ConfigDescriptor()
/*    */   {
/* 61 */     this.logPath.add("/mnt/sdcard/mtklog/");
/* 62 */     this.logPath.add("/mnt/sdcard0/mtklog/");
/* 63 */     this.logPath.add("/mnt/sdcard1/mtklog/");
/* 64 */     this.logPath.add("/mnt/sdcard2/mtklog/");
/* 65 */     this.logPath.add("/mnt/m_external_sd/mtklog");
/*    */     
/* 67 */     this.logPath.add("/data/aee_exp/");
/* 68 */     this.logPath.add("/data/tombstones/");
/* 69 */     this.logPath.add("/data/core/");
/* 70 */     this.logPath.add("/data/mdlog/bootupLog/");
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     String path = "";
/* 77 */     for (String p : this.logPath) {
/* 78 */       path = path + p + ",";
/*    */     }
/* 80 */     return 
/* 81 */       "ConfigDescriptor [sleep=" + this.sleep + ", logPath=" + path + ", pcStoragePath=" + this.pcStoragePath + ", pull_modem_log=" + this.pullModemLog + "]";
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/ConfigDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */