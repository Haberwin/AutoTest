/*     */ package com.mediatek.LoNg;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class AdbAdapter
/*     */ {
/*     */   public static final String ADB = "adb";
/*     */   
/*     */   public static ArrayList<String> ExecuteSN(String sn, ArrayList<String> parameter)
/*     */   {
/*  16 */     String path = new File("").getAbsolutePath();
/*  17 */     String ADB_path = path + File.separator + "adb";
/*     */     
/*  19 */     int len = 0;
/*  20 */     if (parameter != null) {
/*  21 */       len = parameter.size();
/*     */     }
/*  23 */     String[] command = new String[3 + len];
/*  24 */     command[0] = ADB_path;
/*  25 */     command[1] = "-s";
/*  26 */     command[2] = sn;
/*  27 */     for (int i = 3; i < command.length; i++) {
/*  28 */       command[i] = ((String)parameter.get(i - 3));
/*     */     }
/*  30 */     return ExecuteCommand(command);
/*     */   }
/*     */   
/*     */   public static ArrayList<String> Execute(ArrayList<String> parameter) {
/*  34 */     String path = new File("").getAbsolutePath();
/*  35 */     String ADB_path = path + File.separator + "adb";
/*     */     
/*  37 */     int len = 0;
/*  38 */     if (parameter != null) {
/*  39 */       len = parameter.size();
/*     */     }
/*  41 */     String[] command = new String[1 + len];
/*  42 */     command[0] = ADB_path;
/*  43 */     for (int i = 1; i < command.length; i++) {
/*  44 */       command[i] = ((String)parameter.get(i - 1));
/*     */     }
/*  46 */     return ExecuteCommand(command);
/*     */   }
/*     */   
/*     */   private static ArrayList<String> ExecuteCommand(String[] command) {
/*  50 */     final ArrayList<String> result = new ArrayList();
/*     */     
/*  52 */     Thread t1 = null;Thread t2 = null;
/*  53 */     Thread t3 = null;
/*     */     try
/*     */     {
/*  56 */       String log = "";
/*  57 */       String[] arrayOfString = command;int j = command.length; for (int i = 0; i < j; i++) { String string = arrayOfString[i];
/*  58 */         log = log + " " + string;
/*     */       }
/*  60 */       com.mediatek.LoNg.utils.log.Log.logger.info("shell cmd: " + log);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */       final Process proc = Runtime.getRuntime().exec(command, null, null);
/*  67 */       t1 = new Thread("")
/*     */       {
/*     */         public void run()
/*     */         {
/*  71 */           InputStreamReader is = new InputStreamReader(
/*  72 */             proc.getErrorStream());
/*  73 */           BufferedReader errReader = new BufferedReader(is);
/*     */           try
/*     */           {
/*     */             for (;;) {
/*  77 */               String line = errReader.readLine();
/*  78 */               if (line == null) break;
/*  79 */               result.add(line);
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           catch (IOException localIOException) {}
/*     */         }
/*     */         
/*     */ 
/*  89 */       };
/*  90 */       t2 = new Thread("")
/*     */       {
/*     */         public void run() {
/*  93 */           InputStreamReader is = new InputStreamReader(
/*  94 */             proc.getInputStream());
/*  95 */           BufferedReader outReader = new BufferedReader(is);
/*     */           try
/*     */           {
/*     */             for (;;) {
/*  99 */               String line = outReader.readLine();
/* 100 */               if (line == null) break;
/* 101 */               result.add(line);
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           catch (IOException localIOException) {}
/*     */         }
/*     */         
/*     */ 
/* 111 */       };
/* 112 */       t1.start();
/* 113 */       t2.start();
/*     */       
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 119 */         t1.join(180000L);
/* 120 */         t2.join(180000L);
/*     */       }
/*     */       catch (InterruptedException e) {
/* 123 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */       t3 = new Thread("WatchDog")
/*     */       {
/*     */         public void run() {
/* 133 */           int timeCount = 0;
/* 134 */           while (!Thread.currentThread().isInterrupted()) {
/*     */             try {
/* 136 */               Thread.sleep(5000L);
/*     */             }
/*     */             catch (InterruptedException e)
/*     */             {
/*     */               break;
/*     */             }
/* 142 */             timeCount++;
/*     */             
/* 144 */             if ((timeCount * 5 > 360) && (proc != null)) {
/* 145 */               com.mediatek.LoNg.utils.log.Log.logger.info("!!!Kill blocking adb, Adb has been blocking 12 minutes");
/* 146 */               proc.destroy();
/* 147 */               break;
/*     */             }
/*     */             
/*     */           }
/*     */         }
/* 152 */       };
/* 153 */       t3.start();
/*     */       
/*     */       try
/*     */       {
/* 157 */         proc.waitFor();
/*     */       }
/*     */       catch (InterruptedException e) {
/* 160 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 168 */       t3.interrupt();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 165 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 171 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/AdbAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */