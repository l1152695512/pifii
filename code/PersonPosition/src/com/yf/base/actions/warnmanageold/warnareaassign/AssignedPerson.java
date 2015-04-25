package com.yf.base.actions.warnmanageold.warnareaassign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class AssignedPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String areaTimeId;
	
	@Override
	public String execute() throws Exception {
		StringBuffer strB = new StringBuffer();
		strB.append("select person_id ");
		strB.append("from bp_fine_area_time_assign_tbl ");
		strB.append("where area_time_id = '");
		strB.append(areaTimeId);
		strB.append("' ");
		List<?> dataList = dbhelper.getMapListBySql(strB.toString());
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

	public String getAreaTimeId() {
		return areaTimeId;
	}
	
	public void setAreaTimeId(String areaTimeId) {
		this.areaTimeId = areaTimeId;
	}
	
}
