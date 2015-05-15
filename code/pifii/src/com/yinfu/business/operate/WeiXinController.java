
package com.yinfu.business.operate;

import com.jfinal.ext.route.ControllerBind;
import com.yinfu.business.device.model.Device;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;

@ControllerBind(controllerKey = "/business/weixin", viewPath = "/page/business/")
public class WeiXinController extends Controller<Device> {
	public void index(){
		setAttr("baseUrl", PropertyUtils.getProperty("router.auth.url", "http://www.pifii.com:8090/pifii/authorizeAccess")+"?weixin=");
		render("operate/weixin.jsp");
	}
	
}
