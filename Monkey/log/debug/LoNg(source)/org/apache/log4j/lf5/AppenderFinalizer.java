/*    */ package org.apache.log4j.lf5;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.apache.log4j.lf5.viewer.LogBrokerMonitor;
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
/*    */ public class AppenderFinalizer
/*    */ {
/* 41 */   protected LogBrokerMonitor _defaultMonitor = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AppenderFinalizer(LogBrokerMonitor defaultMonitor)
/*    */   {
/* 52 */     this._defaultMonitor = defaultMonitor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void finalize()
/*    */     throws Throwable
/*    */   {
/* 66 */     System.out.println("Disposing of the default LogBrokerMonitor instance");
/* 67 */     this._defaultMonitor.dispose();
/*    */   }
/*    */ }


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/lf5/AppenderFinalizer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */