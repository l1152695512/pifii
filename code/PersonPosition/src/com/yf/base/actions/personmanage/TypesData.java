package com.yf.base.actions.personmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class TypesData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String type;
	
	@Override
	public String execute() throws Exception {
		String sql = "select dic_id id,key_value name from sys_dictionary_tbl where type_name= '"+type+"' order by key_name ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("values", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
