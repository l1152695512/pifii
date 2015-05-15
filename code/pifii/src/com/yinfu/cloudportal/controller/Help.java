package com.yinfu.cloudportal.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.servlet.route.RouteAuth;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
@Before(RouterInterceptor. class)
@ControllerBind(controllerKey = "/portal/mb/help", viewPath = "/portal/mb/help")
public class Help extends Controller<Record>{
	
	public void index(){
		String cmd = getPara(0);
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record client = getSessionAttr(SessionParams.CLIENT_INFO);
		if("work".equals(cmd)){
			String businessUrl = PropertyUtils.getProperty("business.server.url");
			Record keyInfo = new Record().set("routerSn", device.getStr("router_sn")).set("clientMac", client.getStr("client_mac"))
					.set("redirectUrl",businessUrl+"/portal/mb/workorder?routersn="+device.getStr("router_sn")+"&mac="+client.getStr("client_mac"))
					.set("authType", "workOrder").set("tag", client.getStr("client_mac")).set("authDate", new Date()).set("authTimeLength",30);
			String key = RouteAuth.setKey(keyInfo);
			redirect("/authorizeAccess?key="+key, true);
		}else{
			List<Record> functions = new ArrayList<Record>();
			functions.add(new Record().set("src", "/portal/mb/help/images/hm_tsjy.png")
					.set("name","投诉建议").set("url",""));
			if(null != device.get("router_sn") && StringUtils.isNotBlank(device.get("router_sn").toString())){
					//这里不使用session中的shop_id,有时工单绑定的有问题，马上在平台上解绑，但此时用户浏览器没有关闭，这时session中的shop_id是过期的
//					&& (null == device.get("shop_id") || StringUtils.isBlank(device.get("shop_id").toString()))){
				Record thisDevice = Db.findFirst("select shop_id from bp_device where router_sn=?", new Object[]{device.get("router_sn")});
				if(null != thisDevice && 
						(null == thisDevice.get("shop_id") || StringUtils.isBlank(thisDevice.get("shop_id").toString()))){
					
					functions.add(new Record().set("src", "/portal/mb/help/images/hm_ksaz.png")
							.set("name","快速安装").set("url","work?routersn="+device.getStr("router_sn")+"&mac="+client.getStr("client_mac")));
				}
			}
			setAttr("functions", functions);
			render("help.jsp");
		}
	}
	
}
