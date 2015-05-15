package com.yinfu.business.application.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.yinfu.business.application.model.Sms;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/app/sms", viewPath = "/page/business/application/sms")
public class SmsController extends Controller<Sms>
{

	/**
	 * 根据shopId获取短信记录
	 */
	public void getListByShopId()
	{	
		// 分页基本变量（jqGrid自带）
		int pageNum = getParaToInt("pageNum");
		int pageSize = getParaToInt("pageSize");
		String shopId = getPara("shopId");
		Page<Sms> page = Sms.dao.getListByShopId(pageNum, pageSize, shopId);
		renderJson(page);
	}
	
	
	public void add()
	{
	}
	
}
