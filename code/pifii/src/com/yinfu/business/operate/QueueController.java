
package com.yinfu.business.operate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/queue", viewPath = "/page/business/operate/")
public class QueueController extends Controller<Device> {
	public void index(){
		render("queue.jsp");
	}
	
	public void list(){
		Map<String,Object> returnData = new HashMap<String,Object>();
		List<Object> params = new ArrayList<Object>();
		params.add(getPara("shopId"));
		StringBuffer sql = new StringBuffer();
		sql.append("select q.id,q.queue_num,q.person_num,date_format(q.insert_date,'%Y-%m-%d %H:%i:%s') date ");
		sql.append("from bp_device d join bp_shop s on (s.id=? and d.shop_id=s.id) ");
		sql.append("join bp_router_queue q on (q.status=1 ");
		if(StringUtils.isNotBlank(getPara("personNum"))){
			sql.append("and q.person_num=? ");
			params.add(getPara("personNum"));
		}
		sql.append("and d.router_sn=q.router_sn) ");
		sql.append("order by q.insert_date ");
		List<Record> persons = Db.find(sql.toString(), params.toArray());
		StringBuffer ids = new StringBuffer();
		Iterator<Record> ite = persons.iterator();
		while(ite.hasNext()){
			Record row = ite.next();
			ids.append(row.get("id").toString()+":");
		}
		returnData.put("thisData", ids.toString());
		if(!ids.toString().equals(getPara("previousData"))){
			returnData.put("dataList", persons);
		}
		renderJson(returnData);
	}
	
	public void removeNum(){
		try{
			Db.update("update bp_router_queue set status=0 where id=? ", new Object[]{getPara("id")});
		}catch(Exception e){
			renderJsonResult(false);
		}
		renderJsonResult(true);
	}
}
