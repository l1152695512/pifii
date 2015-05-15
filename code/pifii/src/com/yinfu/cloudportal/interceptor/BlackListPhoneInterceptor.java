package com.yinfu.cloudportal.interceptor;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.utils.SessionParams;

/**
 * 针对之前有使用手机认证过的客户端
 * 
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
public class BlackListPhoneInterceptor implements Interceptor{
//	private static Logger logger = Logger.getLogger(BlackListPhoneInterceptor.class);
	
	@Override
	public void intercept(ActionInvocation ai) {
		Controller c = ai.getController();
		Record device = c.getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record client = c.getSessionAttr(SessionParams.CLIENT_INFO);
		if(StringUtils.isNotBlank(client.get("client_mac").toString()) && 
				null != device.getStr("shop_id") && StringUtils.isNotBlank(device.getStr("shop_id"))){
			
			Record phoneInfo = Db.findFirst("select tag from bp_auth where auth_type='phone' and client_mac=? order by auth_date desc ", 
					new Object[]{client.get("client_mac")});
			String phone = "";
			if(null != phoneInfo && null != phoneInfo.get("tag")){//以前使用手机认证过
				phone = phoneInfo.get("tag").toString();
			}else{
				//这里对手机认证获取验证码的请求做过滤，判断是否为黑名单手机号码
				phone = getParamPhone(ai);
			}
			if(StringUtils.isNotBlank(phone)){
				Record info = Db.findFirst("select id from bp_shop_blacklist where type='phone' and tag=? and shop_id=? ", 
						new Object[]{phone,device.get("shop_id")});
				if(null != info){
					String actionKey = ai.getActionKey();
					if((actionKey.startsWith("/portal/mb/temp2") || 
							actionKey.startsWith("/portal/mb/temp3"))
							&& StringUtils.isBlank(c.getPara("cmd"))){//如果不是直接请求模板2和模板3，就直接拦截，如果是模板2和模板3的话拦截之后会出现循环跳转，因为下面的提示弹出后会回到上一个页面
						
					}else{
						c.setAttr("errorMsg", "你的号码已经禁止连网，如有疑问请联系商家！");
						c.render("/portal/mb/error/alert.jsp");
						return;
					}
				} 
			}
		}
		ai.invoke();
	}
	
	private String getParamPhone(ActionInvocation ai){
		Controller c = ai.getController();
		String actionKey = ai.getActionKey();
		if(actionKey.startsWith("/portal/mb/auth") || //手机端上网应用的链接地址
				actionKey.startsWith("/portal/pc") || //PC端上网认证的链接地址
				actionKey.startsWith("/portal/mb/temp2") || //模板2的链接地址
				actionKey.startsWith("/portal/mb/temp3")){//模板3的链接地址
			String cmd = c.getPara("cmd");
			if("code".equals(cmd) || "auth".equals(cmd)){//获取验证码和认证
				return c.getPara("phone");
			}
		}
		return "";
	}
}
