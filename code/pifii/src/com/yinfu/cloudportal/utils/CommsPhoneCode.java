package com.yinfu.cloudportal.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.application.model.SmsFlow;
import com.yinfu.jbase.util.SendSms;
import com.yinfu.servlet.route.RouteAuth;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
public class CommsPhoneCode {
	private static Logger logger = Logger.getLogger(CommsPhoneCode.class);
	public static final String SEND_VALIDATE_CODE_ADDRESS = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	public static final String PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
	public static final String ACCOUNT = "cf_gzyf";
	public static final String PASSWORD = "2ZGH2v";
	
	public static final int VALIDATE_CODE_LENGTH = 4;
	public static final int MIN_GET_CODE_TIME = 60*1000;
	
	/**
	 * 用于客户端认证时发送验证码
	 * @param request
	 * @return
	 */
	public static JSONObject getCode(HttpServletRequest request){
		Record deviceRec=(Record)request.getSession().getAttribute(SessionParams.ACCESS_DEVICE);
		JSONObject info = new JSONObject();
		info.put("success", "0");
		String phone = request.getParameter("phone");
		if(null != deviceRec.get("id")){
			int deviceId = deviceRec.getInt("id");
			request.setAttribute("phone", phone);
			
			Pattern p = Pattern.compile(PATTERN);  
			Matcher m = p.matcher(phone); 
			long timeLeft = getPhoneCodeTimeleft(request);
			
//			long intervalTimes = MIN_GET_CODE_TIME;
//			if(null != request.getSession().getAttribute(SessionParams.CODE_SEND_DATE)){
//				intervalTimes = new Date().getTime() - ((Date)request.getSession().getAttribute(SessionParams.CODE_SEND_DATE)).getTime();
//			}
			if(m.matches() && timeLeft <= 0){
				//动态密码页面限制为4位
				String verifyCode = generVerifyCode();
				try {
					Element root = SendSms.send(phone,verifyCode);
					String code = root.elementText("code");	
					String msg = root.elementText("msg");
//					String smsid = root.elementText("smsid");
					if("2".equals(code)){
						//保存发送短信记录
						SmsFlow.dao.saveSmsFlowInfo(deviceId, phone);
						
						request.getSession().setAttribute(SessionParams.CODE_SEND,verifyCode);//将验证码放入session，这行代码要放在下面判断条件之前，因为有时候短信平台发送验证码失败后（code!=2），可能过一会手机又收到短信了
						info.put("success", "1");
						request.setAttribute("timeSeconds", 60);
						logger.info("获取验证码成功---->【phone="+phone+",verifyCode="+verifyCode+"】");
						request.getSession().setAttribute(SessionParams.CODE_SEND_DATE,new Date());
						request.getSession().setAttribute(SessionParams.CODE_SEND_PHONE,phone);
					}else if("0".equals(code)){
						info.put("msg", "第三方短信平台发送短信失败，请稍后重试！");
						logger.info("获取验证码失败---->【phone="+phone+"，msg="+msg+"】");
					}else{
						info.put("msg", msg);
						logger.info("获取验证码失败---->【phone="+phone+"，msg="+msg+"】");
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("获取验证码异常---->【phone="+phone+"，msg="+e.getMessage()+"】",e);
					info.put("msg", "第三方短信平台发送短信失败，请稍后重试！");
				} 
			}else{
				if(!m.matches()){
					info.put("msg", "手机号码有误，请修改！");
					logger.info("获取验证码失败---->【"+"手机号码有误:"+phone+"】");
				}else if(timeLeft <= 0){
					request.setAttribute("timeSeconds", timeLeft);
					info.put("msg", timeLeft+"秒后可再次获取验证码！");
					logger.info("获取验证码失败---->【"+timeLeft+"秒后可再次获取验证码】！");
				}
			}
		}else{
			info.put("msg", "电脑端认证失败，稍后请重试！");
			Object sn = deviceRec.get("router_sn");
			if(null != sn){
				logger.info("获取验证码失败---->【"+"设备SN未入库或者设备不存在:SN="+sn.toString()+",phone="+phone+"】");
			}else{
				logger.info("获取验证码失败---->【"+"设备SN未入库或者设备不存在:phone="+phone+"】");
			}
		}
		return info;
	}
	
	public static long getPhoneCodeTimeleft(HttpServletRequest request){
		long timeleft = -1;
		if(null != request.getSession().getAttribute(SessionParams.CODE_SEND_DATE)){
			timeleft = (MIN_GET_CODE_TIME-(new Date().getTime() - ((Date)request.getSession().getAttribute(SessionParams.CODE_SEND_DATE)).getTime()))/1000;
		}
		if(timeleft < 0){
			timeleft = -1;
		}
		return timeleft;
	}
	
	public static Object getSendCodePhone(HttpServletRequest request){
		Object phone = request.getSession().getAttribute(SessionParams.CODE_SEND_PHONE);
		if(null != phone){
			return phone;
		}
		return "";
	}
	
	public static JSONObject auth(HttpServletRequest request,String routersn,String clientMac){
		request.setAttribute("phone", request.getParameter("phone"));
		JSONObject info = new JSONObject();
		info.put("success", "0");
		boolean phoneIsAuthed = CommsRoute.isPhoneAuthed(clientMac, request.getParameter("phone"));
		
		Pattern p = Pattern.compile(PATTERN);
		Matcher m = p.matcher(request.getParameter("phone")); 
		if(!m.matches()){
			info.put("msg", "手机号码有误！");
			logger.info("手机认证失败---->手机号码有误【phone="+request.getParameter("phone")+"】！");
		}else{
			String pageCode = request.getParameter("verifyCode");
			Object sessionCode = request.getSession().getAttribute(SessionParams.CODE_SEND);
			if(phoneIsAuthed || 
					(null != sessionCode && sessionCode.equals(pageCode))){
				String phone = (String) request.getSession().getAttribute(SessionParams.CODE_SEND_PHONE);
				if(phoneIsAuthed){
					phone = request.getParameter("phone");
				}
				//如果没有获取到客户端mac会根据手机号码查找客户端信息
				if(StringUtils.isBlank(clientMac)){
					clientMac = setClientInfo(request, phone);
				}
				info.put("success", "1");
				Record keyInfo = new Record().set("routerSn", routersn).set("clientMac", clientMac)
						.set("tag", phone).set("authType", "phone").set("authDate", new Date());
				String key = RouteAuth.setKey(keyInfo);
				info.put("key", key);
				logger.info("手机认证成功---->phone="+phone);
//				info.put("url", PropertyUtils.getProperty("pifii.server.url", "http://www.pifii.com/"));
			}else if(null == sessionCode){
				info.put("msg", "请先获取验证码！");
				logger.info("手机认证失败---->未获取验证码！");
			}else if(!sessionCode.equals(pageCode)){
				info.put("msg", "验证码有误！");
				logger.info("手机认证失败---->验证码有误【sessionCode="+sessionCode+",pageCode="+pageCode+"】！");
			}
		}
		return info;
	}
	
	private static String setClientInfo(HttpServletRequest request, String phone){
		StringBuffer sql = new StringBuffer();
		sql.append("select router_sn,client_mac from bp_auth ");
		sql.append("where auth_type='phone' and tag=? ");
		sql.append("and router_sn is not null and client_mac is not null and LENGTH(router_sn) > 0 and LENGTH(client_mac) > 0 ");
		sql.append("order by auth_date desc ");
		Record clientInfo = Db.findFirst(sql.toString(), new Object[]{phone});
		if(null != clientInfo){
			if(null != clientInfo.get("client_mac")){
				Comms.setSessionClientInfo(request, clientInfo.get("client_mac").toString());
				return clientInfo.get("client_mac").toString();
			}
		}
		return "";
	}
	
	private static String generVerifyCode(){
		StringBuilder code = new StringBuilder();
		for(int i=0;i<VALIDATE_CODE_LENGTH;i++){
			code.append((int) (Math.random() * 10));
		}
		return code.toString();
	}
	
}
