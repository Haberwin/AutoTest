/*    */ package org.apache.log4j.spi;
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
/*    */ public class DefaultRepositorySelector
/*    */   implements RepositorySelector
/*    */ {
/*    */   final LoggerRepository repository;
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
/*    */   public DefaultRepositorySelector(LoggerRepository repository)
/*    */   {
/* 29 */     this.repository = repository;
/*    */   }
/*    */   
/*    */   public LoggerRepository getLoggerRepository()
/*    */   {
/* 34 */     return this.repository;
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/spi/DefaultRepositorySelector.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */