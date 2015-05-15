
package com.yinfu.business.operate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/feedback", viewPath = "/page/business/operate/")
public class FeedbackController extends Controller<Device> {
	public void index(){
		render("feedback.jsp");
	}
	
	public void list(){
		List<Object> params = new ArrayList<Object>();
		params.add(getPara("shopId"));
		StringBuffer sql = new StringBuffer();
		sql.append("select opinion,date_format(create_time,'%Y-%m-%d %H:%i:%s') create_time from (");
		sql.append("select f.opinion,f.mac,f.create_time ");
		sql.append("from bp_feedback f join bp_device d on (d.shop_id=? and f.router_sn=d.router_sn) ");
		sql.append("where f.mac is not null ");
		if(StringUtils.isNotBlank(getPara("startDate"))){
			sql.append("and f.create_time>? ");
			params.add(getPara("startDate"));
		}
		if(StringUtils.isNotBlank(getPara("endDate"))){
			sql.append("and f.create_time<? ");
			params.add(getPara("endDate"));
		}
		sql.append("order by f.create_time desc ");
		sql.append(") a ");
		sql.append("group by mac ");
		sql.append("order by create_time desc ");
		List<Record> foodOrders = Db.find(sql.toString(), params.toArray());
		renderJson(foodOrders);
	}
	
}
