package com.yinfu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.RouterHelper;


public class Extension extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(Extension.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		response.setCharacterEncoding("UTF-8");
		String fn = request.getParameter("fn");
		String sn = request.getParameter("sn");
		logger.info("routerSn="+sn+",fn="+fn);
		if(StringUtils.isNotEmpty(fn) && StringUtils.isNotEmpty(sn)){
			if("timeout_get".equals(fn)){
				String sql = "select time_out from bp_device where router_sn= ?";
				Record rd = Db.findFirst(sql,new Object[]{sn});
				if(rd != null){
					json.put("timeout", rd.get("time_out"));
				}else{
					json.put("timeout", "0");
				}
			}else if("timeout_set".equals(fn)){
				String timeout = request.getParameter("timeout");
				String sql = "update bp_device set time_out=? where router_sn= ?";
				int i = Db.update(sql, new Object[]{timeout,sn});
				json.put("status", i);
			}else if("online".equals(fn)){
				int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔，要考虑网络延迟
				String sql = "select 1 from bp_device where router_sn = ? and date_add(report_date, interval ? second) > now()";
				Record rd = Db.findFirst(sql, new Object[] { sn, interval });
				if(rd != null){
					json.put("online", "true");
				}else{
					json.put("online", "false");
				}
			}else if("task_status".equals(fn)){
				 JSONObject jsons=new JSONObject();
				 jsons.put("cmd", "status");
				 JSONObject obj=new JSONObject();
				 jsons.put("param", obj);
				 System.out.println(json.toString());
				 
				 try{
					 Record rd = getAccount(sn);
					 String token = RouterHelper.routerToken(rd.getStr("remote_account"), rd.getStr("remote_pass"));
					 String getData = RouterHelper.download(jsons.toString(),token);
					 json = JSONObject.parseObject(getData);
				 }catch(Exception e){
					 json.put("status", "400");
				 }
			}
		}
		
		response.getWriter().print(json.toJSONString());
	}
	
	private Record getAccount(String sn){
		Record rec = Db.findFirst("select name,ifnull(remote_account,'') remote_account,ifnull(remote_pass,'') remote_pass from bp_device where router_sn=? ", new Object[]{sn});
		if(null == rec){
			rec = new Record();
		}
		if(null == rec.get("remote_account") || StringUtils.isBlank(rec.getStr("remote_account"))){
			rec.set("remote_account", sn+"@pifii.com");
		}
		
		if(null == rec.get("remote_pass") || StringUtils.isBlank(rec.getStr("remote_pass"))){
			rec.set("remote_pass", "88888888");
		}
		return rec;
	}

	
}
