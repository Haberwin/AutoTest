/*    */ package com.mediatek.LoNg;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class DevicesFileIdexManager {
/*    */   public static final int START_INDEX = 20;
/*  7 */   private static HashMap<String, HashMap<String, Integer>> allDevices = new HashMap();
/*    */   
/*    */   public static synchronized int getNextIndexOfPC(String sn, DeviceFilePath DevicePath)
/*    */   {
/* 11 */     String fullPath = DevicePath.toString();
/* 12 */     HashMap<String, Integer> devices = (HashMap)allDevices.get(sn);
/* 13 */     if (devices != null) {
/* 14 */       Integer index = (Integer)devices.get(fullPath);
/* 15 */       if (index == null) {
/* 16 */         devices.put(fullPath, new Integer(20));
/* 17 */         return 20;
/*    */       }
/* 19 */       devices.put(fullPath, Integer.valueOf(index.intValue() + 1));
/* 20 */       return ((Integer)devices.get(fullPath)).intValue();
/*    */     }
/*    */     
/* 23 */     HashMap<String, Integer> logItem = new HashMap();
/* 24 */     logItem.put(fullPath, new Integer(20));
/* 25 */     allDevices.put(sn, logItem);
/* 26 */     return 20;
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/com/mediatek/LoNg/DevicesFileIdexManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */