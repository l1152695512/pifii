package com.yf.base.actions.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.util.DBUtil;

public class ComboData extends ActionSupport  {

	private String jsonString;
	private String typeName;


	@Override
	public String execute() throws Exception {
		JSONArray arry=new JSONArray();
		JSONObject o=null;
		Map map = new HashMap();
		String sql = "select key_name,key_value from sys_dictionary_tbl ";
		if(this.typeName != null){
			sql += " where type_name='"+this.typeName+"'";
		}
		List list = new DBUtil().getMapListBySql(sql);
		for (int i=0;i<list.size();i++) {
			o=new JSONObject();
			map = (Map)list.get(i);
    		o.put("code",map.get("KEY_VALUE"));
    		o.put("name",map.get("KEY_NAME"));
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
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
