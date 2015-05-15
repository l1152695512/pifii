
package com.yinfu.business.operate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.MS_FeieAPI;
import com.yinfu.jbase.util.PropertyUtils;

@ControllerBind(controllerKey = "/business/food", viewPath = "/page/business/operate/")
public class FoodController extends Controller<Device> {
	public void index(){
		render("food.jsp");
	}
	
	public void list(){
		Map<String,Object> returnData = new HashMap<String,Object>();
		List<Object> params = new ArrayList<Object>();
		params.add(getPara("shopId"));
		StringBuffer sql = new StringBuffer();
		sql.append("select fo.id,fo.order_num,fo.status,date_format(fo.insert_date,'%Y-%m-%d %H:%i:%s') insert_date,count(f.id) food_num,");
		sql.append("sum(m.old_price*f.food_num) old_price,sum(m.new_price*f.food_num) new_price ");
		sql.append("from bp_device d join bp_route_food_order_tbl fo on (d.router_sn=fo.router_sn) ");
		sql.append("join bp_route_food_tbl f on (fo.id=f.order_id) ");
		sql.append("join bp_menu m on (m.id=f.food_id) ");
		sql.append("where d.shop_id=? ");
		if(StringUtils.isNotBlank(getPara("status"))){
			sql.append("and fo.status=? ");
			params.add(getPara("status"));
		}
		if(StringUtils.isNotBlank(getPara("startDate"))){
			sql.append("and fo.insert_date>? ");
			params.add(getPara("startDate"));
		}
		if(StringUtils.isNotBlank(getPara("endDate"))){
			sql.append("and fo.insert_date<? ");
			params.add(getPara("endDate"));
		}
		sql.append("group by fo.id ");
		sql.append("order by fo.insert_date ");
		List<Record> foodOrders = Db.find(sql.toString(), params.toArray());
		StringBuffer ids = new StringBuffer();
		Iterator<Record> ite = foodOrders.iterator();
		while(ite.hasNext()){
			Record row = ite.next();
			ids.append(row.get("id").toString()+":");
		}
		returnData.put("thisData", ids.toString());
		if(!ids.toString().equals(getPara("previousData"))){
			returnData.put("dataList", foodOrders);
		}
		renderJson(returnData);
	}
	
	public void changeStatus(){
		int changeRows = Db.update("update bp_route_food_order_tbl set status=? where id=? ", new Object[]{getPara("status"),getPara("id")});
		renderJsonResult(changeRows == 1);
	}
	
	public void orderInfoIndex(){
		setAttr("orderId", getPara("orderId"));
		render("foodInfo.jsp");
	}
	
	public void orderInfo(){
		StringBuffer sql = new StringBuffer();
		sql.append("select rf.food_num,m.name,m.old_price,m.new_price,IFNULL(m.taste,'') taste,m.icon,mt.name food_type_name ");
		sql.append("from bp_route_food_tbl rf join bp_menu m on (rf.order_id=? and rf.food_id=m.id) ");
		sql.append("join bp_menu_type mt on (m.type=mt.id) ");
		List<Record> orderFoods = Db.find(sql.toString(), new Object[]{getPara("orderId")});
		renderJson(orderFoods);
	}
	
	public void print(){
		StringBuffer sql = new StringBuffer();
		sql.append("select rf.food_num,m.name,m.old_price,m.new_price,IFNULL(m.taste,'') taste,m.icon,mt.name food_type_name ");
		sql.append("from bp_route_food_tbl rf join bp_menu m on (rf.order_id=? and rf.food_id=m.id) ");
		sql.append("join bp_menu_type mt on (m.type=mt.id) ");
		List<Record> orderFoods = Db.find(sql.toString(), new Object[]{getPara("orderId")});
		
		 JSONArray array = new JSONArray();
		 Float total = 0.0f;
		 for(Record rd : orderFoods){
			 String name = rd.getStr("name");
			 String money = rd.get("new_price").toString();
			 String amount = rd.get("food_num").toString();
			 Float cost = Float.parseFloat(money) * Integer.parseInt(amount);
			 JSONObject son = new JSONObject();
			 son.put("name", name);
			 son.put("money", money);
			 son.put("amount", amount);
			 son.put("cost", cost+"");
			 array.add(son);
			 total += cost;
		 }
		 
		 
		 String title = "";
		 String address = "";
		 Record rd = Db.findById("bp_shop", getPara("shopId"));
		 if(rd != null){
			 title = rd.getStr("name");
			 address = rd.getStr("addr");
		 }
		 
		 String phone = "";
		 String orderTime = "";
		 String routersn = "";
		 StringBuffer buff = new StringBuffer("SELECT DATE_FORMAT(a.insert_date,'%Y-%m-%d %H:%i:%s') insert_date,a.router_sn,IFNULL(b.tag,'') tag ");
		 buff.append("FROM bp_route_food_order_tbl a ");
		 buff.append("LEFT JOIN bp_auth b ");
		 buff.append("ON a.client_mac=b.client_mac AND a.router_sn=b.router_sn AND b.auth_type='phone' ");
		 buff.append("WHERE a.id=?");
		 rd = Db.findFirst(buff.toString(),new Object[]{getPara("orderId")});
		 if(rd != null){
			 phone = rd.getStr("tag");
			 orderTime = rd.get("insert_date");
			 routersn = rd.getStr("router_sn");
		 }
		 
		 JSONObject json = new JSONObject();
		 json.put("title", title);
		 json.put("list", array);
		 json.put("remark", "无");
		 json.put("total", total+"");
		 json.put("address", address);
		 json.put("phone", phone);
		 json.put("orderTime", orderTime);
		 json.put("qrcode", "http://www.mofi139.com/jxsqt/portal/mb/nav/"+routersn);
		 
		 String clientCode = PropertyUtils.getProperty("ms.clientCode", "814070813");
		 String strkey = PropertyUtils.getProperty("ms.key", "0TAT2Na3");
		 String returnData = MS_FeieAPI.sendDefaultFormatOrderInfo(clientCode,strkey,"1",json);
		 System.out.println(returnData);
		 renderJson(returnData);
	}
}
