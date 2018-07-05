/*    */ package com.mediatek.LoNg;
/*    */ 
/*    */ public class DeviceFileDetail
/*    */ {
/*    */   private DeviceFilePath path;
/*    */   private String strTime;
/*    */   private long time;
/*    */   private long size;
/*    */   private String raw;
/*    */   
/*    */   public DeviceFileDetail(String deviceFolder, String filename, String modifyTime, long time, long size, String raw)
/*    */   {
/* 13 */     this.path = new DeviceFilePath(deviceFolder, filename);
/* 14 */     this.strTime = modifyTime;
/* 15 */     this.time = time;
/* 16 */     this.size = size;
/* 17 */     this.raw = raw;
/*    */   }
/*    */   
/*    */   public void setData(DeviceFilePath data)
/*    */   {
/* 22 */     this.path = data;
/*    */   }
/*    */   
/*    */   public DeviceFilePath getDeviceFilePath()
/*    */   {
/* 27 */     return this.path;
/*    */   }
/*    */   
/*    */   public String getDeviceFolder() {
/* 31 */     return this.path.deviceFolder;
/*    */   }
/*    */   
/*    */   public String getFilename() {
/* 35 */     return this.path.filename;
/*    */   }
/*    */   
/*    */   public String getModifyTime() {
/* 39 */     return this.strTime;
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 43 */     return this.time;
/*    */   }
/*    */   
/*    */   public long getFileSize() {
/* 47 */     return this.size;
/*    */   }
/*    */   
/*    */   public String getRaw() {
/* 51 */     return this.raw;
/*    */   }
/*    */   
/*    */   public String showDeviceFileDetial() {
/* 55 */     return "FileDetail: " + this.path + " " + this.strTime + " " + this.time + " " + this.size + " " + this.raw;
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/DeviceFileDetail.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */