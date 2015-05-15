package com.yinfu.servlet.route.fun;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

public class QueueN {
	public static Map<String,Map<String,Map<String,Object>>> queueInfos = new HashMap<String, Map<String,Map<String,Object>>>();
	private static Map<String,List<String>> lineNumbers = new HashMap<String,List<String>>();//取号池
	private static List<String> lineNumberStatus = new ArrayList<String>();//存储开启了排队的路由器
	private static final int maxLineNumber = 1000;
	
	private String routersn;
	private String clientMac;
	private String cmd;
	
	public QueueN(String routersn,String clientMac,String cmd) {
		this.routersn = routersn;
		this.clientMac = clientMac;
		this.cmd = cmd;
	}
	
	public JSONObject getNum(){
		JSONObject json = new JSONObject();
		json.put("success", false);
		Record record = Db.findFirst("select id from bp_router_queue where router_sn=? and mac=? and status = 1 ",new Object[]{routersn,clientMac});
		if(null != record){
			json.put("msg", "不能重复排队！");
		}else{
			int lineNum = getLineNum();
			if(lineNum != -1){
				json.put("success", true);
				json.put("num", lineNum);
			}else{
				json.put("msg", "号码已取完，请稍后排队！");
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
	
	public JSONObject historyNum(){
		JSONObject json = new JSONObject();
		json.put("personNum", 1);
		json.put("num", -1);
		Record record = Db.findFirst("select person_num,queue_num from bp_router_queue where router_sn=? and mac=? and status = 1 ",new Object[]{routersn,clientMac});
		if(null != record){
			json.put("personNum", record.get("person_num"));
			json.put("num", record.get("queue_num"));
		}
		return json;
	}
	
	private int getLineNum(){
		List<Integer> orderNumPool = new ArrayList<Integer>();
		int maxNum = PropertyUtils.getPropertyToInt("router.food.maxOrderNum", 999);
		while(maxNum > 0){
			orderNumPool.add(maxNum);
			maxNum--;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT q.queue_num num ");
		sql.append("FROM bp_device d1 join bp_device d2 on (d2.router_sn=? and d1.shop_id=d2.shop_id) ");
		sql.append("join bp_router_queue q on (date_add(q.insert_date, interval 30 minute) < now() and q.status!=1 and q.router_sn=d1.router_sn) ");
		sql.append("order by q.queue_num ");
		List<Record> data = Db.find(sql.toString(),new Object[]{routersn});
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
	
	
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		if(cmd.indexOf("get") != -1){
			JSONObject returnJson = getNextLineNum(request.getParameter("isCreate"),request.getParameter("personNumber"));
			returnJsonp(request,response,returnJson);
		}else if(cmd.indexOf("cancel") != -1){
			String deleteMac = request.getParameter("deleteMac");
//			String manageMac = request.getParameter("manageMac");
			JSONObject returnJson = cancelLineNum(deleteMac);
			returnJsonp(request,response,returnJson);
		}else if(cmd.indexOf("list") != -1){
			JSONObject returnJson = new JSONObject();
//			if(null != manageMac && manageMac.equals(clientMac)){//如果是服务人员
//				returnJson.put("success", "true");
//				returnJson.put("persons", queueInfos.get(routersn));
//			}else{
				returnJson.put("success", "false");
//			}
			returnJsonp(request,response,returnJson);
		}else if(cmd.indexOf("status") != -1){
			returnJsonp(request,response,changeRouteLineNumStatus(request.getParameter("status")));
		}else{
			JSONObject returnJson = new JSONObject();
			returnJson.put("success", "false");
			returnJson.put("msg", "参数错误！");
			returnJsonp(request, response,returnJson);
		}
	}
	
	private JSONObject cancelLineNum(String deleteMac){
		JSONObject returnJson = new JSONObject();
		if(clientMac.equals(deleteMac)){// || clientMac.equals(manageMac)或者是服务人员
//		if(null != deleteMac && !manageMac.equals(clientMac)){
			deleteMac = null == deleteMac?clientMac:deleteMac;
			
			Map<String,Map<String,Object>> routeQueue = queueInfos.get(routersn);
			try{
				Map<String,Object> info = routeQueue.get(deleteMac);
				String number = info.get("number").toString();
				lineNumbers.get(routersn).add(number);
				routeQueue.remove(deleteMac);
			}catch(Exception e){
			}
			returnJson.put("success", "true");
//			returnJson.put("beforeMe", null == routeQueue?"0":routeQueue.size());
		}else{
			returnJson.put("success", "false");
		}
		return returnJson;
	}
	
	private JSONObject changeRouteLineNumStatus(String status){
		JSONObject returnJson = new JSONObject();
//		if(clientMac.equals(manageMac)){
//			if("1".equals(status)){
//				lineNumberStatus.remove(routersn);
//			}else{
//				lineNumberStatus.add(routersn);
//			}
//			returnJson.put("success", "true");
//		}
		return returnJson;
	}
	
	private JSONObject getNextLineNum(String isCreate,String personNumber){
		JSONObject returnJson = new JSONObject();
		//如果开启了排队或者为管理员或者不为取号操作
		if((!lineNumberStatus.contains(routersn)) || !isCreate.equals("1")){// || clientMac.equals(manageMac)
//			if(clientMac.equals(manageMac)){//将路由是否开启了排队的状态返回给客户端（0：关闭；1：开启）
//				returnJson.put("status", lineNumberStatus.contains(routersn)?"0":"1");
//			}
			Map<String,Map<String,Object>> routeQueue = queueInfos.get(routersn);
			if(null == routeQueue){
				routeQueue = new LinkedHashMap<String,Map<String,Object>>();
			}
			returnJson.put("personNumber", personNumber);
			Map<String,Object> info = routeQueue.get(clientMac);
			if(null == info){
				if("1".equals(isCreate)){
					if(!lineNumbers.containsKey(routersn)){
						initLineNumber();
					}
					List<String> lineNumber = lineNumbers.get(routersn);
					if(lineNumber.size() > 0){
						returnJson.put("beforeMe", beforeMe(personNumber,true));
						String createNumber = lineNumber.get(0);
						info = new HashMap<String,Object>();
						info.put("number", createNumber);
						info.put("date", new Date());
						info.put("personNumber", personNumber);
						String addMac = clientMac;
//						if(clientMac.equals(manageMac)){
//							addMac = clientMac+"_"+UUID.randomUUID().toString().substring(0, 5);
//						}
						routeQueue.put(addMac, info);
						queueInfos.put(routersn, routeQueue);
						lineNumber.remove(0);
						returnJson.put("success", "true");
						returnJson.put("number", createNumber);
						returnJson.put("clientMac", addMac);
					}else{
						returnJson.put("success", "false");
						returnJson.put("msg", "号已取完，请联系服务人员！");
					}
				}else{
					returnJson.put("success", "true");
//					if(clientMac.equals(manageMac)){
//						returnJson.put("isManage", "1");
//					}
					returnJson.put("number", "-1");
				}
			}else{
				returnJson.put("success", "true");
				returnJson.put("number", info.get("number"));
				returnJson.put("personNumber", info.get("personNumber"));
				returnJson.put("beforeMe", beforeMe(info.get("personNumber").toString(),false));
				returnJson.put("clientMac", clientMac);
			}
		}else{
			returnJson.put("success", "false");
			returnJson.put("msg", "排队取号已关闭！");
		}
		return returnJson;
	}
	
	private int beforeMe(String personNumber,boolean checkAll){
		int num = 0;
		Map<String,Map<String,Object>> routeQueue = queueInfos.get(routersn);
		if(null != routeQueue && routeQueue.size() > 0){
			Iterator<String> ite = routeQueue.keySet().iterator();
			while(ite.hasNext()){
				String thisClientMac = ite.next();
				if(!checkAll && thisClientMac.equals(clientMac)){
					break;
				}
				Map<String,Object> infos = routeQueue.get(thisClientMac);
				if(infos.get("personNumber").equals(personNumber)){
					num++;
				}
			}
		}
		return num;
	}
	
	private void initLineNumber(){
		List<String> lineNumber = new ArrayList<String>();
		for(int i=1;i<maxLineNumber;i++){
			lineNumber.add(""+i);
		}
		lineNumbers.put(routersn, lineNumber);
	}
	
	private void returnJsonp(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws IOException{
		response.setContentType("text/plain");  
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        PrintWriter out = response.getWriter();       
        String jsonpCallback = request.getParameter("jsonpCallback");//客户端请求参数  
        out.println(jsonpCallback+"("+json.toString(1,1)+")");//返回jsonp格式数据  
        out.flush();  
        out.close(); 
	}
	
//	private void returnJson(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws IOException{
//		response.setContentType("application/x-json");  
//        PrintWriter pw = response.getWriter();  
//        pw.print(json.toString());
//        pw.flush();
//        pw.close();
//	}
	
}
