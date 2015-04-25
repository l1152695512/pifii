package com.yf.base.actions.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.util.DBUtil;

public class ComboDic extends ActionSupport  {

	private String jsonString;

	@Override
	public String execute() throws Exception {
		JSONArray arry=new JSONArray();
		JSONObject o=null;
		Map map = new HashMap();
		String sql = "select type_name from sys_dictionary_tbl group by type_name";
		List list = new DBUtil().getMapListBySql(sql);
		for (int i=0;i<list.size();i++) {
			o=new JSONObject();
			map = (Map)list.get(i);
    		o.put("code",map.get("TYPE_NAME"));
    		o.put("name",map.get("TYPE_NAME"));
    		arry.add(o);	
		}
		this.jsonString = arry.toString();
		return "comboData";
	}

	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

}
