/*    */ package com.ifidc.common;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*    */ 
/*    */ public class ApplicationContextHelper
/*    */ {
/* 15 */   private static String CONFIG_FILE_LOCATION = "applicationContext.xml";
/*    */   private static ApplicationContext applicationContext;
/* 17 */   private static String configFile = CONFIG_FILE_LOCATION;
/*    */   private static ExecutorService threadPool;
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 21 */       applicationContext = new ClassPathXmlApplicationContext(configFile);
/* 22 */       threadPool = Executors.newCachedThreadPool();
/*    */     } catch (Exception e) {
/* 24 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static ApplicationContext getApplicationContext() {
/* 29 */     return applicationContext;
/*    */   }
/*    */ 
/*    */   public static ExecutorService getTheadPool() {
/* 33 */     return threadPool;
/*    */   }
/*    */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.ifidc.common.ApplicationContextHelper
 * JD-Core Version:    0.6.0
 */