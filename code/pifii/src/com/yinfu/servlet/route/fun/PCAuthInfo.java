package com.yinfu.servlet.route.fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class PCAuthInfo {
	private String routersn;
	private String clientMac;
	
	public PCAuthInfo(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		JSONObject info = getAuthInfo();
		//info.put("messages", getAdvInfo());
		return info.toString();
	}
	
	private JSONObject getAuthInfo(){
		JSONObject json = new JSONObject();
		List<String> params = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select tag,date_format(auth_date,'%Y-%m-%d %H:%i:%s') auth_date ");
		sql.append("from bp_auth ");
		sql.append("where auth_type='phone' ");
		if(StringUtils.isNotBlank(routersn)){
			sql.append("and router_sn =? ");
			params.add(routersn);
		}
		if(StringUtils.isNotBlank(clientMac)){
			sql.append("and client_mac =? ");
			params.add(clientMac);
		}
		sql.append("order by auth_date desc ");
		Record rec = Db.findFirst(sql.toString(), params.toArray());
		if(null == rec){
			json.put("tag", "");
			json.put("auth_date", "");
		}else{
			json.put("tag", rec.get("tag"));
			json.put("auth_date", rec.get("auth_date"));
		}
		return json;
	}
}
