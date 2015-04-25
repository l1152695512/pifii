package com.yf.base.actions.warnmanageold.warnareaassign;

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
	private String areaId;
	
	@Override
	public String execute() throws Exception {
		StringBuffer strB = new StringBuffer();
		strB.append("select p.id,p.name,p.photo,p.age,p.phone,p.description ");
		strB.append("from bp_person_tbl p join bp_fine_area_tbl a on (a.id = '");
		strB.append(areaId);
		strB.append("' and p.communityId = a.community_id) ");
		strB.append("where 1=1 ");
//		strB.append("and p.dictionaryId = '2'" );
		strB.append("and p.id not in (select person_id from bp_warn_area_person_assign_tbl where warn_area_id = '");
		strB.append(areaId);
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

	public String getAreaId() {
		return areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
}
