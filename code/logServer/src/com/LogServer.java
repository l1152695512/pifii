/*    */ package com;
/*    */ 
/*    */ import com.ifidc.common.ApplicationContextHelper;
/*    */ import com.ifidc.common.ServerConfig;
/*    */ import com.ifidc.common.SyslogToDBEventHandler;
import com.pifii.test.Table;
import com.pifii.timer.TimerCutTab;

/*    */ import java.util.concurrent.ExecutorService;

/*    */ import org.productivity.java.syslog4j.server.SyslogServer;
/*    */ import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
/*    */ import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
/*    */ import org.productivity.java.syslog4j.server.SyslogServerIF;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ 
/*    */ public class LogServer
/*    */ {
/* 21 */   private static final Logger log = LoggerFactory.getLogger(LogServer.class);
/*    */ 
/*    */   public static void main(String[] args) {
/* 24 */     log.info("因孚日志服务启动中,请稍候......");
/*    */ 
/* 26 */     ApplicationContext ctx = 
/* 27 */       ApplicationContextHelper.getApplicationContext();
/* 28 */     ServerConfig serverConfig = (ServerConfig)ctx.getBean("ServerConfig");
/*    */ 
/* 30 */     SyslogServerEventHandlerIF eh = new SyslogToDBEventHandler(System.out);
/* 31 */     SyslogServerIF serverIF = SyslogServer.getInstance(serverConfig
/* 32 */       .getProtocol());
/* 33 */     SyslogServerConfigIF config = serverIF.getConfig();
/* 34 */     config.setHost(serverConfig.getIpAddr());
/* 35 */     config.setPort(serverConfig.getPort());
/* 36 */     config.addEventHandler(eh);
/* 37 */     serverIF.initialize(serverConfig.getProtocol(), config);
/*    */ 
/* 39 */     log.info("因孚日志服务启动成功  IP：" + serverConfig.getIpAddr() + "  port：" + 
/* 40 */       serverConfig.getPort());
/* 41 */     serverIF.run();
//			 new TimerCutTab();
/* 42 */     ApplicationContextHelper.getTheadPool().shutdown();
/*    */   }
/*    */ }
