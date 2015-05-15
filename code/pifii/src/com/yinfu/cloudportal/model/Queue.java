package com.yinfu.cloudportal.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

public class Queue {
	private static final String EMPTY_MAC = "00:00:00:00:00:00";
	private String routersn;
	private String clientMac;
	
	public Queue(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public JSONObject getNum(String personNum){
		JSONObject json = new JSONObject();
		json.put("success", false);
		if(EMPTY_MAC.equals(clientMac)){
			json.put("msg", "无法识别客户信息，请稍后再试！");
		}else{
			try{
				int num = Integer.parseInt(personNum);
				if(num>0 && num<15){
					Record record = Db.findFirst("select id from bp_router_queue where router_sn=? and mac=? and status = 1 ",new Object[]{routersn,clientMac});
					if(null != record){
						json.put("msg", "不能重复排队！");
					}else{
						executeQueue(personNum,json);
					}
				}else{
					throw new Exception();
				}
			}catch(Exception e){
				json.put("msg", "非法参数！");
			}
		}
		return json;
	}
	
	public JSONObject cancelNum(){
		JSONObject json = new JSONObject();
		json.put("success", false);
		try{
			Db.update("update bp_router_queue set status=0 where router_sn=? and mac=? ", new Object[]{routersn,clientMac});
			json.put("success", true);
		}catch(Exception e){
			json.put("msg", "取消排队失败，稍后请重试！");
		}
		return json;
	}
	
	private Object getBeforeMe(String routersn,Object personNum,Object date){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(DISTINCT id) beforeMe ");
		sql.append("from ( ");
		sql.append("select q.id ");
		sql.append("from bp_device d1 join bp_device d2 on (d2.router_sn=? and d1.shop_id=d2.shop_id) ");
		sql.append("join bp_router_queue q on (q.status=1 and q.person_num=? and q.insert_date<? and q.router_sn=d1.router_sn) ");
		sql.append("union ");
		sql.append("select id ");
		sql.append("from bp_router_queue ");
		sql.append("where router_sn=? and status=1 and person_num=? and insert_date<? ");
		sql.append(") a ");
//		StringBuffer sql = new StringBuffer();
//		sql.append("select count(*) beforeMe ");
//		sql.append("from bp_device d1 join bp_device d2 on (d2.router_sn=? and d1.shop_id=d2.shop_id) ");
//		sql.append("join bp_router_queue q on (q.status=1 and q.person_num=? and q.insert_date<? and q.router_sn=d1.router_sn) ");
		System.err.println(sql.toString());
		Record rec = Db.findFirst(sql.toString(),new Object[]{routersn,personNum,date,routersn,personNum,date});
		return rec.get("beforeMe");
	}
	
	public JSONObject historyNum(){
		JSONObject json = new JSONObject();
		json.put("personNum", 1);
		json.put("num", -1);
		Record record = Db.findFirst("select person_num,queue_num,insert_date from bp_router_queue where router_sn=? and mac=? and status = 1 ",new Object[]{routersn,clientMac});
		if(null != record){
			Object date =  record.get("insert_date");
			Object personNum = record.get("person_num");
			json.put("personNum", personNum);
			json.put("num", record.get("queue_num"));
			json.put("beforeMe", getBeforeMe(routersn,personNum,date));
		}
		return json;
	}
	
	private synchronized void executeQueue(String personNum,JSONObject json){
		int lineNum = getLineNum();
		if(lineNum != -1){
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			int changRow = Db.update("insert into bp_router_queue(router_sn,mac,person_num,queue_num,status,insert_date) values(?,?,?,?,?,?)", 
					new Object[]{routersn,clientMac,personNum,lineNum,1,date});
			if(changRow == 1){
				json.put("success", true);
				json.put("num", lineNum);
				json.put("beforeMe", getBeforeMe(routersn,personNum,date));
			}else{
				json.put("msg", "获取号码失败，稍后请重试！");
			}
		}else{
			json.put("msg", "号码已取完，请稍后排队！");
		}
	}
	
	private int getLineNum(){
		List<Integer> orderNumPool = new ArrayList<Integer>();
		int maxNum = PropertyUtils.getPropertyToInt("router.food.maxOrderNum", 999);
		int releaseNumMinuteLength = PropertyUtils.getPropertyToInt("router.queue.releaseNumMinuteLength", 30);
		while(maxNum > 0){
			orderNumPool.add(maxNum);
			maxNum--;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT q.queue_num num ");
		sql.append("FROM bp_device d1 join bp_device d2 on (d2.router_sn=? and d1.shop_id=d2.shop_id) ");
		sql.append("join bp_router_queue q on (date_add(q.insert_date, interval ? minute) < now() and q.status!=1 and q.router_sn=d1.router_sn) ");
		sql.append("order by q.queue_num ");
		List<Record> data = Db.find(sql.toString(),new Object[]{routersn,releaseNumMinuteLength});
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
