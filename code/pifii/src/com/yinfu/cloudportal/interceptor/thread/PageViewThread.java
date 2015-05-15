package com.yinfu.cloudportal.interceptor.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;


public class PageViewThread extends Thread{
	private static Logger logger = Logger.getLogger(PageViewThread.class);
	private boolean hasCreateTable = false;//担心下面创建table的语句循环执行
	
	private String action;
	private String mac;
	private String sn;
	private Map<String,String[]> params;
	private String marker;
	
	public PageViewThread(String action,String mac,String sn,Map<String,String[]> params) {
		this.action = null == action?"":action;
		this.mac = null == mac?"":mac;
		this.sn = null == sn?"":sn;
//		this.marker = null == marker?"":marker;
		this.params = params;
	}
	
	@Override
	public void run() {
		if(action.equals("/portal/mb/temp1") && 
				(null == params.get("shopId") || StringUtils.isBlank(params.get("shopId")[0]))){//平台预览
			marker = "temp1";
		}else if(action.equals("/portal/mb/temp2") && 
				(null == params.get("shopId") || StringUtils.isBlank(params.get("shopId")[0])) && //平台预览
				(null == params.get("cmd") || StringUtils.isBlank(params.get("cmd")[0]))){//不是发送验证码或者认证
			marker = "temp2";
		}else if(action.equals("/portal/mb/temp3") && 
				(null == params.get("shopId") || StringUtils.isBlank(params.get("shopId")[0])) && //平台预览
				(null == params.get("cmd") || StringUtils.isBlank(params.get("cmd")[0]))){//不是发送验证码或者认证
			marker = "temp3";
		}else if(action.equals("/portal/mb/postal")){
			marker = "postal";
		}else if(action.equals("/portal/mb/nav")){
			marker = "nav";
		}
		if(StringUtils.isNotBlank(marker)){
			insert();
		}
	}
	
	private void insert(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM");
		String date = sdf.format(new Date());
		try{
			Db.update("insert into bp_page_view_"+date+"(url,mac,sn,marker,access_date) values(?,?,?,?,now())",
					new Object[]{action,mac,sn,marker});
		}catch(Exception e){
			String errorMessage = e.getMessage();
			if(errorMessage.indexOf("MySQLSyntaxErrorException") != -1 
					&& errorMessage.indexOf("Table") != -1 
					&& errorMessage.indexOf("doesn't exist") != -1
					&& !hasCreateTable){
				StringBuffer sql = new StringBuffer();
				sql.append("CREATE TABLE IF NOT EXISTS `bp_page_view_"+date+"`(");
				sql.append("`id` int(11) NOT NULL AUTO_INCREMENT,");
				sql.append("`url` varchar(500) DEFAULT NULL COMMENT '访问的url',");
				sql.append("`mac` varchar(50) DEFAULT NULL,");
				sql.append("`sn` varchar(50) DEFAULT NULL,");
				sql.append("`marker` varchar(50) DEFAULT NULL COMMENT '标识，用于针对某种类型的url做统计时使用',");
				sql.append("`access_date` datetime DEFAULT NULL,");
				sql.append("PRIMARY KEY (`id`)");
				sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
				Db.update(sql.toString());
				hasCreateTable = true;
				insert();
			}else{
				logger.warn("插入页面访问数据失败！", e);
			}
		}
	}
}
