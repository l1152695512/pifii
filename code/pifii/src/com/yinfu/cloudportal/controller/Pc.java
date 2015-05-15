package com.yinfu.cloudportal.controller;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.interceptor.BlackListMacInterceptor;
import com.yinfu.cloudportal.interceptor.BlackListPhoneInterceptor;
import com.yinfu.cloudportal.utils.CommsPhoneCode;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.servlet.route.RouteAuth;

@Before({RouterInterceptor.class,BlackListMacInterceptor.class,BlackListPhoneInterceptor.class})
@ControllerBind(controllerKey = "/portal/pc", viewPath = "/portal/pc")
public class Pc extends Controller<Record>{
	
	public void auth(){
		String cmd = getPara("cmd");
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record client = getSessionAttr(SessionParams.CLIENT_INFO);
		setAttr("phones", JsonKit.toJson(CommsRoute.authedPhones(client.get("client_mac"))));
		setAttr("timeSeconds", CommsPhoneCode.getPhoneCodeTimeleft(getRequest()));//设置获取验证码的剩余时长
		if("code".equals(cmd)){
			JSONObject json = CommsPhoneCode.getCode(getRequest());
			String success = json.getString("success");
			setAttr("success", success);
			if(!"1".equals(success)){
				setAttr("msg", json.get("msg"));
			}
		}else if("auth".equals(cmd)){
			JSONObject json = CommsPhoneCode.auth(getRequest(), device.getStr("router_sn"), client.getStr("client_mac"));
			String success = json.getString("success");
			if(!"1".equals(success)){//认证失败
				setAttr("msg", json.get("msg"));
			}else{
				String key = json.getString("key");
				Record keyInfo = RouteAuth.getKey(key);
				keyInfo.set("redirectUrl", "portal/pc/auth?msg=success");
				redirect("/authorizeAccess?key="+json.getString("key"));//认证成功
				return;
			}
		}
		if(StringUtils.isNotBlank(getPara("msg"))){
			setAttr("msg", getPara("msg"));
		}
		render("authPC.jsp");
	}
}

