package com.yinfu.cloudportal.interceptor;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.utils.SessionParams;

/**
 * 对上网应用、手机认证、微信认证（以后加）做拦截，如果客户端mac为黑名单，则禁止认证
 * 
 * 其中包含的链接地址：
 * 1.portal/mb/auth			手机端上网应用的链接地址
 * 2.portal/pc				PC端上网认证的链接地址
 * 3.portal/mb/temp2		模板2的链接地址（模板2中包含认证）
 * 4.portal/mb/temp3		模板3的链接地址（模板3中包含认证）
 * 
 * 5.微信的链接地址（以后加）
 * 
 * @author liuzhao
 *
 */
public class BlackListMacInterceptor implements Interceptor{
//	private static Logger logger = Logger.getLogger(BlackListMacInterceptor.class);
	
	@Override
	public void intercept(ActionInvocation ai) {
		Controller c = ai.getController();
		Record device = c.getSessionAttr(SessionParams.ACCESS_DEVICE);
		if(StringUtils.isNotBlank(device.getStr("router_sn")) && 
				null != device.getStr("shop_id") && StringUtils.isNotBlank(device.getStr("shop_id"))){
			Record client = c.getSessionAttr(SessionParams.CLIENT_INFO);
			Record thisMac = Db.findFirst("select id from bp_shop_blacklist where type='mac' and tag=? and shop_id=? ", 
					new Object[]{client.get("client_mac"),device.get("shop_id")});
			if(null != thisMac){
				String actionKey = ai.getActionKey();
				if((actionKey.startsWith("/portal/mb/temp2") || 
						actionKey.startsWith("/portal/mb/temp3"))
						&& StringUtils.isBlank(c.getPara("cmd"))){//如果不是直接请求模板2和模板3，就直接拦截，如果是模板2和模板3的话拦截之后会出现循环跳转，因为下面的提示弹出后会回到上一个页面
					
				}else{
					c.setAttr("errorMsg", "你的设备已经禁止连网，如有疑问请联系商家！");
					c.render("/portal/mb/error/alert.jsp");
					return;
				}
			}
		}
		ai.invoke();
	}
	
}
