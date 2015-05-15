package com.yinfu.cloudportal.controller.template;

import net.sf.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.interceptor.BlackListMacInterceptor;
import com.yinfu.cloudportal.interceptor.BlackListPhoneInterceptor;
import com.yinfu.cloudportal.interceptor.PageViewInterceptor;
import com.yinfu.cloudportal.utils.CommsPhoneCode;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
@Before({RouterInterceptor.class,PageViewInterceptor.class,BlackListMacInterceptor.class,BlackListPhoneInterceptor.class})
@ControllerBind(controllerKey = "/portal/mb/temp2", viewPath = "/portal")
public class IndexT2 extends Controller<Record>{
	
	public void index(){
		Record client = getSessionAttr(SessionParams.CLIENT_INFO);
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		setAttr("shopInfo", CommsRoute.getPageShopInfo(device.get("shop_id")));
		setAttr("phones", JsonKit.toJson(CommsRoute.authedPhones(client.get("client_mac"))));
		setAttr("timeSeconds", CommsPhoneCode.getPhoneCodeTimeleft(getRequest()));//设置获取验证码的剩余时长
		String cmd = getPara("cmd");
		if("code".equals(cmd)){//发送验证码
//			request.setAttribute("cmd", "code");
			JSONObject json = CommsPhoneCode.getCode(getRequest());
			String success = json.getString("success");
			setAttr("success", success);
			if(!"1".equals(success)){
				setAttr("msg", json.get("msg"));
			}
		}else if("auth".equals(cmd)){//手机认证
//			request.setAttribute("cmd", "auth");
			JSONObject json = CommsPhoneCode.auth(getRequest(), device.getStr("router_sn"), client.getStr("client_mac"));
			String success = json.getString("success");
			if(!"1".equals(success)){
				setAttr("msg", json.get("msg"));
			}else{
				redirect("/authorizeAccess?key="+json.getString("key"));//认证成功
				return;
			}
		}
		setAttr("banner_advs", CommsRoute.getBannerAvds(device.get("shop_id")));
		render("mb/index2/index.jsp");
	}
	
}
