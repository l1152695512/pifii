package com.yinfu.system.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.yinfu.UrlConfig;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.system.model.Log;


@CacheName(value = "/system/log")
@ControllerBind(controllerKey = "/system/log" ,viewPath=UrlConfig.SYSTEM)
public class LogController extends Controller<Log> 
{

	
	@Before(value = { CacheInterceptor.class })
	public void getVisitCount(){
		
		renderGson(Log.dao.getVisitCount());
		
	}
	
	public void list()
	{
		renderJson( Log.dao.listByDataGrid(getDataGrid(), getFrom(Log.dao.tableName)));
	}
	
	public void excel()
	{
		renderExcel(Log.dao.list(getFrom(Log.dao.tableName)),"log.xls",new String[]{"uid","id", "用户","事件","来源","日期","ip"});
	
	}

	public void chart(){
		
		renderGson(Log.dao.chart(getFrom(null)));
	}
	
	
	
	
	@Before(value = { EvictInterceptor.class })
	public void delete()
	{
		renderJsonResult( Log.dao.deleteById(getPara("id")));
	}

}
