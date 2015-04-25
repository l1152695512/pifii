package com.yf.base.actions.mapposition.areamanage.warnarea;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GetAreas extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select id,name,description from bp_coarse_area_tbl order by add_date ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		Iterator<?> ite = dataList.iterator();
		JSONArray jsonArray = new JSONArray();
		while(ite.hasNext()){
			Map<String,Object> rowData = (Map<String,Object>)ite.next();
			JSONObject node = new JSONObject();
			node.put("id", rowData.get("id"));
			node.put("text", null==rowData.get("name")?"":rowData.get("name"));
			node.put("leaf", true);
			node.put("qtip", null==rowData.get("description")?"":rowData.get("description"));
			jsonArray.add(node);
		}
		this.jsonString = jsonArray.toString();
		return "data";
	}
	
	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
}
