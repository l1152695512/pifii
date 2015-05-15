
package com.yinfu.shiro;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.yinfu.cloudportal.utils.SessionParams;

/***
 * 用于拦截登录用户的商铺请求，登录用户只能对自己拥有权限的商铺进行操作
 * 
 * @author 12
 */
public class ShopInterceptor implements Interceptor {
	
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		HttpSession session = controller.getSession(false);
		if(null != session){
			List<Object> shopList= controller.getSessionAttr(SessionParams.MY_SHOP_LIST);
			if(null!=shopList && null!=controller.getPara("shopId")){ 
				if(shopList.contains(controller.getPara("shopId"))){
					ai.invoke();
				}else{
					controller.renderError(401);
				}
			}else{
				ai.invoke();
			}
		}
	}
}
