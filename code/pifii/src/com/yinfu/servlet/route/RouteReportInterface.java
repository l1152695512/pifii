package com.yinfu.servlet.route;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;


public class RouteReportInterface extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(RouteReportInterface.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	public static JSONObject getJSONBean(HttpServletRequest request) {
		Map params = request.getParameterMap();
		JSONObject json = new JSONObject();
		Iterator<String> ite = params.keySet().iterator();
		while(ite.hasNext()){
			String key = ite.next();
			json.put(key, request.getParameter(key));
		}
		return json;
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			response.setCharacterEncoding("UTF-8");
			logger.warn(getJSONBean(request));
			String fn = request.getParameter("fn");
			boolean isSuccess = true;
			if(StringUtils.isNotEmpty(fn)){
				if("cmd".equals(fn)){
					cmd(request,response);
				}else if("online".equals(fn)){
					online(request,response);
				}else if("statistics".equals(fn)){
					isSuccess = statistics(request,response);
				}
			}
			JSONObject json = new JSONObject();
			json.put("success", isSuccess);
			response.getWriter().print(json.toJSONString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void cmd(HttpServletRequest request, HttpServletResponse response){
		String routersn = request.getParameter("routersn");
		String cmdId = request.getParameter("cmdId");
		if(StringUtils.isNotEmpty(routersn) && StringUtils.isNotEmpty(cmdId)){
			Record cmd = Db.findById("bp_cmd", cmdId);
			if(cmd != null){
				String sql = "update bp_cmd set status=1,syn_date=now() where id = ?";
				Db.update(sql, new Object[]{cmdId});
				
				sql = "select 1 from bp_cmd where status=0 and uid=?"; 
				List<Record> list = Db.find(sql, new Object[]{cmd.getStr("uid")});
				if(list.size()==0){
					sql = "update bp_task set status=1,syn_date=now() where uid=?";
					Db.update(sql, new Object[]{cmd.getStr("uid")});
					//更新页面更改日志的状态
					Record res = Db.findFirst("select router_sn,type,key_id from bp_task where uid=? ",
							new Object[]{cmd.getStr("uid")});
					Db.update("update bp_page_operate set status='1',download_date=now() where router_sn=? and type=? and key_id=? ", 
							new Object[]{res.getStr("router_sn"),res.getStr("type"),res.get("key_id").toString()});
				}
			}
		}
	}
	
	private void online(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = JSONObject.parseObject(request.getParameter("json"));
		String routersn = "";
		String wifiname = "";
		String sys_version = "";
		String version_code_all = "";
		String version_tfcard = "";
		int num = 0;
		try{
			if(json.containsKey("sn")){
				routersn = json.getString("sn");
				if(json.containsKey("wifiname")){
					wifiname = json.getString("wifiname");
				}
				if(json.containsKey("sys_version")){
					sys_version = json.getString("sys_version");
				}
				if(json.containsKey("version_code_all")){
					version_code_all = json.getString("version_code_all");
				}
				if(json.containsKey("version_tfcard")){
					version_tfcard = json.getString("version_tfcard");
				}
				if(json.containsKey("clientList")){
					List<String> sqlList = new ArrayList<String>();
					JSONArray jsonArray = new JSONArray();
					try{
						jsonArray = json.getJSONArray("clientList");
					}catch(Exception e){
					}
					num = jsonArray.size();
					if(num > 0){
						for(int i=0;i<jsonArray.size();i++){
							JSONObject sonJson = jsonArray.getJSONObject(i);
							String host = sonJson.getString("host");
							String mac = sonJson.getString("mac");
							String ip = sonJson.getString("ip");
							
							sqlList.add("insert into bp_report(sn,host,mac,ip,create_date) values('"+routersn+"','"+host+"','"+mac+"','"+ip+"',now())");
						}
						Db.batch(sqlList, sqlList.size());
					}
				}
				
				String sql = "update bp_device set name=?,online_num=?,online_type=?,ip=?,report_date=now(),router_version=?,script_version=?,data_version=? where router_sn=?";
				Db.update(sql, new Object[]{wifiname,num,json.get("wantype"),json.get("ip"),sys_version,version_code_all,version_tfcard,routersn});
			}else{
				
			}
		}catch(Exception e){
			logger.error("在线上报失败", e);
		}
	}
	//{"sn":"8bc182ef277cbf4e","userLog":[{"dates":"20140819170544","rid":"","mac":"74:e5:0b:f4:c8:d0"}]}
	private boolean statistics(HttpServletRequest request, HttpServletResponse response){
		boolean isSuccess = false;
		try{
			System.err.println(request.getParameter("json"));
			JSONObject json = JSONObject.parseObject(request.getParameter("json"));
			String routerSN = json.getString("sn");
			final JSONArray jsonArray = json.getJSONArray("userLog");
			final Object[][] params = new Object[jsonArray.size()][5];
			for(int i=0;i<jsonArray.size();i++){
				JSONObject sonJson = jsonArray.getJSONObject(i);
				Date accessDate = new Date();
				try {
					accessDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(sonJson.getString("dates"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
	//			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(accessDate);
				int index = sonJson.getString("rid").lastIndexOf("_");
				String type = "";
				int id = -1;
				if(index != -1){
					type = sonJson.getString("rid").substring(0, index);
					try{
						id = Integer.parseInt(sonJson.getString("rid").substring(index+1));
					}catch(Exception e){
					}
				}else{
					type = sonJson.getString("rid");
				}
				params[i] = new Object[]{routerSN,sonJson.getString("mac"),id,type,new Timestamp(accessDate.getTime())};
			}
			isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
				int[] changeRows = Db.batch("insert into bp_statistics_all(router_sn,client_mac,statistics_id,statistics_type,access_date,create_date) "
					+ "values(?,?,?,?,?,now()) ", params, jsonArray.size());
				for(int i=0;i<changeRows.length;i++){
					if(changeRows[i] < 1){
						return false;
					}
				}
				return true;
			}});
			logger.warn(routerSN+">>>>>>>>>>>>>>>>>>>>>>>>>>"+isSuccess);
		}catch(Exception e){
			e.printStackTrace();
		}
		return isSuccess;
	}
	
}
