package com.yinfu.jbase.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *
 * @author Administrator
 */
public class Send {
	private static Logger logger = Logger.getLogger(Send.class);
	private static String postUrl = "http://sms.chanzor.com:8001/sms.aspx?action=send";
	private static String ACCOUNT = "gzyinfu";
	private static String PWD = "155135";
	
	static{
		postUrl = PropertyUtils.getProperty("sms.postUrl", "http://sms.chanzor.com:8001/sms.aspx?action=send");;
		ACCOUNT = PropertyUtils.getProperty("sms.account", "gzyinfu");
		PWD = PropertyUtils.getProperty("sms.password", "155135");
	}


	/***
	 * 发送激活短信
	 * @param mobile
	 * @param account
	 * @param password
	 * @return
	 */
    public static boolean SMS(String mobile,String account,String password) {
    	boolean flag = false;
    	logger.info("激活短信输入参数mobile:"+mobile+"==account："+account+"==password:"+password);
        try {
        	String businessUrl = PropertyUtils.getProperty("business.server.url");
        	Element root = null;
        	String content = "尊敬的客户：您的商铺已激活，请使用帐号"+account+"，密码"+password+"登录 "+businessUrl+" 管理您的商铺，为保护您的个人隐私，请登录后，立即修改密码。【智能派路由】";
        	String postData = "userid=&account="+ACCOUNT+"&password="+PWD+"&mobile="+mobile+"&sendTime=&content="+java.net.URLEncoder.encode(content,"utf-8");
        	//发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.error("激活短信发送连接失败");
                return flag;
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
        	Document doc = DocumentHelper.parseText(result); 
			root = doc.getRootElement();
			String returnstatus = root.elementText("returnstatus");	
			String message = root.elementText("message");	
			String taskID = root.elementText("taskID");	
			String remainpoint = root.elementText("remainpoint");
//			
			logger.warn("短信手机号："+mobile);
			logger.warn("短信帐号："+account);
			logger.warn("短信返回错误码："+returnstatus);
			logger.warn("短信返回信息："+message);
			logger.warn("短信剩余条数："+remainpoint);
			logger.warn("短信返回id:"+taskID);
			
			if("Success".equals(returnstatus)){
				flag = true;
			}
        } catch (Exception e) {
        	logger.error("激活短信发送错误:"+e);
        }
        return flag;
    }
    
    
    /***
     * 发送认证短信
     * @param mobile
     * @param verifyCode
     * @return
     */
    public static boolean verifyCode(String mobile,String verifyCode) {
    	boolean flag = false;
    	logger.warn("认证短信输入参数mobile:"+mobile+"==verifyCode："+verifyCode);
        try {
        	Element root = null;
        	String content = "您好，您的验证码是："+verifyCode+"，请不要把验证码泄露给其他人。【智能派路由】";
        	String postData = "userid=&account="+ACCOUNT+"&password="+PWD+"&mobile="+mobile+"&sendTime=&content="+java.net.URLEncoder.encode(content,"utf-8");
        	//发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.error("认证短信发送连接失败");
                return flag;
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
        	Document doc = DocumentHelper.parseText(result); 
			root = doc.getRootElement();
			String returnstatus = root.elementText("returnstatus");	
			String message = root.elementText("message");	
			String taskID = root.elementText("taskID");	
			String remainpoint = root.elementText("remainpoint");
//			
			logger.warn("短信手机号："+mobile);
			logger.warn("短信认证码："+verifyCode);
			logger.warn("短信返回错误码："+returnstatus);
			logger.warn("短信返回信息："+message);
			logger.warn("短信剩余条数："+remainpoint);
			logger.warn("短信返回id:"+taskID);
			
			if("Success".equals(returnstatus)){
				flag = true;
			}
        } catch (Exception e) {
        	logger.error("认证短信发送错误:"+e);
        }
        return flag;
    }
}
