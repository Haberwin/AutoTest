/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ThreadLocalMap
/*    */   extends InheritableThreadLocal
/*    */ {
/*    */   public final Object childValue(Object parentValue)
/*    */   {
/* 35 */     Hashtable ht = (Hashtable)parentValue;
/* 36 */     if (ht != null) {
/* 37 */       return ht.clone();
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/helpers/ThreadLocalMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */