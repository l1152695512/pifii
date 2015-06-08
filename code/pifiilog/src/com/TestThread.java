/*    */ package com;
/*    */ 
/*    */ import com.ifidc.common.ApplicationContextHelper;
/*    */ import com.ifidc.common.ServerConfig;
/*    */ import org.productivity.java.syslog4j.Syslog;
/*    */ import org.productivity.java.syslog4j.SyslogConfigIF;
/*    */ import org.productivity.java.syslog4j.SyslogIF;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ 
/*    */ public class TestThread
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 16 */     ApplicationContext ctx = 
/* 17 */       ApplicationContextHelper.getApplicationContext();
/* 18 */     ServerConfig serverConfig = (ServerConfig)ctx.getBean("ServerConfig");
/*    */ 
/* 20 */     SyslogIF syslog = Syslog.getInstance(serverConfig.getProtocol());
/* 21 */     syslog.getConfig().setHost(serverConfig.getIpAddr());
/* 22 */     syslog.getConfig().setPort(serverConfig.getPort());
/* 23 */     syslog.getConfig().setCharSet("utf-8");
/*    */ 
/* 25 */     TestLog log = (TestLog)ctx.getBean("TestLog");
/*    */ 
/* 27 */     syslog.debug("|" + log.getDeviceNo() + "|" + log.getType() + "|" + 
/* 28 */       log.getInputMac() + "|" + log.getIp() + "|" + log.getLink() + 
/* 29 */       "|");
/*    */   }
/*    */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.TestThread
 * JD-Core Version:    0.6.0
 */