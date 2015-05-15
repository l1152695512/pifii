package com.yinfu.business.application.controller;


import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/app/customapp", viewPath = "/page/business/application/customapp")
public class CustomAppController extends Controller<Record> {
	public void index(){
		if(null != getPara("shopId")){
			Record thisApp = Db.findFirst("select id,url from bp_custom_app where shop_id=? ", new Object[]{getPara("shopId")});
			if(null != thisApp){
				setAttr("customApp", thisApp);
			}
		}
		render("customApp.jsp");
	}

	public void save() {
		Record rec = getModel();
		if(null == rec.get("url")){
			rec.set("url", "");
		}
		if(null!=rec.get("id") && StringUtils.isNotBlank(rec.get("id").toString())){
			Db.update("bp_custom_app", rec);
		}else{
			Object id = UUID.randomUUID().toString();
			Db.save("bp_custom_app", rec.set("id", id)
					.set("create_date",new Timestamp(System.currentTimeMillis())));
		}
		renderJsonResult(true);
	}
}
