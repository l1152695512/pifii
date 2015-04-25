/*    */ package com;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;

import com.pifii.dao.CutTableJdbc;
import com.pifii.util.DateUtil;
import com.pifii.util.ResourcesUtil;
/*    */ 
/*    */ public class Test
/*    */ {
///*    */   public static void main(String[] args)
///*    */   {
///* 18 */     ExecutorService threadPool = Executors.newCachedThreadPool();
///*    */ 
///* 20 */     for (int i = 0; i < 10; i++) {
///*    */       try {
///* 22 */         Thread.sleep(1L);
///*    */       } catch (InterruptedException e) {
///* 24 */         e.printStackTrace();
///*    */       }
///*    */ 
///* 27 */       threadPool.execute(new TestThread());
///*    */     }
///*    */   }
	
	
	
	
	public static void main(String[] args) {
		 String[] logArray = "PiFii_ynsqt_07 process: |witfii:|8d19fae51c3f7cd8|ec:85:2f:6d:47:2e|Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Mobile/11D201|http://m.baidu.com/sug?control=sug&query=5.303435E+3065%B7%9D&uid=31E58EE1E44695526CC49F1811186BDB509B44C62OGLIBFREIJ&ua=640_960_iphone_5.4.5.0_0&ut=iPhone4".split("witfii:");
		 /*    */ 
		 /* 56 */     if (logArray.length >= 2) {
		 /* 57 */       String[] logs = logArray[1].split("\\|");
		 /*    */ 
		 /* 59 */       if (logs.length >= 3) {
		 /* 60 */         String wifiName = logs[0].trim();
		 System.out.println(wifiName+"wifiName");
		 /* 61 */         String mac = logs[1].trim();
		 System.out.println(mac+"mac");
		 /* 62 */         String url = logs[2].trim();
		 System.out.println(url+"url");
		 /* 63 */         String type = logs[3].trim();
		 System.out.println(type+"type");
		 /*    */ 
		 /* 65 */         int index = url.indexOf("Android");
		 /* 67 */         boolean isMobile = false;
		 /*    */ 
		 /* 69 */         if (index > -1) {
		 /* 70 */           type = "Android";
		 /* 71 */           isMobile = true;
		 /*    */         }
		 /*    */ 
		 /* 74 */         index = type.indexOf("iPhone");
		 /*    */ 			System.out.println(index+"index");
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
		 /*    */ System.out.println("type"+type);
		 }
}}
	
	
	
	 @org.junit.Test
	 public void testNewDate(){
		 SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");
		System.out.println(sdf.format(new Date()));;
//		 while(true){
//			 SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
//			 System.out.println(sdf.format(new Date()));
//			 System.out.println("10:57".equals(sdf.format(new Date())));
//			 
//		 }
	 }
	 
	 @org.junit.Test
	 public void testResultUtil(){
		 System.out.println(CutTableJdbc.isdureHere("bpbaselogtbl","2014-12-23"));
		 CutTableJdbc.deleteDureTab("bpbaselogtbl", "2014-12-23");
	 }
	 @org.junit.Test
	 public void testLIke(){
		 System.out.println(CutTableJdbc.getReplace("bpbaselogtbl", "2015-01-08"));;
	 }
	 
	 @org.junit.Test
	 public void testjdbc(){
		 List<String> lt=CutTableJdbc.getReplace("bpbaselogtbl", "2015-01-08");
		 for (String object : lt) {
			 System.out.println(object);
			 CutTableJdbc.deleteDureTab("bpbaselogtbl", object);
		}
//		 System.out.println(DateUtil.getTabCurrentDay("2015-01-08"));;
		 
	 }
	 
}
