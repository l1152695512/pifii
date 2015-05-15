package com.yinfu.servlet.route;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.yinfu.jbase.util.SendSms;

public class PhoneValidateCode{
//	public static final String PC_AUTH_PAGE = "/WEB-INF/component/showPCFuntion.jsp";
	private static Logger logger = Logger.getLogger(PhoneValidateCode.class);
	public static final String SEND_VALIDATE_CODE_ADDRESS = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	public static final String PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
	public static final String ACCOUNT = "cf_gzyf";
	public static final String PASSWORD = "2ZGH2v";
	
	public static final int VALIDATE_CODE_LENGTH = 4;
	public static final int MIN_GET_CODE_TIME = 60*1000;
	
	protected void execute(HttpServletRequest request, HttpServletResponse response){
//		request.getSession().removeAttribute("routeAuthSuccess");
		request.setAttribute("sendVerifyCode",true);//用于在页面标识本次请求为获取验证码
		request.setAttribute("sendVerifyCodeSuccess", false);//发送验证码是否成功
		String phone = request.getParameter("phone");
		request.getSession().setAttribute("phone",phone);//放入session，方便页面再次加载时，不用再次输入
//		request.setAttribute("phone", phone);
		Pattern p = Pattern.compile(PATTERN);  
		Matcher m = p.matcher(phone); 
		long intervalTimes = MIN_GET_CODE_TIME;
		if(null != request.getSession().getAttribute("sendVerifyCodeDate")){
			intervalTimes = new Date().getTime() - ((Date)request.getSession().getAttribute("sendVerifyCodeDate")).getTime();
		}
		if(m.matches() && intervalTimes >= MIN_GET_CODE_TIME){
			//动态密码页面限制为4位
			String verifyCode = generVerifyCode();
			try {
				Element root = SendSms.send(phone,verifyCode);
				String code = root.elementText("code");	
				String msg = root.elementText("msg");
//				String smsid = root.elementText("smsid");
				if("2".equals(code)){
					request.getSession().setAttribute("verifyCode",verifyCode);//将验证码放入session，这行代码要放在下面判断条件之前，因为有时候短信平台发送验证码失败后（code!=2），可能过一会手机又收到短信了
					request.setAttribute("sendVerifyCodeSuccess", true);
					logger.warn("手机端获取验证码成功---->【phone="+phone+",verifyCode="+verifyCode+"】");
					request.getSession().setAttribute("sendVerifyCodeDate",new Date());
				}else if("0".equals(code)){
					request.setAttribute("sendVerifyCodeMsg", "第三方短信平台发送短信失败，我们正在努力协调中！");
					logger.warn("手机端获取验证码失败---->【phone="+phone+"，msg="+msg+"】");
				}else{
					request.setAttribute("sendVerifyCodeMsg", msg);
					logger.warn("手机端获取验证码失败---->【phone="+phone+"，msg="+msg+"】");
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("sendVerifyCodeMsg", "第三方短信平台异常，我们正在努力协调中！");
				logger.error("手机端获取验证码异常---->【phone="+phone+"，msg="+e.getMessage()+"】",e);
			} 
		}else{
			if(!m.matches()){
				request.setAttribute("sendVerifyCodeMsg", "手机号码有误，请修改！");
				logger.warn("手机端获取验证码失败---->【"+"手机号码有误:"+phone+"】");
			}else if(intervalTimes < MIN_GET_CODE_TIME){
				request.setAttribute("sendVerifyCodeMsg", (PhoneValidateCode.MIN_GET_CODE_TIME-intervalTimes)/1000+"秒后可再次获取验证码！");
				logger.warn("手机端获取验证码失败---->【"+(PhoneValidateCode.MIN_GET_CODE_TIME-intervalTimes)/1000+"秒后可再次获取验证码】！");
			}
		}
	}
	
	public static String generVerifyCode(){
		StringBuilder code = new StringBuilder();
		for(int i=0;i<VALIDATE_CODE_LENGTH;i++){
			code.append((int) (Math.random() * 10));
		}
		return code.toString();
	}
	
}
