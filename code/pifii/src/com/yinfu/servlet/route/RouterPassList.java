package com.yinfu.servlet.route;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.setting.SynWhitelist;


public class RouterPassList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(RouterPassList.class);
	
//	private String routersn;//设备标识
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		String routersn = request.getParameter("routersn");
		JSONObject json = new JSONObject();
		JSONArray macArray = new JSONArray();
		JSONArray auth = new JSONArray();
		JSONArray group = new JSONArray();
		JSONArray passlist = new JSONArray();
		if(StringUtils.isNotEmpty(routersn)){
			//查询开启群组功能的白名单
//			String mac_sql = "select mac from bp_pass_list where sn= ? group by mac";
			StringBuffer sql = new StringBuffer();
			sql.append("select a.mac from bp_pass_list a ");
			sql.append("left join bp_device b ");
			sql.append("on a.sn=b.router_sn ");
			sql.append("left join bp_shop c ");
			sql.append("on b.shop_id=c.id ");
			sql.append("where c.gstatus=1 and a.sn=? group by a.mac");
			List<Record> macList = Db.find(sql.toString(), new Object[]{routersn});
			for(Record rd : macList){
				macArray.add(rd.getStr("mac"));
			}
			List<Record> macWhitelist = SynWhitelist.getMacWhitelist(routersn);
			addPasslistMac(macWhitelist, macArray);//添加永久性的白名单mac
			passlist = listToArray(macWhitelist);
			StringBuffer sqlMac = new StringBuffer();
			sqlMac.append("select a.mac,a.is_me ");
			sqlMac.append("from bp_pass_list a ");
			sqlMac.append("left join bp_device b on a.sn=b.router_sn ");
			sqlMac.append("left join bp_shop c on (b.shop_id=c.id) ");
			sqlMac.append("where a.sn=? and (a.is_me or c.gstatus=1) ");
			sqlMac.append("group by a.mac ");
			List<Record> newMacList = Db.find(sqlMac.toString(), new Object[]{routersn});
			auth = getMac(newMacList,"1");
			group = getMac(newMacList,"0");
		}
		json.put("passlist", passlist);
		json.put("auth", auth);
		json.put("group", group);
		json.put("member", macArray);
		
		response.getWriter().print(json.toJSONString());
	}
	
	private JSONArray getMac(List<Record> macList,String joinmark){
		JSONArray macArray = new JSONArray();
		Iterator<Record> ite = macList.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			if(joinmark.equals(rec.get("is_me").toString())){
				macArray.add(rec.getStr("mac"));
			}
		}
		return macArray;
	}
	
	private void addPasslistMac(List<Record> settingMacs,JSONArray macs){//兼容之前的白名单功能
		Iterator<Record> ite = settingMacs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			if(!macs.contains(rec.get("content").toString())){
				macs.add(rec.get("content").toString());
			}
		}
	}
	
	private JSONArray listToArray(List<Record> settingMacs){
		JSONArray macs = new JSONArray();
		Iterator<Record> ite = settingMacs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			macs.add(rec.getStr("content"));
		}
		return macs;
	}
	
	
}
