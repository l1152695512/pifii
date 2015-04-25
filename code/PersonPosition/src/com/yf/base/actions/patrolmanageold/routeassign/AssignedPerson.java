package com.yf.base.actions.patrolmanageold.routeassign;

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
	private String nodeId;
	
	@Override
	public String execute() throws Exception {
		StringBuffer strB = new StringBuffer();
		strB.append("select distinct a.id,a.person_id,p.name,p.age,p.phone,p.photo,p.description ");
		strB.append("from bp_route_person_assign_tbl a join bp_person_tbl p on (a.person_id = p.id) ");
		strB.append("where a.route_id = '");
		strB.append(nodeId);
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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}
