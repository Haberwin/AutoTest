/*     */ package com.mediatek.LoNg;
/*     */ 
/*     */ import com.mediatek.LoNg.utils.log.Log;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ public class DataFolderCache
/*     */ {
/*     */   private static final String ZZ_INTERNAL = "ZZ_INTERNAL";
/*  10 */   private static HashSet<String> dataFiles = new HashSet();
/*     */   
/*     */ 
/*     */   static synchronized ArrayList<DeviceFileDetail> filterOutRepeatDataDBandCoredump(String sn, ArrayList<DeviceFileDetail> list, ArrayList<DeviceFileDetail> repeatList)
/*     */   {
/*  15 */     ArrayList<DeviceFileDetail> newList = new ArrayList();
/*     */     
/*  17 */     for (DeviceFileDetail deviceFile : list) {
/*  18 */       if (deviceFile.getDeviceFolder().startsWith("/data/"))
/*     */       {
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  59 */         String fileKey = "sn:" + sn + " path:" + 
/*  60 */           deviceFile.getDeviceFilePath().toString() + 
/*  61 */           " raw:" + deviceFile.getRaw();
/*     */         
/*  63 */         if (dataFiles.contains(fileKey)) {
/*  64 */           repeatList.add(deviceFile);
/*  65 */           Log.logger.info("filter out repeat data file. " + 
/*  66 */             fileKey);
/*     */         } else {
/*  68 */           dataFiles.add(fileKey);
/*  69 */           newList.add(deviceFile);
/*     */         }
/*     */       }
/*     */       else {
/*  73 */         newList.add(deviceFile);
/*     */       }
/*     */     }
/*  76 */     return newList;
/*     */   }
/*     */   
/*     */ 
/*     */   static ArrayList<DeviceFileDetail> removeRepeatDB(ArrayList<DeviceFileDetail> list, ArrayList<DeviceFileDetail> removeList)
/*     */   {
/*  82 */     if (removeList.size() == 0) {
/*  83 */       return list;
/*     */     }
/*  85 */     ArrayList<DeviceFileDetail> finalList = new ArrayList();
/*  86 */     for (DeviceFileDetail item : list) {
/*  87 */       boolean keep = true;
/*  88 */       for (DeviceFileDetail remove_item : removeList)
/*     */       {
/*  90 */         String removePath = remove_item.getDeviceFilePath()
/*  91 */           .toString();
/*  92 */         String path = item.getDeviceFilePath().toString();
/*  93 */         if (path.compareTo(removePath) == 0) {
/*  94 */           keep = false;
/*  95 */           break;
/*     */         }
/*     */       }
/*  98 */       if (keep) {
/*  99 */         finalList.add(item);
/*     */       } else {
/* 101 */         Log.logger.info("Ignore repeat db. " + 
/* 102 */           item.getDeviceFilePath().toString());
/*     */       }
/*     */     }
/* 105 */     return finalList;
/*     */   }
/*     */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/DataFolderCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */