package com.yinfu.cloudportal.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.interceptor.PageViewInterceptor;
import com.yinfu.cloudportal.utils.Comms;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
@Before({RouterInterceptor.class,PageViewInterceptor.class})
@ControllerBind(controllerKey = "/portal/mb/nav", viewPath = "/portal/mb")
public class Nav extends Controller<Record>{
	
	public void index(){
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		if(null == device || null == device.get("id")){//处理“/portal/mb/nav/盒子sn”这样的url
			Record thisDevice = Comms.getDeviceInfo(getPara(0));
			if(null != thisDevice.get("id") || null == device){
				setSessionAttr(SessionParams.ACCESS_DEVICE, thisDevice);
				device = thisDevice;
				redirect("/portal/mb/nav?routersn="+getPara(0),true);
				return;
			}
		}
		setAttr("banner_advs", CommsRoute.getBannerAvds(device.get("shop_id")));
		render("navigate.jsp");
	}
	
}
