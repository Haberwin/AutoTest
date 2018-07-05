/*    */ package com.mediatek.LoNg.utils.log;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.PropertyConfigurator;
/*    */ 
/*    */ public class Log
/*    */ {
/*    */   private static final String KTLOG = "KT-log/";
/* 13 */   public static Logger logger = null;
/*    */   
/*    */   static {
/* 16 */     String filePath = "KT-log/kt.log";
/* 17 */     if (!new File("KT-log/").exists())
/* 18 */       new File("KT-log/").mkdirs();
/* 19 */     File ktlog = new File(filePath);
/* 20 */     if (ktlog.exists()) {
/* 21 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
/* 22 */       File ktlog_bak = new File("KT-log/kt.log_" + sdf.format(new Date()));
/* 23 */       boolean renameOK = ktlog.renameTo(ktlog_bak);
/* 24 */       if (renameOK)
/* 25 */         System.out.println("rename kt.log to " + ktlog_bak);
/*    */     }
/* 27 */     if (logger == null) {
/* 28 */       PropertyConfigurator.configure("log4j.properties");
/* 29 */       logger = LogManager.getLogger("");
/*    */     }
/*    */   }
/*    */   
/*    */   public static void DEBUG(String msg) {}
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/utils/log/Log.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */