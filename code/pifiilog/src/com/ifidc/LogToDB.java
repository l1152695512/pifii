/*    */ package com.ifidc;
/*    */ 
/*    */ import com.ifidc.common.ApplicationContextHelper;
/*    */ import com.ifidc.persistent.BpBaseLogTbl;
/*    */ import com.ifidc.persistent.BpBaseLogTblDAO;
import com.pifii.dao.CutTableJdbc;
import com.pifii.util.ResourcesUtil;

/*    */ import java.io.PrintStream;
/*    */ import java.sql.Timestamp;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
import java.util.Calendar;
/*    */ import java.util.Date;

/*    */ import org.productivity.java.syslog4j.server.SyslogServerEventIF;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ 
/*    */ public class LogToDB
/*    */   implements Runnable
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(LogToDB.class);
/*    */   private SyslogServerEventIF event;
/*    */ 
/*    */   public LogToDB(SyslogServerEventIF event)
/*    */   {
/* 27 */     this.event = event;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 41 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 42 */     String ds = df.format(this.event.getDate());
/* 43 */     Timestamp tt = null;
/*    */     try {
/* 45 */       Date ss = df.parse(ds);
/* 46 */       tt = new Timestamp(ss.getTime());
/*    */     } catch (ParseException e) {
/* 48 */       e.printStackTrace();
/*    */     }
/*    */ 
/* 51 */     BpBaseLogTbl bpBaseLogTbl = new BpBaseLogTbl();
/* 52 */     bpBaseLogTbl.setCreateDate(tt);
/*    */ 
/* 54 */     String[] logArray = this.event.getMessage().split("witfii:");
/*    */ 
/* 56 */     if (logArray.length >= 2) {
/* 57 */       String[] logs = logArray[1].split("\\|");
/*    */ 
/* 59 */       if (logs.length >= 3) {
/* 60 */         String wifiName = logs[0].trim();
/* 61 */         String mac = logs[1].trim();
				String local=logs[2].trim();//添加判断去除 无用的00:00:00:00:00MAC地址
/* 62 */         String url = logs[4].trim();
/* 63 */         String type = logs[3].trim();
/* 65 */         int index = type.indexOf("Android");
/* 67 */         boolean isMobile = false;
/*    */ 
/* 69 */         if (index > -1) {
/* 70 */           type = "Android";
/* 71 */           isMobile = true;
/*    */         }
/*    */ 
/* 74 */         index = type.indexOf("iPhone");
/*    */ 
/* 76 */         if (index > -1) {
/* 77 */           type = "iPhone";
/* 78 */           isMobile = true;
/*    */         }
/*    */ 
/* 81 */         index = type.indexOf("iPad");
/*    */ 
/* 83 */         if (index > -1) {
/* 84 */           type = "iPad";
/* 85 */           isMobile = true;
/*    */         }
/*    */ 
/* 88 */         if (!isMobile) {
/* 89 */           type = "PC";
/*    */         }

       bpBaseLogTbl.setDeviceNo(mac.trim());//设备编号
       bpBaseLogTbl.setInputMac(local.trim());
        bpBaseLogTbl.setLink(url);
        bpBaseLogTbl.setType(type);
        ApplicationContext ctx = ApplicationContextHelper.getApplicationContext();
        BpBaseLogTblDAO dao = (BpBaseLogTblDAO)ctx.getBean("BpBaseLogTblDAO");
        dao.save(bpBaseLogTbl);
        
        
        /*
        Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");// 按天分表之后只截取当天时间日期
		if (ResourcesUtil.getVbyKey("time").equals(sdf.format(new Date()))) {
			if (!CutTableJdbc.isHere(ResourcesUtil.getVbyKey("db"),
					ResourcesUtil.getVbyKey("tablename") + "_" + year + "_"
							+ month + "_" + day)) {
				log.debug("开始分表+++++++++++++++++++++++++++");
				log.info("分表日期：day :" + day + "month:" + month + ":year :"
						+ year);
				CutTableJdbc.reamTableName(true,
						ResourcesUtil.getVbyKey("tablename"), "_" + year + "_"
								+ month + "_" + day);
				log.debug("结束分表+++++++++++++++++++++++++++");
			}
		}*/
        
		
			}
	    }
   }
 }
