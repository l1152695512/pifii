/*    */ package com;
/*    */ 
/*    */ import com.ifidc.common.ApplicationContextHelper;
/*    */ import com.ifidc.common.ServerConfig;
/*    */ import org.productivity.java.syslog4j.Syslog;
/*    */ import org.productivity.java.syslog4j.SyslogConfigIF;
/*    */ import org.productivity.java.syslog4j.SyslogIF;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ 
/*    */ public class LogClient
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 18 */     ApplicationContext ctx = 
/* 19 */       ApplicationContextHelper.getApplicationContext();
/* 20 */     ServerConfig serverConfig = (ServerConfig)ctx.getBean("ServerConfig");
/*    */ 
/* 22 */     SyslogIF syslog = Syslog.getInstance(serverConfig.getProtocol());
/* 23 */     syslog.getConfig().setHost(serverConfig.getIpAddr());
/* 24 */     syslog.getConfig().setPort(serverConfig.getPort());
/* 25 */     syslog.getConfig().setCharSet("utf-8");
/*    */ 
/* 27 */     TestLog log = (TestLog)ctx.getBean("TestLog");
/*    */ 
/* 29 */     syslog.debug("|" + log.getDeviceNo() + "|" + log.getType() + "|" + 
/* 30 */       log.getInputMac() + "|" + log.getIp() + "|" + log.getLink() + 
/* 31 */       "|");
/*    */   }
/*    */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.LogClient
 * JD-Core Version:    0.6.0
 */