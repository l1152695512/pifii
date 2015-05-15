package com.yinfu;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Main {
	public static void main(String[] args) throws ParseException {
//		File file = new File("E:/tomcat/apache-tomcat-7.0.55/webapps/eclipse/webapps/pifii/upload/routerSyn/8cdddf77-f54e-4a58-a375-3f99eeaebf65.tar.gz");
//		System.err.println(file.length());
//		System.err.println(VerifyUtil.getMD5(file));
//		File file1 = new File("E:/tomcat/apache-tomcat-7.0.55/webapps/eclipse/webapps/pifii/upload/routerSyn/c3dee723-0a18-4b00-9a2b-dc7e261bef59.tar.gz");
//		System.err.println(file1.length());
//		System.err.println(VerifyUtil.getMD5(file1));
		
//		System.err.println(file.length());
//		System.err.println(file.getParent());
//		InitDemoDbConfig.initPlugin();
//		List<Record> list = Db.find("select * from bp_res_task where router_sn=?",new Object[]{"f128b86eeddb7029"});
//		Iterator<Record> ite = list.iterator();
//		List<String> sqls = new ArrayList<String>();
//		while(ite.hasNext()){
//			Record rec = ite.next();
//			File file = new File("E:/tomcat/apache-tomcat-7.0.55/webapps/eclipse/webapps/pifii/"+rec.getStr("res_url"));
//			if(file.exists()){
//				String md5 = VerifyUtil.getMD5(file);
//				if(!rec.getStr("md5").equals(md5)){
//					System.err.println(rec.getStr("id")+"----"+rec.getStr("res_url")+"---------"+file.length()+"---"+rec.getInt("file_size"));
//				}
//				if(file.length() != rec.getInt("file_size")){
////					sqls.add("update bp_res_task set md5='"+VerifyUtil.getMD5(file)+"',file_size="+file.length()+" where id='"+rec.getStr("id")+"'");
//					System.err.println(rec.getStr("id")+"----"+rec.getStr("res_url")+"---------"+file.length()+"---"+rec.getInt("file_size"));
//				}
//			}
//		}
//		System.err.println(sqls.size());
//		if(sqls.size() > 0){
//			Db.batch(sqls, sqls.size());
//		}
		
//		System.err.println(UUID.randomUUID().toString());
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM");
//		System.err.println(sdf.format(new Date()));
		
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date a = sdf.parse("13:26:00");
//		Date b = new Date(a.getTime()-1000);
//		System.err.println(sdf.format(b));
		
		System.err.println((int)(Math.random()*200));
	}
	
	public static void main1(String[] args) throws IOException {
//		String aa = "mb/mall/images";
//		System.err.println(aa.replaceAll("/",File.separator+File.separator));
		
//		String imgDir = "/storageroot/Data/mb/mall/images";
//    	String[] img = imgDir.split("/");
//    	imgDir = img[img.length-1];
//    	System.err.println(imgDir);
//		System.err.println(FlowPackTask.IMAGE_FOLDER.substring(FlowPackTask.IMAGE_FOLDER.lastIndexOf("/")));
		
		
//		System.err.println(new File("E:/1/Galactians2.tar.gz").getName());
//		System.err.println(new File("E:/1/DangerRanger").getName());
		
//		String apth = SynUtils.getResDownLoadFloder();
//		System.err.println(apth);
//		System.err.println(new File(apth).getParent());
//		String path = new File(apth).getParent()+".tar.gz";
//		System.err.println(path.substring(path.lastIndexOf(File.separator)+1));
		
//		StringBuffer sqls = new StringBuffer();
//		sqls.append("");
//		System.err.println(sqls.length());
		
//		InitDemoDbConfig.initPlugin();
//		List<Record> videos = Db.find("select id,delete_date from bp_video");
//		Iterator<Record> ite = videos.iterator();
//		while(ite.hasNext()){
//			Record rec = ite.next();
//			if(null == rec.get("delete_date")){
//				System.err.println(">>>>>>>>>null");
//			}else{
//				System.err.println(rec.get("delete_date"));
//			}
//		}
//		List<String> sqls = new ArrayList<String>();
//		List<Record> recs = Db.find("select id from bp_video");
//		Iterator<Record> ite = recs.iterator();
//		while(ite.hasNext()){
//			Record rec = ite.next();
//			sqls.add("update bp_video set md5='"+UUID.randomUUID().toString().replaceAll("-", "")+"' where id="+rec.get("id").toString());
//		}
//		Db.batch(sqls, sqls.size()+1);
		
//		System.err.println("aaaa".split("_")[0]);
//		File file = new File("E:/tomcat/apache-tomcat-7.0.55/webapps/eclipse/webapps/pifii/upload/routerSyn/e9d62962-f0d0-41e7-ace0-c6e7a2751ff1.tar.gz");
//		System.err.println(file.exists());
//		System.gc();
//		FileUtils.forceDelete(file);
//		forceDelete(file);
		
		System.err.println(UUID.randomUUID().toString());
	}
}
