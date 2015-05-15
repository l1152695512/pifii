package com.yinfu.servlet.route.fun;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

public class Food {
	private String routersn;
	private String clientMac;
	
	public Food(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public JSONObject getOrder(HttpServletRequest request){
		JSONObject json = new JSONObject();
		json.put("orderNum", "");
		json.put("orderDate", "");
		json.put("foods", "");
		json.put("orderId", "");
		
		String historyOrderId = request.getParameter("orderId");
		System.err.println("historyOrderId="+historyOrderId);
		System.err.println("clientMac="+clientMac);
		Record historyOrder = Db.findFirst("select id,order_num,status,date_format(insert_date,'%Y-%m-%d %H:%i:%s') insert_date from bp_route_food_order_tbl where id=? or client_mac=? order by insert_date desc ", 
				new Object[]{historyOrderId,clientMac});
		if(null != historyOrder && "1".equals(historyOrder.get("status").toString())){//如果有未完成订单
			json.put("orderNum", historyOrder.get("order_num"));
			json.put("orderDate", historyOrder.get("insert_date"));
			json.put("orderId", historyOrder.get("id"));
			List<Record> foods = Db.find("select food_id,food_num from bp_route_food_tbl where order_id = ?",new Object[]{historyOrder.get("id")});
			StringBuffer foodsBuf = new StringBuffer();
			Iterator<Record> ite = foods.iterator();
			while(ite.hasNext()){
				Record rowData = ite.next();
				foodsBuf.append("&food="+rowData.get("food_id")+":"+rowData.get("food_num"));
			}
			json.put("foods", foodsBuf.toString());
		}
//		if(null != historyOrder){//加载历史点菜
//			json.put("orderId", historyOrder.get("id"));
//			List<Record> foods = Db.find("select food_id,food_num from bp_route_food_tbl where order_id = ?",new Object[]{historyOrder.get("id")});
//			StringBuffer foodsBuf = new StringBuffer();
//			Iterator<Record> ite = foods.iterator();
//			while(ite.hasNext()){
//				Record rowData = ite.next();
//				foodsBuf.append("&food="+rowData.get("food_id")+":"+rowData.get("food_num"));
//			}
//			json.put("foods", foodsBuf.toString());
//		}
		System.err.println(json.toString());
		return json;
	}
	
	public JSONObject saveFoodOrder(HttpServletRequest request){//如果已下了订单就直接返回订单，并刷新页面，显示订单页
		JSONObject json = new JSONObject();
		boolean success = false;
		String historyOrderId = request.getParameter("orderId");
		Record historyOrder = Db.findFirst("select id from bp_route_food_order_tbl where (id=? or client_mac=?) and status=1 ", new Object[]{historyOrderId,clientMac});
		if(null != historyOrder){
			json.put("success", false);
			json.put("msg", "refresh");
//			json.put("msg", "你的上一个订单未完成，不能再次下订单，请联系服务员。");
			return json;
		}
		String[] foods = request.getParameterValues("food");
		final int orderNum = getOrderNum();
		if(orderNum == -1){
			json.put("msg", "无可用号牌，请联系服务员！");
		}else{
			try{
				final String orderId = UUID.randomUUID().toString();
				final Object[][] paramsArr = new Object[foods.length][4];
				for(int i=0;i<foods.length;i++){
					String[] foodInfo = foods[i].split(":");
					paramsArr[i][0] = UUID.randomUUID().toString();
					paramsArr[i][1] = orderId;
					paramsArr[i][2] = foodInfo[0];
					paramsArr[i][3] = foodInfo[1];
				}
				success = Db.tx(new IAtom(){public boolean run() throws SQLException {
					int[] changeRows = Db.batch("insert into bp_route_food_tbl(id,order_id,food_id,food_num) values(?,?,?,?) ",
							paramsArr, paramsArr.length);
					for(int i=0;i<changeRows.length;i++){
						if(changeRows[i] < 1){
							return false;
						}
					}
					int chageRows = Db.update("insert into bp_route_food_order_tbl(id,router_sn,client_mac,order_num,status,insert_date) values(?,?,?,?,?,now())",
							new Object[]{orderId,routersn,clientMac,orderNum,1});
					if(chageRows == 1){
						return true;
					}else{
						return false;
					}
				}});
				if(success){
					json.put("orderNum", orderNum);
					json.put("orderDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					json.put("orderId", orderId);
				}else{
					json.put("msg", "暂时无法点餐请稍后！");
				}
			}catch(Exception e){
				e.printStackTrace();
				json.put("msg", "暂时无法点餐请稍后！");
			}
		}
		json.put("success", success);
		return json;
	}
	
	private int getOrderNum(){
		List<Integer> orderNumPool = new ArrayList<Integer>();
		int maxNum = PropertyUtils.getPropertyToInt("router.food.maxOrderNum", 999);
		while(maxNum > 0){
			orderNumPool.add(maxNum);
			maxNum--;
		}
		
		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT DISTINCT fo.order_num num ");
//		sql.append("FROM bp_device d1 join bp_device d2 on (d2.router_sn=? and d1.shop_id=d2.shop_id) ");
//		sql.append("join bp_route_food_order_tbl fo on (date_add(fo.insert_date, interval 30 minute) < now() and fo.status!=1 and fo.router_sn=d1.router_sn) ");
//		List<Record> data = Db.find(sql.toString(),new Object[]{routersn});
		
		sql.append("SELECT DISTINCT order_num num ");
		sql.append("FROM ( ");
		sql.append("SELECT fo.order_num ");
		sql.append("FROM bp_device d1 join bp_device d2 on (d2.router_sn=? and d1.shop_id=d2.shop_id) ");
		sql.append("join bp_route_food_order_tbl fo on (date_add(fo.insert_date, interval 10 minute) < now() and fo.router_sn!=? and fo.status!=1 and fo.router_sn=d1.router_sn) ");
		sql.append("UNION ");
		sql.append("SELECT order_num ");
		sql.append("FROM bp_route_food_order_tbl ");
		sql.append("where date_add(insert_date, interval 10 minute) < now() and router_sn=? and status!=1 ");
		sql.append(") a ");
		List<Record> data = Db.find(sql.toString(),new Object[]{routersn,routersn,routersn});
		
		Iterator<Record> ite = data.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			int num = -1;
			try{
				num = rowData.getInt("num");
			}catch(Exception e){
			}
			int index = orderNumPool.indexOf(num);
			if(index != -1){
				orderNumPool.remove(index);
			}
		}
		if(orderNumPool.size() > 0){
			int index = (int) (Math.random()*orderNumPool.size());
			return orderNumPool.get(index);
		}else{
			return -1;
		}
	}
}
