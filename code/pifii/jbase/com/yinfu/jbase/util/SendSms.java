package com.yinfu.jbase.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SendSms {
	private static Logger logger = Logger.getLogger(SendSms.class);
	
	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	private static int smsType = 1;
	
	static{
		smsType = PropertyUtils.getPropertyToInt("sms.smsType", 1);
	}
	
	
	public static Element send(String phone,String verifyCode) {
		Element root = null;
		
		if(smsType == 1){
			HttpClient client = new HttpClient(); 
			PostMethod method = new PostMethod(Url); 
				
			client.getParams().setContentCharset("UTF-8");
			method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		    String content = new String("您好，您的验证码是：【"+verifyCode+"】，请不要把验证码泄露给其他人。"); 

			NameValuePair[] data = {//提交短信
				    new NameValuePair("account", "cf_gzyf"), 
				    new NameValuePair("password", "2ZGH2v"), //密码可以使用明文密码或使用32位MD5加密
				    new NameValuePair("mobile", phone), 
				    new NameValuePair("content", content),
			};
			
			method.setRequestBody(data);		
			
			try {
				client.executeMethod(method);	
				
				String SubmitResult =method.getResponseBodyAsString();
						
				Document doc = DocumentHelper.parseText(SubmitResult); 
				root = doc.getRootElement();
				String code = root.elementText("code");	
				String msg = root.elementText("msg");	
				String smsid = root.elementText("smsid");	
//				
				logger.warn("短信手机号："+phone);
				logger.warn("短信验证码："+verifyCode);
				logger.warn("短信返回错误码："+code);
				logger.warn("短信返回信息："+msg);
				logger.warn("短信返回id:"+smsid);
			} catch (Exception e) {
				logger.error("短信发送错误:"+e);
				root = DocumentHelper.createElement("returnsms");
				root.addElement("code").addText("0");
				root.addElement("msg").addText("发送失败");
			}	
		}else{
			root = DocumentHelper.createElement("returnsms");
			if(Send.verifyCode(phone, verifyCode)){
				root.addElement("code").addText("2");
				root.addElement("msg").addText("发送成功");
			}else{
				root.addElement("code").addText("0");
				root.addElement("msg").addText("发送失败");
			}
		}
		return root;
		
	}
	
}