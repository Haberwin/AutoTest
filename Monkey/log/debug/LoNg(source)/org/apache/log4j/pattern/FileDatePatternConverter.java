/*    */ package org.apache.log4j.pattern;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FileDatePatternConverter
/*    */ {
/*    */   public static PatternConverter newInstance(String[] options)
/*    */   {
/* 41 */     if ((options == null) || (options.length == 0)) {
/* 42 */       return DatePatternConverter.newInstance(new String[] { "yyyy-MM-dd" });
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 48 */     return DatePatternConverter.newInstance(options);
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/pattern/FileDatePatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */