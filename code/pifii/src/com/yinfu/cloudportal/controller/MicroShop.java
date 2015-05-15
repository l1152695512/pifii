package com.yinfu.cloudportal.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.Portal;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
@Before(RouterInterceptor. class)
@ControllerBind(controllerKey = "/portal/mb/microshop", viewPath = "/portal/mb/microshop")
public class MicroShop extends Controller<Record>{
	
	public void index(){
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		List<Record> types = Db.find("select concat('"+Portal.serverpath+"','/',icon) icon,name from bp_micro_shop_type where shop_id=? ", new Object[]{device.get("shop_id")});
		setAttr("types", types);
		StringBuffer sql = new StringBuffer();
		sql.append("select ms.title,concat('"+Portal.serverpath+"','/',ms.img) img,ms.old_price,ms.new_price,mst.name,");
		sql.append("if(ms.link is null or ms.link='',concat('"+getRequest().getContextPath()+"','/portal/mb/microshop/detail/',ms.id) ,ms.link) link ");
		sql.append("from bp_micro_shop ms join bp_micro_shop_type mst on (ms.type=mst.id) ");
		sql.append("where ms.shop_id=? ");
		List<Record> products = Db.find(sql.toString(), new Object[]{device.get("shop_id")});
		setAttr("products", products);
		render("weidian.jsp");
	}
	
	public void detail(){
		StringBuffer sql = new StringBuffer();
		sql.append("select title,concat('"+Portal.serverpath+"','/',img) img,old_price,new_price,remark ");
		sql.append("from bp_micro_shop where id=? ");
		Record rec = Db.findFirst(sql.toString(), new Object[]{getPara()});
		setAttr("detail", rec);
		render("product.jsp");
	}
}
