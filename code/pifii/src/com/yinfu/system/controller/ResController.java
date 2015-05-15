package com.yinfu.system.controller;

import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.yinfu.UrlConfig;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.easyui.Tree;
import com.yinfu.shiro.ShiroCache;
import com.yinfu.shiro.ShiroInterceptor;
import com.yinfu.system.model.Res;
import com.yinfu.system.model.Role;
import com.yinfu.system.model.User;
import com.yinfu.system.validator.ResValidator;

@ControllerBind(controllerKey = "/system/res", viewPath = UrlConfig.SYSTEM)
public class ResController extends Controller<Res>
{

	public void tree()
	{
		Integer pid = getParaToInt("id");
		Integer passId = getParaToInt("passId");
		int type = getParaToInt("type", Res.TYPE_MEUE);
		renderJson(Res.dao.getTree(pid, type, passId));

	}
	
	public void menu(){
		String userId = getAttr("userid");
		User user = User.dao.findById(userId);
		List<Tree> menuList = Res.dao.getTree(null,  Res.TYPE_MEUE, null);
		setAttr("menuList", menuList);
		render("/page/common/menu.jsp");
	}
	public void list()
	{
		renderJson(Res.dao.listOrderBySeq());
	}

	public void delete()
	{
		renderJsonResult(Res.dao.deleteByIdAndPid(getParaToInt("id")));

		removeAuthorization();
	}

	@Before(value = { ResValidator.class })
	public void add()
	{
		Res res=getModel();
		boolean result=res.save();
		renderJsonResult(result);
		
		if(result) Role.dao.grant(1, res.getId()+"");
		
		removeAuthorization();
	}

	@Before(value = { ResValidator.class })
	public void edit()
	{
		Res res = getModel();

		if (res.getId() == res.getPid()) renderJsonError("父节点不能为自己");
		else if (res.getType() == Res.TYPE_PERMISSION && Res.dao.getChild(res.getId(), null).size() > 0) renderJsonError("功能属性不能有子节点");
		else if (Res.dao.pidIsChild(res.getId(), res.getPid())) renderJsonError("父节点不能为子节点");
		else
		{
			renderJsonResult(res.update());
			removeAuthorization();
		}

	}

	private void removeAuthorization()
	{
		ShiroCache.clearAuthorizationInfoAll();
		ShiroInterceptor.updateUrls();
	}

}
