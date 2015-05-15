package com.yinfu.cloudportal.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
public class Comms {
	private static Logger logger = Logger.getLogger(Comms.class);
//	public static void setSessionDeviceInfo(HttpServletRequest request,String routersn){
//		Object device = request.getSession().getAttribute(SessionParams.ACCESS_DEVICE);
//		Record thisDevice = Comms.getDeviceInfo(routersn);
//		if(null != thisDevice.get("id") || null == device){
//			request.getSession().setAttribute(SessionParams.ACCESS_DEVICE, thisDevice);
//		}
//	}
//	
	public static void setSessionClientInfo(HttpServletRequest request,String mac){
		Object client = request.getSession().getAttribute(SessionParams.CLIENT_INFO);
		Record thisClient = Comms.getClientInfo(mac);
		if(null != thisClient.get("id") || null == client){
			request.getSession().setAttribute(SessionParams.CLIENT_INFO, thisClient);
		}
	}
	
	public static Record getDeviceInfo(String routersn){
		Record device = new Record();
		if(StringUtils.isNotBlank(routersn)){
			device.set("router_sn", routersn);
			Record thisDevice = Db.findFirst("select id,router_sn,ifnull(shop_id,'') shop_id,type from bp_device where router_sn=? ", 
					new Object[]{routersn});
			if(null != thisDevice){
				device = thisDevice;
			}
		}else{
			device.set("router_sn", "");
		}
		return device;
	}
	
	public static Record getClientInfo(String mac){
		Record client = new Record();
		if(StringUtils.isNotBlank(mac)){
			client.set("client_mac", mac);
			Record thisClient = Db.findFirst("select * from bp_auth where auth_type='phone' and client_mac=? order by auth_date desc ", new Object[]{mac});
			if(null != thisClient){
				client = thisClient;
			}
		}else{
			client.set("client_mac", "");
		}
		return client;
	}
	
	//判断客户端类型
	public static boolean clientIsPC(HttpServletRequest request){
		//mozilla/5.0 (compatible; msie 10.0; windows phone 8.0; trident/6.0; iemobile/10.0; arm; touch; nokia; rm-1010)
		//
//		Enumeration<String> headers = request.getHeaderNames();
//		while(headers.hasMoreElements()){
//			String thisHeader = headers.nextElement();
//			System.err.println(thisHeader+"---------"+request.getHeader(thisHeader));
//		}
		String clientInfo = (null != request.getHeader("USER-AGENT"))?request.getHeader("USER-AGENT").toLowerCase():"";
//		logger.info(clientInfo);
		if(clientInfo.indexOf("windows nt") > 0//windows 系统
				|| (clientInfo.indexOf("linux") > 0 
						&& clientInfo.indexOf("android") == -1
						&& clientInfo.indexOf("mobile") == -1 )//linux系统
				|| clientInfo.indexOf("macintosh") > 0){//苹果系统
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean clientIsPad(HttpServletRequest request){
		String clientInfo = (null != request.getHeader("USER-AGENT"))?request.getHeader("USER-AGENT").toLowerCase():"";
		if((clientInfo.indexOf("android") != 1 && clientInfo.indexOf("mobile") == -1 )//android平板
				|| (clientInfo.indexOf("mac os") > 0 && clientInfo.indexOf("ipad") > 0)){//ipad
			return true;
		}else{
			return false;
		}
	}
	
	public static String getTemplateUrl(Object shopId,Object routersn){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.cloud_url ");
		sql.append("from bp_shop_page sp join bp_temp t on (sp.shop_id=? and sp.template_id=t.id) ");
		Record rec = Db.findFirst(sql.toString(),new Object[]{shopId});
		String url = "";
		if(null == rec || null == rec.get("cloud_url") || StringUtils.isBlank(rec.getStr("cloud_url"))){
			Record device = Db.findFirst("select type from bp_device where router_sn=? ", new Object[]{routersn});
			String templateMarker = "";
			if(null != device && null != device.get("type") && "2".equals(device.get("type").toString())){//吸顶的默认模板页
				templateMarker = "template2";
			}else{//盒子的默认模板页
				templateMarker = "template1";
			}
			Record defaultRec = Db.findFirst("select ifnull(cloud_url,'') cloud_url from bp_temp where marker=? ",new Object[]{templateMarker});
			url = defaultRec.getStr("cloud_url");
		}else{
			url = rec.getStr("cloud_url");
		}
		return url;
	}
}
