/*    */ package com.mediatek.LoNg;
/*    */ 
/*    */ public class DeviceFilePath
/*    */ {
/*    */   public String deviceFolder;
/*    */   public String filename;
/*    */   
/*    */   public DeviceFilePath(String deviceFolder, String filename) {
/*  9 */     this.deviceFolder = deviceFolder;
/* 10 */     this.filename = filename;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 14 */     return this.deviceFolder + this.filename;
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/DeviceFilePath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */