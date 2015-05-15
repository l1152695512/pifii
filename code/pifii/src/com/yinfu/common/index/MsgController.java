package com.yinfu.common.index;

import com.yinfu.Consts;
import com.yinfu.UrlConfig;
import com.yinfu.common.validator.MsgValidator;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.system.model.User;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;

@CacheName(value = "/index/msg")
@ControllerBind(controllerKey = "/index/msg")
public class MsgController extends Controller<Msg>
{

	/***
	 * 使用页面缓存 注意：经常变动的不能使用缓存 与权限相关的 不能用页面缓存 可使用 sql 缓存
	 * 
	 */
	@Before(value = { CacheInterceptor.class })
	public void list()
	{
		setAttr("list", Msg.dao.list());

		render(UrlConfig.VIEW_INDEX_MSG);

	}

	@Before(value = { EvictInterceptor.class, MsgValidator.class })
	public void add()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		renderJsonResult(getModel( ).set("uid", user.getId()).saveAndDate());
		
	}

}
