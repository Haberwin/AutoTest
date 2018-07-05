/*    */ package com.mediatek.LoNg.utils.log;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class FileWrite
/*    */ {
/*    */   private static final String ktLog = "KT-log/";
/* 14 */   private static HashMap<String, FileWriter> ioFileWter = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static synchronized void openFile(String fileName)
/*    */   {
/* 22 */     String filePath = "KT-log/" + fileName;
/* 23 */     if (!new File("KT-log/").exists())
/* 24 */       new File("KT-log/").mkdirs();
/* 25 */     File tagFile = new File(filePath);
/* 26 */     if (tagFile.exists()) {
/* 27 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
/* 28 */       File tagFile_bak = new File(filePath + "-" + sdf.format(new Date()));
/* 29 */       boolean renameOK = tagFile.renameTo(tagFile_bak);
/* 30 */       if (renameOK)
/* 31 */         System.out.println("rename " + filePath + " to " + tagFile_bak);
/*    */     }
/*    */     try {
/* 34 */       FileWriter fileWterItem = new FileWriter(filePath);
/* 35 */       ioFileWter.put(fileName, fileWterItem);
/*    */     }
/*    */     catch (IOException e) {
/* 38 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public static synchronized void write(String fileName, String s) {
/* 43 */     if (ioFileWter.get(fileName) == null) {
/* 44 */       openFile(fileName);
/*    */     }
/* 46 */     FileWriter fileWterItem = (FileWriter)ioFileWter.get(fileName);
/*    */     try {
/* 48 */       fileWterItem.write(s, 0, s.length());
/*    */     }
/*    */     catch (IOException e) {
/* 51 */       e.printStackTrace();
/*    */     }
/*    */     
/*    */ 
/*    */     try
/*    */     {
/* 57 */       fileWterItem.flush();
/*    */     }
/*    */     catch (IOException e) {
/* 60 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/utils/log/FileWrite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */