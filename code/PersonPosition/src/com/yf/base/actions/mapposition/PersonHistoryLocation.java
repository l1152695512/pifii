package com.yf.base.actions.mapposition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PersonHistoryLocation extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String id;
	private String startTime;
	private String endTime;
	
	@Override
	public String execute() throws Exception {
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select location_x,location_y,upload_date ");
		getListSql.append("from bp_phone_location_tbl ");
		getListSql.append("where phone_imsi = '"+id+"' and upload_date >= '"+startTime+"' and upload_date <= '"+endTime+"' ");
		getListSql.append("order by upload_date ");
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString());
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
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
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
