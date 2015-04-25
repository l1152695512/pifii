package com.yf.base.actions.patrolmanageold.routeassign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class UnassignedPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String routeId;
	
	@Override
	public String execute() throws Exception {
		StringBuffer strB = new StringBuffer();
		strB.append("select p.id,p.name,p.photo,p.age,p.phone,p.description ");
		strB.append("from bp_person_tbl p join bp_fine_route_tbl r on (r.id = '");
		strB.append(routeId);
		strB.append("' and p.communityId = r.community_id) ");
		strB.append("where 1=1 ");
//		strB.append("and p.dictionaryId = '2'" );
		strB.append("and p.id not in (select person_id from bp_route_person_assign_tbl where route_id = '");
		strB.append(routeId);
		strB.append("' )");
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

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
}
