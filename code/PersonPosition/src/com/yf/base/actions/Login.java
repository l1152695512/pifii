package com.yf.base.actions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.security.providers.encoding.PasswordEncoder;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.SysUser;
import com.yf.base.service.SysUserService;

public class Login extends ActionSupport implements ServletResponseAware,ServletRequestAware{

	private HttpServletResponse response;
	private String error;
	private String msg;
	private HttpServletRequest request;

	
	private SysUserService sysUserService;
	
	private SysUser user = new SysUser();
	
	private PasswordEncoder passwordEncoder;
	

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;	
	}

	@Override
	public String execute() throws Exception {
		try{
//			GlobalVar.setWorkPath("F:\\Tomcat 6.0\\webapps\\jngh\\WEB-INF");
//			GlobalVar.initGlobalVar();
//			
//			Map mappar=new HashMap();
//			mappar.put("ROOT$HEAD$USERID", "111");
//			mappar.put("ROOT$HEAD$PASSWD", "222");
//			mappar.put("ROOT$HEAD$TRANSID", "001");
//			mappar.put("ROOT$HEAD$TRADECODE", "2000");
//			mappar.put("ROOT$BODY",null);
//			mappar.put("ROOT$TAIL$RETCODE", "2");
//			Map callBackMap = null; 
//			Msfactory  factory= new TransControlmp();
//			callBackMap=factory.transControlMap(mappar);
			
			this.response.addHeader("ext-login", "true");//重要，如果登录系统后，用户session失效后点击按钮会跳到Login页面，EXT的ajax功能通过此request head 初始化内部登录框。
            if(this.error!=null&&this.error.equals("1"))this.msg="对不起!用户名或密码错误!";
            if(this.error!=null&&this.error.equals("2"))this.msg="对不起!此用户已登录!";
            
		}catch(Exception e){
			e.printStackTrace();
		}
		return super.execute();
		
	}


	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}


	
}
