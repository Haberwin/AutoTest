/*    */ package com.mediatek.LoNg.utils.log;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.log4j.Level;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.PatternLayout;
/*    */ import org.apache.log4j.RollingFileAppender;
/*    */ 
/*    */ public class ThreadLog
/*    */ {
/* 11 */   private Logger logger = null;
/* 12 */   private RollingFileAppender rollFileAppender = null;
/*    */   
/*    */   public ThreadLog() {}
/*    */   
/*    */   public ThreadLog(String sn)
/*    */   {
/* 18 */     String str = "thread-" + sn;
/* 19 */     this.logger = Logger.getLogger(str);
/*    */     
/* 21 */     PatternLayout layout = new PatternLayout("[%d{ISO8601}] (%t)(%F): %m%n");
/*    */     try
/*    */     {
/* 24 */       this.rollFileAppender = new RollingFileAppender(layout, "KT-log/" + str + "-kt.log", true);
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 28 */       e.printStackTrace();
/*    */     }
/*    */     
/* 31 */     this.rollFileAppender.setThreshold(Level.DEBUG);
/* 32 */     this.rollFileAppender.setMaxFileSize("500000KB");
/* 33 */     this.rollFileAppender.setMaxBackupIndex(1);
/* 34 */     this.rollFileAppender.activateOptions();
/*    */     
/* 36 */     this.logger.setLevel(Level.DEBUG);
/* 37 */     this.logger.addAppender(this.rollFileAppender);
/*    */   }
/*    */   
/*    */ 
/*    */   public Logger getLogger()
/*    */   {
/* 43 */     return this.logger;
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/utils/log/ThreadLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */