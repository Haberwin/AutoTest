/*     */ package com.mediatek.LoNg.utils.log;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class ParseLogFile
/*     */ {
/*     */   private String currentZipPath;
/*  16 */   private ArrayList<String> zz_internalFileList = new ArrayList();
/*  17 */   private HashMap<String, ArrayList<String>> zz_internalFileMap = new HashMap();
/*  18 */   private ArrayList<String> zipedFiles = new ArrayList();
/*     */   
/*  20 */   private static Pattern ptnAndroidLog = Pattern.compile(
/*  21 */     "(^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+) .*");
/*  22 */   private static Pattern ptnKernelLog = Pattern.compile(
/*  23 */     "\\<\\d{1,2}\\>\\[ *(\\d+\\.\\d+)\\].*");
/*     */   
/*  25 */   private static Pattern ptnName = Pattern.compile(".*\\/([\\(\\)a-zA-Z0-9_.-]*)");
/*     */   
/*     */   public ParseLogFile() {
/*  28 */     this.currentZipPath = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void startParse(String sn, String logFile)
/*     */   {
/*  34 */     ArrayList<String> zz_internalContent = new ArrayList();
/*     */     
/*  36 */     int i = 20;
/*  37 */     int hit = 0;
/*  38 */     String finalLogFile = logFile;
/*     */     
/*  40 */     Matcher m = ptnName.matcher(logFile);
/*  41 */     if (m.matches()) {
/*  42 */       Log.DEBUG("logfile hit");
/*  43 */       finalLogFile = m.group(1);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  48 */       BufferedReader br = new BufferedReader(
/*  49 */         new InputStreamReader(
/*  50 */         new FileInputStream(logFile)));
/*     */       String line;
/*     */       do { String line;
/*  53 */         if (finalLogFile.indexOf("ZZ_INTERNAL") >= 0) {
/*  54 */           zz_internalContent.add(line);
/*  55 */         } else if (logFile.contains("/mobilelog/")) {
/*  56 */           if (finalLogFile.indexOf("kernel_log") >= 0) {
/*  57 */             m = ptnKernelLog.matcher(line);
/*  58 */             if (!m.matches())
/*  59 */               m = ptnAndroidLog.matcher(line);
/*     */           } else {
/*  61 */             m = ptnAndroidLog.matcher(line); }
/*  62 */           if (m.matches()) {
/*  63 */             hit = 1;
/*  64 */             this.zipedFiles.add("  -- " + sn + "; " + finalLogFile + ";\tstartTime: " + m.group(1) + '\n');
/*  65 */             break;
/*     */           }
/*     */         }
/*  52 */         i--; } while ((i >= 0) && ((line = br.readLine()) != null));
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
/*     */ 
/*     */ 
/*  70 */       br.close();
/*     */     }
/*     */     catch (FileNotFoundException e) {
/*  73 */       e.printStackTrace();
/*     */     }
/*     */     catch (IOException e) {
/*  76 */       e.printStackTrace();
/*     */     }
/*  78 */     if (finalLogFile.indexOf("ZZ_INTERNAL") >= 0)
/*     */     {
/*  80 */       this.zz_internalFileList.add(logFile);
/*  81 */       this.zz_internalFileMap.put(logFile, zz_internalContent);
/*     */     }
/*  83 */     if (hit == 0) {
/*  84 */       Log.DEBUG("nothing write to the file 'ZipFileMap'");
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void write(String sn, String zipPath)
/*     */   {
/*  90 */     String currentZipName = null;
/*  91 */     if (!zipPath.equals(this.currentZipPath))
/*  92 */       this.currentZipPath = zipPath;
/*  93 */     Matcher m = ptnName.matcher(this.currentZipPath);
/*  94 */     if (m.matches()) {
/*  95 */       currentZipName = m.group(1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 100 */     for (String s : this.zz_internalFileList) {
/* 101 */       FileWrite.write("DBFileMap", s + "  ");
/* 102 */       for (String s0 : (ArrayList)this.zz_internalFileMap.get(s)) {
/* 103 */         FileWrite.write("DBFileMap", s0);
/*     */       }
/* 105 */       FileWrite.write("DBFileMap", "\n");
/*     */     }
/*     */     
/* 108 */     FileWrite.write("ZipFileMap", currentZipName + ":\n");
/* 109 */     for (String s : this.zipedFiles) {
/* 110 */       FileWrite.write("ZipFileMap", s);
/*     */     }
/*     */     
/* 113 */     this.zz_internalFileMap.clear();
/* 114 */     this.zz_internalFileList.clear();
/* 115 */     this.zipedFiles.clear();
/*     */   }
/*     */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/utils/log/ParseLogFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */