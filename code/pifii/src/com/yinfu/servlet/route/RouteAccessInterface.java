package com.yinfu.servlet.route;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.servlet.route.fun.Adv;
import com.yinfu.servlet.route.fun.App;
import com.yinfu.servlet.route.fun.CloudPage;
import com.yinfu.servlet.route.fun.Commons;
import com.yinfu.servlet.route.fun.Feedback;
import com.yinfu.servlet.route.fun.Food;
import com.yinfu.servlet.route.fun.PCAccess;
import com.yinfu.servlet.route.fun.PCAuth;
import com.yinfu.servlet.route.fun.PCAuthInfo;
import com.yinfu.servlet.route.fun.Queue;
import com.yinfu.servlet.route.fun.Survey;


public class RouteAccessInterface extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(RouteAccessInterface.class);
	public static final String AUTH_PAGE = "/page/auth/auth.jsp";
//	public static final String ERROR_PAGE = "http://www.pifii.com/";//未找到页面时，跳转到的页面
	
	private String cmd;
	private String deviceUniqueCode;
	private String clientMac;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Enumeration<?> en = request.getParameterNames();
//		while(en.hasMoreElements()){
//			String par = en.nextElement().toString();
//		}
		
		if(!initClientData(request)){
			response.sendRedirect(PropertyUtils.getProperty("router.auth.errorPage"));
			return;
		}
		if(cmd.startsWith("pcAuth")){
			response.sendRedirect("portal/pc/auth?mac="+clientMac+"&routersn="+deviceUniqueCode);
			//该请求是放在认证上网的平台
//			PCAuth pcAuth = new PCAuth(deviceUniqueCode, clientMac);
//			if(cmd.indexOf("code") != -1){
//				returnJson(request, response,pcAuth.getCode(request));
//			}else{
//				pcAuth.index(request, response);
//			}
		}else if(cmd.startsWith("auth")){
			if(cmd.indexOf("validateCode") != -1){//请求验证码，向session和request中放入值
				request.setAttribute("addAuthAppLog", false);
				PhoneValidateCode phoneValidateCode = new PhoneValidateCode();
				phoneValidateCode.execute(request, response);
			}
			if(StringUtils.isNotBlank(deviceUniqueCode) && null == request.getAttribute("hasAuthed")){//该值用于决定页面上的手机和验证码输入框是否显示
				String authPhone = new RouteComms().isClientAuthed(deviceUniqueCode,clientMac);
				request.setAttribute("hasAuthed", StringUtils.isNotBlank(authPhone));
			}
			if(null != request.getSession().getAttribute("sendVerifyCodeDate")){//该值用于提示用户在多长时间后可再次获取验证码
				long intervalTimes = new Date().getTime() - ((Date)request.getSession().getAttribute("sendVerifyCodeDate")).getTime();
				if(intervalTimes < PhoneValidateCode.MIN_GET_CODE_TIME){
					request.setAttribute("waitSendVerifyCodeTime", 60-intervalTimes/1000);
				}
			}
			request.setAttribute("mac", clientMac);
			request.setAttribute("routersn", deviceUniqueCode);
			request.getRequestDispatcher(AUTH_PAGE).forward(request,response);
			if(null == request.getAttribute("addAuthAppLog")){
				String rid = request.getParameter("rid");
				if(null != rid){
					int index = rid.lastIndexOf("_");
					if(index != -1){
						try{
							Commons.addAccessData(deviceUniqueCode,clientMac,rid.substring(index+1),rid.substring(0, index));
						}catch(Exception e){
						}
					}
				}
			}
		}else if(cmd.startsWith("adv")){
			new Adv(deviceUniqueCode,clientMac).execute(request, response);
		}else if(cmd.startsWith("app")){
			new App(deviceUniqueCode,clientMac).execute(request, response);
		}else if(cmd.startsWith("queue")){
			Queue queue = new Queue(deviceUniqueCode,clientMac);
			String jsonStr = "";
			if(cmd.indexOf("get") != -1){
				jsonStr = queue.getNum(request.getParameter("personNum")).toString();
			}else if(cmd.indexOf("cancel") != -1){
				jsonStr = queue.cancelNum().toString();
			}else if(cmd.indexOf("history") != -1){
				jsonStr = queue.historyNum().toString();
			}
			returnJsonp(request, response,jsonStr);
		}else if(cmd.startsWith("pcAuthInfo")){//进入路由电脑端主页时请求，只有客户端能上网时才会请求到该地址，返回客户端登录上网的信息（手机号码，认证时间等等）
			//该请求是放在商户平台的
			PCAuthInfo info = new PCAuthInfo(deviceUniqueCode, clientMac);
			String jsonStr = info.execute(request, response);
			returnJsonp(request, response,jsonStr);
		}else if(cmd.startsWith("pcAccess")){
			//该请求是放在认证上网的平台
			PCAccess access = new PCAccess(deviceUniqueCode, clientMac);
			String jsonStr = "";
			if(cmd.indexOf("phones") != -1){
				jsonStr = access.getPhones();
			}else if(cmd.indexOf("messages") != -1){
				jsonStr = access.getMessages();
			}else if(cmd.indexOf("code") != -1){
				jsonStr = access.getCode(request);
			}else if(cmd.indexOf("auth") != -1){
//				jsonStr = access.auth(request,response);
			}
			returnJsonp(request, response,jsonStr);
		}else if(cmd.startsWith("foodOrder")){
			Food food = new Food(deviceUniqueCode, clientMac);
			if(cmd.indexOf("save") != -1){
				returnJsonp(request, response,food.saveFoodOrder(request).toString());
			}else if(cmd.indexOf("get") != -1){
				returnJsonp(request, response,food.getOrder(request).toString());
			}
		}else if(cmd.startsWith("survey")){//调查问卷
			Survey survey = new Survey(deviceUniqueCode, clientMac);
			if(cmd.indexOf("save") != -1){
				JSONObject json = new JSONObject();
				survey.save(request.getParameter("options"));
				json.put("main_page", getMainPageWidthParams());
				returnJson(request, response,json);
			}else{
				survey.index(request, response);
			}
		}else if(cmd.startsWith("feedback")){//意见反馈
			Feedback feedback = new Feedback(deviceUniqueCode, clientMac);
			if(cmd.indexOf("save") != -1){
				JSONObject json = new JSONObject();
				feedback.save(request.getParameter("opinion"));
				json.put("main_page", getMainPageWidthParams());
				returnJson(request, response,json);
			}else{
				feedback.index(request, response);
			}
		}else if(cmd.startsWith("notFound") || cmd.startsWith("Init")){
			request.setAttribute("main_page", getMainPageWidthParams());
			request.setAttribute("auth_url", getAuthUrl());
			request.getRequestDispatcher("/page/error/app_404.jsp").forward(request,response);
		}else{//加载主页
			new CloudPage().execute(request, response);
		}
	}
	
	private String getAuthUrl(){
		Record rec = Db.findFirst("select link from bp_app where marker='auth_net' ");
		if(null != rec){
			String link = rec.getStr("link");
			if(link.indexOf("?") == -1){
				link += "?";
			}else{
				link += "&";
			}
			link += RouteAuth.REQUEST_MAC+"="+clientMac+"&"+RouteAuth.REQUEST_SN+"="+deviceUniqueCode;
			return link;
		}else{
			return "";
		}
	}
	
	private String getMainPageWidthParams(){
		String indexPage = PropertyUtils.getProperty("router.mainPage", "http://m.pifii.com/ifidc/pifii.html");
		if(indexPage.indexOf("?") == -1){
			indexPage += "?";
		}else{
			indexPage += "&";
		}
		indexPage += RouteAuth.REQUEST_MAC+"="+clientMac+"&"+RouteAuth.REQUEST_SN+"="+deviceUniqueCode;
		return indexPage;
	}
	
	//初始化全局变量并将客户端mac地址及盒子唯一码放到session中
	private boolean initClientData(HttpServletRequest request){
		cmd = request.getParameter("cmd");
		if(StringUtils.isBlank(cmd)){
			cmd = "";
		}
		if(StringUtils.isNotBlank(request.getParameter("deviceId"))){
			deviceUniqueCode = request.getParameter("deviceId");
		}else{
			deviceUniqueCode = request.getParameter("routersn");
		}
		if(StringUtils.isBlank(deviceUniqueCode)){
			deviceUniqueCode = (String)request.getSession().getAttribute("session_deviceUniqueCode");
		}else{
			request.getSession().setAttribute("session_deviceUniqueCode",deviceUniqueCode);
		}
		clientMac = request.getParameter("mac");
		if(StringUtils.isBlank(clientMac)){
			clientMac = (String)request.getSession().getAttribute("session_clientMac");
		}else{
			request.getSession().setAttribute("session_clientMac",clientMac);
		}
		if(StringUtils.isBlank(deviceUniqueCode) || StringUtils.isBlank(clientMac)){
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>未传递路由唯一码或客户端手机的mac");
			return false;
		}
		return true;
	}
	
	private void returnJsonp(HttpServletRequest request, HttpServletResponse response,String jsonStr) throws IOException{
		response.setCharacterEncoding("UTF-8"); 
		response.setContentType("text/plain");  
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        PrintWriter out = response.getWriter();       
        String jsonpCallback = request.getParameter("jsonpCallback");//客户端请求参数  
        out.println(jsonpCallback+"("+jsonStr+")");//返回jsonp格式数据  
        out.flush();  
        out.close(); 
	}
	
	private void returnJson(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws IOException{
		response.setCharacterEncoding("UTF-8"); 
		response.setContentType("application/x-json");  
		response.setHeader("Pragma", "No-cache");  
	    response.setHeader("Cache-Control", "no-cache");  
	    response.setDateHeader("Expires", 0);  
        PrintWriter pw = response.getWriter();  
        pw.print(json.toString());
        pw.flush();
        pw.close();
	}
}
