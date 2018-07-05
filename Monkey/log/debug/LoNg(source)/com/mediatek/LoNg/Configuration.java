/*    */ package com.mediatek.LoNg;
/*    */ 
/*    */ import com.mediatek.LoNg.utils.log.Log;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileReader;
/*    */ import java.util.ArrayList;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class Configuration
/*    */ {
/* 12 */   private final String CONFIG_ERROR = "Config error: ";
/* 13 */   private final String SLEEP = "sleep=";
/* 14 */   private final String LOGPATH = "logpath=";
/* 15 */   private final String PULL_MODEM_LOG = "pull_modem_log=";
/* 16 */   private final String CONFIG_INI = "LoNg.ini";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConfigDescriptor getConfig()
/*    */   {
/* 24 */     ConfigDescriptor config = new ConfigDescriptor();
/*    */     
/* 26 */     String[] lines = new String[0];
/*    */     try {
/* 28 */       FileReader fread = new FileReader("LoNg.ini");
/* 29 */       BufferedReader br = new BufferedReader(fread);
/* 30 */       ArrayList<String> list = new ArrayList();
/* 31 */       while (br.ready()) {
/* 32 */         list.add(br.readLine());
/*    */       }
/* 34 */       lines = new String[list.size()];
/* 35 */       list.toArray(lines);
/* 36 */       fread.close();
/*    */     } catch (FileNotFoundException e) {
/* 38 */       Log.logger.fatal("Config error: File Not Found: LoNg.ini");
/*    */       
/* 40 */       return config;
/*    */     } catch (java.io.IOException e) {
/* 42 */       Log.logger.fatal("Config error: IOException: LoNg.ini");
/*    */       
/* 44 */       return config;
/*    */     }
/*    */     
/* 47 */     for (int i = 0; i < lines.length; i++) {
/* 48 */       if (lines[i].startsWith("sleep=")) {
/* 49 */         String strSleepTime = lines[i].replace("sleep=", "").trim();
/* 50 */         if (strSleepTime.length() > 0) {
/* 51 */           int sleep = Integer.parseInt(strSleepTime);
/* 52 */           if ((sleep < 5) || (sleep > 60))
/*    */           {
/* 54 */             Log.logger.fatal("Config error: sleep time out of range. default sleep 5 minus.");
/*    */           }
/*    */           else {
/* 57 */             config.setSleep(sleep);
/*    */           }
/*    */         }
/*    */       }
/* 61 */       if (lines[i].startsWith("logpath=")) {
/* 62 */         String strLogPath = lines[i].replace("logpath=", "").trim();
/* 63 */         if (strLogPath.length() > 0) {
/* 64 */           String[] split = strLogPath.split(",");
/* 65 */           String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String path = arrayOfString1[i];
/* 66 */             String trim = path.trim();
/* 67 */             if (trim.length() > 3) {
/* 68 */               config.addLogPath(trim);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/* 73 */       if (lines[i].startsWith("pull_modem_log=")) {
/* 74 */         String pullModemLog = lines[i].replace("pull_modem_log=", "").trim();
/* 75 */         if ((pullModemLog.length() > 0) && (
/* 76 */           (pullModemLog.equals("true")) || (pullModemLog.equals("false")))) {
/* 77 */           config.setPullModemLog(pullModemLog.equals("true"));
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 83 */     Log.logger.info(config);
/*    */     
/* 85 */     return config;
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/Configuration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */