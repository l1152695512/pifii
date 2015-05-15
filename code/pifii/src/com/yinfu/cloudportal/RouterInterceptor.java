package com.yinfu.cloudportal;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.utils.Comms;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.util.PropertyUtils;

/**
 * 拦截器所做的事：
 * 1.放入访问用户的信息，包含用户信息（mac地址）、连接的盒子信息（routersn），
 * 		放入客户信息有安全过滤：只有规定的url传入的routersn和mac才能被服务器作为用户的信息放入session和cookie，
 * 						其他的url识别客户信息是通过session中放入的SessionParams.ACCESS_DEVICE和SessionParams.CLIENT_INFO；
 * 		放入客户信息顺序：规定的url放入顺序为：url后有routersn和mac，则使用该参数，如果没有则使用session中的信息，
 * 									如果session中的信息为空或者没有经过认证（SessionParams.ACCESS_DEVICE中的数据的id为空，即没有根据routersn在数据库查出对应的盒子），
 * 									则使用cookie中的routersn和mac
 * 					其他url放入的顺序为：先查找session中是否包含用户信息，不包含则使用url后的router和mac查找客户信息，如果url后没有携带routersn和mac则使用cookie中的routersn和mac
 * 
 * 2.请求应用的过滤：如果盒子没有绑定商铺，是不允许访问指定应用的
 * 
 * 3.portal页面、应用、广告的点击量统计
 * 
 * @author l
 *
 */
public class RouterInterceptor implements Interceptor{
	private static Logger logger = Logger.getLogger(RouterInterceptor.class);
	private static final int COOKIE_LENGTH = 60*60*24*30;//cookie的有效时长(30天)
	
	@Override
	public void intercept(ActionInvocation ai) {
		boolean trust= isTrustUrl(ai);
		checkRouterInfo(trust,ai);
		checkClientInfo(trust,ai);
		Controller controller = ai.getController();
		Record device = controller.getSessionAttr(SessionParams.ACCESS_DEVICE);
		if(StringUtils.isNotBlank(device.getStr("router_sn")) && 
				(null == device.get("shop_id") || StringUtils.isBlank(device.getStr("shop_id"))) &&
				checkAppAccess(ai)){
			controller.setAttr("errorMsg", "检测到该盒子未绑定商铺，请先绑定商铺！");
			controller.render("/portal/mb/error/alert.jsp");
		}else{
			addAccessData(ai.getActionKey(),controller);
			ai.invoke();
		}
	}
	/**
	 * 设置session中用户连接的盒子的信息
	 */
	private void checkRouterInfo(boolean canSetSessionAndCookie,ActionInvocation ai){
		Controller c = ai.getController();
		String routersn = "";
		if(canSetSessionAndCookie){//url参数---->session------->cookie
			routersn = c.getPara("routersn");
			if(StringUtils.isBlank(routersn)){
				Record device = c.getSessionAttr(SessionParams.ACCESS_DEVICE);
				if(null == device || null == device.get("id") || 
						null == device.get("router_sn") || StringUtils.isBlank(device.get("router_sn").toString())){
					routersn = c.getCookie(SessionParams.COOKIE_ROUTER_SN);
					Object shopId = null;
					if(null != device){
						shopId = device.get("shop_id");
					}
					if(StringUtils.isBlank(routersn) && (null == shopId || StringUtils.isBlank(shopId.toString()))){//处理了商户平台中的portal预览
						logger.warn("未获取到客户端的routersn！");
					}
					Record thisDevice = Comms.getDeviceInfo(routersn);
					if(null != shopId && StringUtils.isNotBlank(shopId.toString())){//处理了商户平台中的portal预览
						thisDevice.set("shop_id", shopId);
					}
					c.setSessionAttr(SessionParams.ACCESS_DEVICE, thisDevice);
				}
			}else{
				Record thisDeviceInfo = Comms.getDeviceInfo(routersn);
				c.setSessionAttr(SessionParams.ACCESS_DEVICE, thisDeviceInfo);
				String cookieRoutersn = c.getCookie(SessionParams.COOKIE_ROUTER_SN);
				if(!routersn.equals(cookieRoutersn)){
					Cookie snCookie = new Cookie(SessionParams.COOKIE_ROUTER_SN,routersn);
					snCookie.setMaxAge(COOKIE_LENGTH);
					c.getResponse().addCookie(snCookie);
				}
			}
		}else{//session---->url参数----->cookie
			Record device = c.getSessionAttr(SessionParams.ACCESS_DEVICE);
			if(null == device || null == device.get("id") || 
					null == device.get("router_sn") || StringUtils.isBlank(device.get("router_sn").toString())){
				routersn = c.getPara("routersn");
				if(StringUtils.isBlank(routersn)){
					routersn = c.getCookie(SessionParams.COOKIE_ROUTER_SN);
				}
				Object shopId = null;
				if(null != device){
					shopId = device.get("shop_id");
				}
				if(StringUtils.isBlank(routersn) && (null == shopId || StringUtils.isBlank(shopId.toString()))){//处理了商户平台中的portal预览
					logger.warn("未获取到客户端的routersn！");
				}
				Record thisDevice = Comms.getDeviceInfo(routersn);
				if(null != shopId && StringUtils.isNotBlank(shopId.toString())){//处理了商户平台中的portal预览
					thisDevice.set("shop_id", shopId);
				}
				c.setSessionAttr(SessionParams.ACCESS_DEVICE, thisDevice);
			}
		}
	}
	/**
	 * 设置session中用户的信息（终端mac）
	 */
	private void checkClientInfo(boolean canSetSessionAndCookie,ActionInvocation ai){
		Controller c = ai.getController();
		String mac = "";
		if(canSetSessionAndCookie){
			mac = c.getPara("mac");
			if(StringUtils.isBlank(mac)){
				Record client = c.getSessionAttr(SessionParams.CLIENT_INFO);
				if(null==client || null == client.get("client_mac") || 
						StringUtils.isBlank(client.get("client_mac").toString())){
					mac = c.getCookie(SessionParams.COOKIE_CLIENT_MAC);
					if(StringUtils.isBlank(mac)){
						logger.warn("未获取到客户端的routersn！");
					}
					Record thisClient = Comms.getClientInfo(mac);
					c.setSessionAttr(SessionParams.CLIENT_INFO, thisClient);
				}
			}else{
				Record thisClient = Comms.getClientInfo(mac);
				c.setSessionAttr(SessionParams.CLIENT_INFO, thisClient);
				String cookieMac = c.getCookie(SessionParams.COOKIE_CLIENT_MAC);
				if(!mac.equals(cookieMac)){
					Cookie macCookie = new Cookie(SessionParams.COOKIE_CLIENT_MAC,mac);
					macCookie.setMaxAge(COOKIE_LENGTH);
					c.getResponse().addCookie(macCookie);
				}
			}
		}else{
			Record client = c.getSessionAttr(SessionParams.CLIENT_INFO);
			if(null==client || null == client.get("client_mac") || 
					StringUtils.isBlank(client.get("client_mac").toString())){
				mac = c.getPara("mac");
				if(StringUtils.isBlank(mac)){
					mac = c.getCookie(SessionParams.COOKIE_CLIENT_MAC);
				}
				if(StringUtils.isBlank(mac)){
					logger.warn("未获取到客户端的routersn！");
				}
				Record thisClient = Comms.getClientInfo(mac);
				c.setSessionAttr(SessionParams.CLIENT_INFO, thisClient);
			}
		}
	}
	
	/**
	 * 只有可信任的请求才会根据该请求后的routersn和mac去设置session中的数据并设置cookie，（session中的数据很重要，所有和盒子有关的请求如果有需要使用routersn和mac，都是从session中取的）
	 * 目前可信任的url：
	 * 		/portal：这个url是吸顶拦截用户上网后的跳转页面，所以参数中的routersn和mac是可信任的
	 * 可信任的其他请求：
	 * 		盒子发出的请求：因为盒子是有本地portal页面的，上网的请求拦截会跳到本地的portal页面，所以不会包含上面的可信任的url请求，
	 * 					盒子中的第一个服务器端的请求可能会很多：上网、帮助中心以及其他的云端应用，对于这些请求，都应该能够设置session中的数据和cookie中的数据，
	 * 					不然就会出现：1.工单流程中绑定盒子时显示的不是当前盒子的sn；2.云端应用使用的sn和实际连接盒子的sn不一致
	 * 
	 * @param actionKey
	 * @return
	 */
	private boolean isTrustUrl(ActionInvocation ai){
		String actionKey = ai.getActionKey();
		if(actionKey.equals("/portal")){
			return true;
		}
		Controller c = ai.getController();
		String routersn = c.getPara("routersn");
		if(StringUtils.isNotBlank(routersn)){
			Record device = Db.findFirst("select type from bp_device where router_sn=? ", new Object[]{routersn});
			if(null == device || null == device.get("type") || !"2".equals(device.get("type").toString())){//如果不是吸顶则定该请求为可设置session和cookie的请求
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 未绑定商铺的盒子不能访问应用，包括上网
	 * @param actionKey
	 * @return
	 */
 	private boolean checkAppAccess(ActionInvocation ai){
 		String actionKey = ai.getActionKey();
		if(actionKey.startsWith("/portal/mb/app") || 
				actionKey.startsWith("/portal/mb/audio") || 
				actionKey.startsWith("/portal/mb/book") || 
				actionKey.startsWith("/portal/mb/games") || 
				actionKey.startsWith("/portal/mb/video") || 
				actionKey.startsWith("/portal/mb/introduce") || 
				actionKey.startsWith("/portal/mb/flowpack") || 
				actionKey.startsWith("/portal/mb/funny") || 
				actionKey.startsWith("/portal/mb/preferential") || 
				actionKey.startsWith("/portal/mb/tide") || 
				actionKey.startsWith("/portal/mb/queue") || 
				actionKey.startsWith("/portal/mb/restaurant") || 
				actionKey.startsWith("/portal/mb/survey") || 
				actionKey.startsWith("/portal/mb/feedback") || 
				actionKey.startsWith("/portal/mb/auth") || 
				actionKey.startsWith("/portal/mb/microshop")){
			return true;
		}else if(actionKey.startsWith("/portal/mb/temp2") || 
				actionKey.startsWith("/portal/mb/temp3")){//模板2和模板3的认证在模板页，所以要对这两个模板的认证和获取验证码做拦截
			String cmd = ai.getController().getPara("cmd");
			if(StringUtils.isNotBlank(cmd)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 数据点击量及访问量
	 * 
	 * 1.portal页面的访问
	 * 2.app的点击
	 * 3.广告的点击
	 * @param actionKey
	 * @param c
	 */
	private void addAccessData(String actionKey,Controller c){
		String routersn = ((Record)c.getSessionAttr(SessionParams.ACCESS_DEVICE)).getStr("router_sn");
		String mac = ((Record)c.getSessionAttr(SessionParams.CLIENT_INFO)).getStr("client_mac");
		if(actionKey.equals("/portal/mb") && "index".equals(c.getPara(0))){//访问主页,下面的方式不可靠，有时候手机没有认证会出现一些app应用访问网络，盒子会拦截后跳转到/portal，这时会产生大量的访问主页的记录
//		if(actionKey.equals("/portal")){//访问主页，不再使用之前的/portal/mb/index，因为portal页面的访问经常是用户返回时的操作，所以这种情况不应该统计为主页的访问
			CommsRoute.addAccessData(routersn,mac,"-1","");
		}else if((actionKey.startsWith("/portal/mb") && "app".equals(c.getPara(0))) //云端portal应用的访问，地址类似于：http://223.82.251.50:8090/jxsqt/portal/mb/microshop/app-22
				|| (StringUtils.isNotBlank(c.getPara("rid")) && c.getPara("rid").startsWith("app_"))){//本地portal页面应用的访问，地址类似于：http://223.82.251.50:8090/jxsqt/portal/mb/microshop/?rid=app_22&....
			String appId = null == c.getPara(1)?"":c.getPara(1);
			if("app".equals(c.getPara(0))){
				appId = c.getPara(1);
			}else if(c.getPara("rid").startsWith("app_")){
				appId = c.getPara("rid").substring(4);
			}
			if(StringUtils.isNotBlank(appId)){
				Record device = c.getSessionAttr(SessionParams.ACCESS_DEVICE);
				c.setAttr("name", CommsRoute.getAppCustomName(appId, device.get("shop_id")));
				CommsRoute.addAccessData(routersn,mac,appId,"app");
			}
		}else if(actionKey.startsWith("/portal/mb/adv")){
			String advId = c.getPara("id");
			CommsRoute.addAccessData(routersn, mac, advId, PropertyUtils.getProperty("route.upload.type.adv"));
		}
	}
	
}
