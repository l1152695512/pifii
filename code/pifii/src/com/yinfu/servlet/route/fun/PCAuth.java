package com.yinfu.servlet.route.fun;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.SendSms;
import com.yinfu.servlet.route.PhoneValidateCode;
import com.yinfu.servlet.route.RouteAuth;
import com.yinfu.servlet.route.RouteComms;

public class PCAuth {
	private static Logger logger = Logger.getLogger(PCAuth.class);
	
	private String routersn;
	private String clientMac;
	
	public PCAuth(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.addHeader("Cache-Control", "no-store, must-revalidate"); 
		response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
		List<Record> phones = Db.find("select distinct tag phone from bp_auth where auth_type='phone' and client_mac=? order by auth_date desc", 
				new Object[]{clientMac});
		request.setAttribute("phones", JsonKit.toJson(phones));
		request.setAttribute("routersn", routersn);
		request.setAttribute("mac", clientMac);
		request.getRequestDispatcher("/page/auth/authPC.jsp").forward(request,response);
	}
	
	public JSONObject getCode(HttpServletRequest request){
		String phone = request.getParameter("phone");
		JSONObject info = new JSONObject();
		info.put("success", "0");
		
		Pattern p = Pattern.compile(PhoneValidateCode.PATTERN);  
		Matcher m = p.matcher(phone); 
		long intervalTimes = PhoneValidateCode.MIN_GET_CODE_TIME;
		if(null != request.getSession().getAttribute("sendVerifyCodeDate")){
			intervalTimes = new Date().getTime() - ((Date)request.getSession().getAttribute("sendVerifyCodeDate")).getTime();
		}
		if(m.matches() && intervalTimes >= PhoneValidateCode.MIN_GET_CODE_TIME){
			//动态密码页面限制为4位
			String verifyCode = PhoneValidateCode.generVerifyCode();
			try {
				Element root = SendSms.send(phone,verifyCode);
				String code = root.elementText("code");	
				String msg = root.elementText("msg");
//				String smsid = root.elementText("smsid");	
				request.getSession().setAttribute("verifyCode",verifyCode);//将验证码放入session，这行代码要放在下面判断条件之前，因为有时候短信平台发送验证码失败后（code!=2），可能过一会手机又收到短信了
				if("2".equals(code)){
					info.put("success", "1");
					logger.warn("PC端获取验证码成功---->【phone="+phone+",verifyCode="+verifyCode+"】");
					request.getSession().setAttribute("sendVerifyCodeDate",new Date());
					request.getSession().setAttribute("sendMessagePhone",phone);
				}else if("0".equals(code)){
					info.put("msg", "第三方短信平台发送短信失败，我们正在努力协调中！");
					logger.warn("PC端获取验证码失败---->【phone="+phone+"，msg="+msg+"】");
				}else{
					info.put("msg", msg);
					logger.warn("PC端获取验证码失败---->【phone="+phone+"，msg="+msg+"】");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("PC端获取验证码异常---->【phone="+phone+"，msg="+e.getMessage()+"】",e);
				info.put("msg", "第三方短信平台异常，我们正在努力协调中！");
			} 
		}else{
			if(!m.matches()){
				info.put("msg", "手机号码有误，请修改！");
				logger.warn("PC端获取验证码失败---->【"+"手机号码有误:"+phone+"】");
			}else if(intervalTimes < PhoneValidateCode.MIN_GET_CODE_TIME){
				info.put("msg", (PhoneValidateCode.MIN_GET_CODE_TIME-intervalTimes)/1000+"秒后可再次获取验证码！");
				logger.warn("PC端获取验证码失败---->【"+(PhoneValidateCode.MIN_GET_CODE_TIME-intervalTimes)/1000+"秒后可再次获取验证码】！");
			}
		}
		return info;
	}
	
	public JSONObject auth(HttpServletRequest request,HttpServletResponse response){
		int authTimeLength = -1;
		response.setHeader("validateCode", authTimeLength+"");
		JSONObject info = new JSONObject();
		info.put("success", "0");
		boolean phoneIsAuthed = RouteComms.isPhoneAuthed(clientMac, request.getParameter("phone"));
		
		Pattern p = Pattern.compile(PhoneValidateCode.PATTERN);  
		Matcher m = p.matcher(request.getParameter("phone")); 
		if(!m.matches()){
			info.put("msg", "手机号码有误！");
			logger.warn("PC认证失败---->手机号码有误【phone="+request.getParameter("phone")+"】！");
		}else{
			String pageCode = request.getParameter("code");
			Object sessionCode = request.getSession().getAttribute("verifyCode");
			if(phoneIsAuthed || 
					(null != sessionCode && sessionCode.equals(pageCode))){
				String phone = (String) request.getSession().getAttribute("sendMessagePhone");
				if(phoneIsAuthed){
					phone = request.getParameter("phone");
				}
				info.put("success", "1");
				info.put("url", PropertyUtils.getProperty("pifii.server.url", "http://www.pifii.com/"));
				authTimeLength = RouteAuth.getOnlineTime(routersn);
				response.setHeader("validateCode", authTimeLength+"");
				RouteAuth.addAuthLog(routersn,clientMac,phone,"phone",authTimeLength);
			}else if(null == sessionCode){
				info.put("msg", "请先获取验证码！");
				logger.warn("PC认证失败---->未获取验证码！");
			}else if(!sessionCode.equals(pageCode)){
				info.put("msg", "验证码有误！");
				logger.warn("PC认证失败---->验证码有误【sessionCode="+sessionCode+",pageCode="+pageCode+"】！");
			}
		}
		logger.warn("返回的认证时长：authTimeLength="+authTimeLength+"秒,clientMac="+clientMac+",routersn="+routersn);
		return info;
	}
	
}
