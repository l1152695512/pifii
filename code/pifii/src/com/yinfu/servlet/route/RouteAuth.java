package com.yinfu.servlet.route;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.cloudportal.Portal;
import com.yinfu.cloudportal.utils.Comms;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.servlet.route.fun.Commons;
import com.yinfu.servlet.route.fun.PCAccess;
import com.yinfu.servlet.route.fun.PCAuth;


public class RouteAuth extends HttpServlet {
	private static Logger logger = Logger.getLogger(RouteAuth.class);
	private static final long serialVersionUID = 1L;
	private static final String authResultPage = "/page/auth/authResult.jsp";
//	private static final String authSuccessImg = "images/auth/authSuccess.jpg";
	private static final String authFailImg = "images/auth/authFail.jpg";
	private static final String authFailCountImg = "images/auth/authFail01.jpg";
	public static final String REQUEST_MAC = "mac";
	public static final String REQUEST_SN = "routersn";
	private static Map<String,Record> authKey = new HashMap<String,Record>();//存储所有认证成功的key
		
	public static Map<String, Record> getAuthKey() {
		return authKey;
	}

	private String clientMac;
	private String routeUniqueCode;
	private int authTimeLength = -1;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	public static String setKey(Record keyInfo){
		String key = UUID.randomUUID().toString().replaceAll("-", "");
		authKey.put(key, keyInfo);
		return key;
	}
	
	public static Record getKey(String key){
		return authKey.get(key);
	}
	
	@Override//写一个定时器定时清除半分钟之前的key
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String key = request.getParameter("key");
			if(null != key){
				Record keyInfo = authKey.get(key);
				if(null != keyInfo){
					if(isAuth(keyInfo.getStr("routerSn"),keyInfo.getStr("clientMac"))){//盒子每天认证的次数校验
						response.setHeader("validateCode", "-1");
						request.setAttribute("img", authFailCountImg);
						request.getRequestDispatcher("portal/mb/auth/result.jsp").forward(request,response);
					}else{
						int authTimeLength = 0;
						if(null != keyInfo.get("authTimeLength")){
							try{
								authTimeLength = keyInfo.getInt("authTimeLength");
							}catch(Exception e){
								authTimeLength = CommsRoute.getOnlineTime(keyInfo.getStr("routerSn"));
							}
						}else{
							authTimeLength = CommsRoute.getOnlineTime(keyInfo.getStr("routerSn"));
						}
						CommsRoute.addAuthLog(keyInfo.getStr("routerSn"),keyInfo.getStr("clientMac"),keyInfo.getStr("tag"),
								keyInfo.getStr("authType"),authTimeLength);
						response.setHeader("validateCode", authTimeLength+"");
						logger.warn("客户端认证信息：MAC="+keyInfo.getStr("clientMac")+",SN="+keyInfo.getStr("routerSn")+",tag="+keyInfo.getStr("tag")+",authTimeLength="+authTimeLength);
						String redirectUrl = keyInfo.getStr("redirectUrl");
						if(StringUtils.isNotBlank(redirectUrl)){
							response.sendRedirect(redirectUrl);
						}else{
							response.sendRedirect(getAuthSuccessUrl(keyInfo.getStr("routerSn"),keyInfo.getStr("clientMac")));
//							request.setAttribute("success", "1");
//							request.setAttribute("img", authSuccessImg);
////							request.setAttribute("main_page", request.getSession().getAttribute(SessionParams.AUTH_SUCCESS_URL));//这里根据组织表中的配置跳转
//							request.setAttribute("main_page", getAuthSuccessUrl(keyInfo.getStr("routerSn"),keyInfo.getStr("clientMac")));
//							request.getRequestDispatcher("portal/mb/auth/result.jsp").forward(request,response);
						}
					}
					authKey.remove(key);
				}else{
					response.setHeader("validateCode", "-1");
					request.setAttribute("success", "0");
					request.setAttribute("img", authFailImg);
					request.getRequestDispatcher("portal/mb/auth/result.jsp").forward(request,response);
				}
			}else{
				clientMac = request.getParameter(REQUEST_MAC);
				routeUniqueCode = request.getParameter(REQUEST_SN);
				logger.warn("请求客户端的信息：clientMac="+clientMac+",routeUniqueCode="+routeUniqueCode);
				if(isAuth(routeUniqueCode,clientMac)){
					request.setAttribute("autoBack", false);
					response.setHeader("validateCode", authTimeLength+"");
					request.setAttribute("result", "over");
					request.getRequestDispatcher(authResultPage).forward(request,response);
				}else{
					if(null != request.getParameter("opid") && null != request.getParameter("phone") 
							&& null != request.getParameter("type") && null != request.getParameter("weixin")){
						if(StringUtils.isBlank(clientMac)){//硬件没有把客户端的mac放到url后(授权成功后再次访问，不再带mac)
							authTimeLength = 0;
						}else{
							authTimeLength = getOnlineTime(routeUniqueCode);
							addAuthLog(routeUniqueCode,clientMac,request.getParameter("weixin"),"weixin", authTimeLength,
									request.getParameter("opid"),request.getParameter("phone"),request.getParameter("type"));
						}
						response.setHeader("validateCode", authTimeLength+"");
						response.sendRedirect(getAuthSuccessUrl(routeUniqueCode,clientMac));
						logger.warn("返回的认证时长：authTimeLength="+authTimeLength+"秒,clientMac="+clientMac+",routeUniqueCode="+routeUniqueCode);
					}else if(StringUtils.isNotBlank(request.getParameter("weixin"))){//微信认证
						authByWeixin(request.getParameter("weixin"));
//						gotoResultPage(request, response,true);
						response.setHeader("validateCode", authTimeLength+"");
//						request.setAttribute("success", "1");
//						request.setAttribute("img", authSuccessImg);
////						request.setAttribute("main_page", "portal/mb/nav?"+REQUEST_MAC+"="+clientMac+"&"+REQUEST_SN+"="+routeUniqueCode);
//						request.setAttribute("main_page", getAuthSuccessUrl(routeUniqueCode,clientMac));
//						request.getRequestDispatcher("portal/mb/auth/result.jsp").forward(request,response);
						response.sendRedirect(getAuthSuccessUrl(routeUniqueCode,clientMac));
						logger.warn("返回的认证时长：authTimeLength="+authTimeLength+"秒,clientMac="+clientMac+",routeUniqueCode="+routeUniqueCode);
					}else if(null != request.getParameter("phone")){//手机认证
						if(null != request.getParameter("cmd") && request.getParameter("cmd").indexOf("pcAuth")!=-1){
							PCAuth pcAuth = new PCAuth(routeUniqueCode, clientMac);
							returnJson(request, response,pcAuth.auth(request,response));
						}else if(null != request.getParameter("cmd") && request.getParameter("cmd").indexOf("pcAccess")!=-1){
							PCAccess access = new PCAccess(routeUniqueCode, clientMac);
							returnJsonp(request, response,access.auth(request,response));
						}else{
							initParams(request);
							if(!authByPhone(request)){//授权失败
								if(null != request.getSession().getAttribute("sendVerifyCodeDate")){//该值用于提示用户在多长时间后可再次获取验证码
									long intervalTimes = new Date().getTime() - ((Date)request.getSession().getAttribute("sendVerifyCodeDate")).getTime();
									if(intervalTimes < PhoneValidateCode.MIN_GET_CODE_TIME){
										request.setAttribute("waitSendVerifyCodeTime", (PhoneValidateCode.MIN_GET_CODE_TIME-intervalTimes)/1000);
									}
								}
								response.setHeader("validateCode", -1+"");
								request.setAttribute("addAuthAppLog", false);
								request.getRequestDispatcher(RouteAccessInterface.AUTH_PAGE).forward(request,response);
							}else{
								gotoResultPage(request, response,true);
							}
							logger.warn("返回的认证时长：authTimeLength="+authTimeLength+"秒,clientMac="+clientMac+",routeUniqueCode="+routeUniqueCode);
						}
					}else if(null != request.getParameter("noAuth")){
						authTimeLength = getOnlineTime(routeUniqueCode);
						String rid = request.getParameter("rid");
						if(null != rid){
							int index = rid.lastIndexOf("_");
							if(index != -1){
								try{
									Commons.addAccessData(routeUniqueCode,clientMac,rid.substring(index+1),rid.substring(0, index));
								}catch(Exception e){
								}
							}
						}
						addAuthLog(routeUniqueCode,clientMac,"noAuth","noAuth",authTimeLength);
						gotoResultPage(request, response,true);
						logger.warn("返回的认证时长：authTimeLength="+authTimeLength+"秒,clientMac="+clientMac+",routeUniqueCode="+routeUniqueCode);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			gotoResultPage(request, response,false);
			logger.warn("返回的认证时长：authTimeLength="+authTimeLength+"秒,clientMac="+clientMac+",routeUniqueCode="+routeUniqueCode);
		}
	}
	/**
	 * 获取组织架构中配置的认证成功的url
	 * @param routersn
	 * @param clientMac
	 * @return
	 */
	private String getAuthSuccessUrl(String routersn,String clientMac){
		String defaultUrl = Portal.serverpath+"/portal/mb/nav";
		Record rec = Db.findFirst("select shop_id from bp_device where router_sn=? ", new Object[]{routersn});
		if(null != rec && null != rec.get("shop_id") && StringUtils.isNotBlank(rec.get("shop_id").toString())){
			String authSuccessUrl = DataOrgUtil.getOrgUrl(rec.get("shop_id"),"auth_success_url");
			if(StringUtils.isNotBlank(authSuccessUrl)){
				if("template".equals(authSuccessUrl)){
					defaultUrl = Comms.getTemplateUrl(rec.get("shop_id"),routersn);
					if(StringUtils.isNotBlank(defaultUrl)){
						defaultUrl = Portal.serverpath+"/"+defaultUrl;
					}
				}else{
					if(authSuccessUrl.startsWith("http://")){
						defaultUrl = authSuccessUrl;
					}else{
						defaultUrl = Portal.serverpath+"/"+authSuccessUrl;
					}
				}
			}
		}
		if(defaultUrl.indexOf("?") > 0){
			defaultUrl += "&";
		}else{
			defaultUrl += "?";
		}
		return defaultUrl+"routersn="+routersn+"&mac="+clientMac;
	}
	
	//跳转到授权结果页面
	private void gotoResultPage(HttpServletRequest request, HttpServletResponse response,boolean autoBack) throws ServletException, IOException{
		request.setAttribute("autoBack", "true");
//		if(autoBack){
//			String indexPage = PropertyUtils.getProperty("router.mainPage", "http://m.pifii.com/ifidc/pifii.html");
//			if(indexPage.indexOf("?") == -1){
//				indexPage += "?";
//			}else{
//				indexPage += "&";
//			}
//			indexPage += REQUEST_MAC+"="+clientMac+"&"+REQUEST_SN+"="+routeUniqueCode;
//			request.setAttribute("main_page", indexPage);
//		}
//		request.setAttribute("main_page", "portal/mb/nav?"+REQUEST_MAC+"="+clientMac+"&"+REQUEST_SN+"="+routeUniqueCode);
		request.setAttribute("main_page", getAuthSuccessUrl(routeUniqueCode,clientMac));
		response.setHeader("validateCode", authTimeLength+"");
		if(authTimeLength != -1){
//			request.setAttribute("result", true);
			response.sendRedirect(getAuthSuccessUrl(routeUniqueCode,clientMac));
		}else{
			request.setAttribute("result", false);
			request.getRequestDispatcher(authResultPage).forward(request,response);
		}
	}
	
	private void initParams(HttpServletRequest request){
		if(StringUtils.isBlank(clientMac)){
			clientMac = request.getSession().getAttribute("session_clientMac").toString();
		}
		if(StringUtils.isBlank(routeUniqueCode)){
			routeUniqueCode = request.getSession().getAttribute("session_deviceUniqueCode").toString();
		}
	}
	
	private boolean authByPhone(HttpServletRequest request){
		String authedPhone = checkClientAuthed(request);
		String verifyCode = request.getParameter("verifyCode");
		if(StringUtils.isNotBlank(authedPhone) && StringUtils.isBlank(verifyCode)){
			request.setAttribute("routeAuthSuccess", true);
			authTimeLength = getOnlineTime(routeUniqueCode);
			addAuthLog(routeUniqueCode,clientMac,authedPhone,"phone",authTimeLength);
//			response.setHeader("validateCode", getOnlineTime());
//			syncWhiteList();
			logger.warn("手机端认证成功【phone="+authedPhone+",非首次认证】。");
			return true;
		}else{
			String phone = request.getParameter("phone");
			request.setAttribute("phone", phone);
			
			Pattern p = Pattern.compile(PhoneValidateCode.PATTERN);  
			Matcher m = p.matcher(phone); 
			if(m.matches()){
				Object sendVerifyCode = request.getSession().getAttribute("verifyCode");//发送验证码时会把验证码放到session中
				if(verifyCode.equals(sendVerifyCode)){
					request.getSession().removeAttribute("verifyCode");
					request.setAttribute("routeAuthSuccess", true);
//					request.setAttribute("redirectUrl", AdvServlet.ERROR_PAGE);
					request.setAttribute("hasAuthed", true);
					authTimeLength = getOnlineTime(routeUniqueCode);
					addAuthLog(routeUniqueCode,clientMac,phone,"phone",authTimeLength);
//					response.setHeader("validateCode", getOnlineTime());
//					syncWhiteList();
					return true;
				}else{
					request.setAttribute("routeAuthSuccess", false);
					request.setAttribute("routeAuthError", "verifyCode");
					if(null == sendVerifyCode){
						request.setAttribute("routeAuthMsg", "请获取验证码！");
						logger.warn("手机端认证失败【未获取验证码】");
					}else if(!verifyCode.equals(sendVerifyCode)){
						request.setAttribute("routeAuthMsg", "验证码错误！");
						logger.warn("手机端认证失败【pageCode="+verifyCode+",sessionCode="+sendVerifyCode+"】");
					}
				}
			}else{
				request.setAttribute("routeAuthSuccess", false);
				request.setAttribute("routeAuthError", "phone");
				request.setAttribute("routeAuthMsg", "手机号码填写有误！");
				logger.warn("手机端认证失败【手机号码有误："+phone+"】");
			}
		}
		return false;
	}
	
	private void authByWeixin(String weixin) throws IOException{
		if(StringUtils.isBlank(clientMac)){//硬件没有把客户端的mac放到url后(授权成功后再次访问，不再带mac)
			authTimeLength = 0;
		}else{
			authTimeLength = getOnlineTime(routeUniqueCode);
			addAuthLog(routeUniqueCode,clientMac,weixin,"weixin",authTimeLength);
		}
	}
	
	//前台授权页面会按照该字段设置手机和验证码是否可见（授权成功后需要将session中的该值设置为true）
	private String checkClientAuthed(HttpServletRequest request){
		boolean hasAuthed = false;
		String authedPhone = new RouteComms().isClientAuthed(routeUniqueCode,clientMac);
		if(StringUtils.isNotBlank(authedPhone)){
			request.getSession().setAttribute("phone",authedPhone);
			hasAuthed = true;
		}
		request.setAttribute("hasAuthed", hasAuthed);
//		request.getSession().setAttribute("hasAuthed",hasAuthed);
		return authedPhone;
	}
	
	public static int getOnlineTime(String sn){
		int timeOut = PropertyUtils.getPropertyToInt("router.auth.onlineTime");
//		try{
//			StringBuffer sql = new StringBuffer();
//			sql.append("select sot.time_out ");
//			sql.append("from bp_online_time sot join bp_auth ba on ");
//			sql.append("(ba.route_code=? and (ba.client_mac = sot.client_mac or ba.tag = sot.phone)) ");
//			Record record = Db.findFirst(sql.toString(), new Object[]{routeUniqueCode});
//			if(null != record){
//				timeOut = record.getInt("time_out")*60;
//				return timeOut;
//			}
//		}catch(Exception e){
//		}
		
		try{
			Record record = Db.findFirst("select time_out from bp_device where router_sn=? ", new Object[]{sn});
			if(record != null){
				timeOut = record.getInt("time_out")*60;
				return timeOut;
			}
		}catch(Exception e){
		}
		return timeOut;
	}
	
	public static void addAuthLog(String sn,String mac,String tag,String authType,int timeLength){
		addAuthLog(sn,mac,tag,authType,timeLength,"","","");
	}
	
	private static void addAuthLog(String sn,String mac,String tag,String authType,int timeLength,
			String weixinOpid,String weixinPhone,String weixintype){
		try {
			if(StringUtils.isNotBlank(sn) || StringUtils.isNotBlank(mac)){
				if(StringUtils.isBlank(weixinOpid) && StringUtils.isBlank(weixinPhone) 
						&& StringUtils.isBlank(weixintype)){//新的微信认证
					Db.update("insert into bp_auth(tag,router_sn,auth_type,client_mac,auth_date) values(?,?,?,?,now())", 
							new Object[]{tag,sn,authType,mac});
				}else{
					Db.update("insert into bp_auth(tag,router_sn,auth_type,client_mac,auth_date,weixin_opid,weixin_phone,weixin_type) values(?,?,?,?,now(),?,?,?)", 
							new Object[]{tag,sn,authType,mac,weixinOpid,weixinPhone,weixintype});
				}
				CommsRoute.groupPassList(sn,mac,timeLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入验证日志失败:", e);
		}
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
	
	/***
	 * 判断当天是否认证过
	 * @param sn
	 * @param mac
	 */
	private static boolean isAuth(String sn,String mac){
		boolean bool = false;
		int num = PropertyUtils.getPropertyToInt("auth.num", 99);
		String sql = "select count(1) num from bp_auth where router_sn=? and client_mac= ? and to_days(auth_date) = to_days(now())";
		Record rd = Db.findFirst(sql, new Object[]{sn,mac});
		if(rd != null){
			int hasNum = Integer.parseInt(rd.get("num").toString());
			if(hasNum >= num){
				logger.warn("当天已经认证:"+hasNum+"次,最大认证数:"+num);
				bool = true;
			}
		}
		
		return bool;
	}


}
