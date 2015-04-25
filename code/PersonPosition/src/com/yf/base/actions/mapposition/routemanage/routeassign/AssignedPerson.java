package com.yf.base.actions.mapposition.routemanage.routeassign;

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
	private String routeTimeId;
	
	@Override
	public String execute() throws Exception {
		StringBuffer strB = new StringBuffer();
		strB.append("select person_id ");
		strB.append("from bp_coarse_route_time_assign_tbl ");
		strB.append("where route_time_id = '");
		strB.append(routeTimeId);
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

	public String getRouteTimeId() {
		return routeTimeId;
	}
	
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
	}
	
}
