/*    */ package com.ifidc.common;
/*    */ 
/*    */ import com.ifidc.LogToDB;
/*    */ import java.io.PrintStream;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import org.productivity.java.syslog4j.server.SyslogServerEventIF;
/*    */ import org.productivity.java.syslog4j.server.SyslogServerIF;
/*    */ import org.productivity.java.syslog4j.server.SyslogServerSessionEventHandlerIF;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ 
/*    */ public class SyslogToDBEventHandler
/*    */   implements SyslogServerSessionEventHandlerIF
/*    */ {
/*    */   private static final long serialVersionUID = 6036415838696050746L;
/* 22 */   protected PrintStream stream = null;
/*    */ 
/*    */   public SyslogToDBEventHandler(PrintStream stream) {
/* 25 */     this.stream = stream;
/*    */   }
/*    */ 
/*    */   public void initialize(SyslogServerIF syslogServer)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object sessionOpened(SyslogServerIF syslogServer, SocketAddress socketAddress)
/*    */   {
/* 34 */     return null;
/*    */   }
/*    */ 
/*    */   public void event(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event)
/*    */   {
/* 39 */     ApplicationContext ctx = 
/* 40 */       ApplicationContextHelper.getApplicationContext();
/* 41 */     ServerConfig serverConfig = (ServerConfig)ctx.getBean("ServerConfig");
/* 42 */     String[] filters = serverConfig.getFilter().split(",");
/* 43 */     boolean isExist = false;
/*    */ 
/* 45 */     for (int i = 0; i < filters.length; i++) {
/* 46 */       if (event.getMessage().contains(filters[i])) {
/* 47 */         isExist = true;
/* 48 */         break;
/*    */       }
/*    */     }
/*    */ 
/* 52 */     if (!isExist)
/* 53 */       ApplicationContextHelper.getTheadPool().execute(new LogToDB(event));
/*    */   }
/*    */ 
/*    */   public void exception(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void sessionClosed(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, boolean timeout)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void destroy(SyslogServerIF syslogServer)
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.ifidc.common.SyslogToDBEventHandler
 * JD-Core Version:    0.6.0
 */