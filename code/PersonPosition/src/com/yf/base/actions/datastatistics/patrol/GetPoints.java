package com.yf.base.actions.datastatistics.patrol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetPoints extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String pointId;
	
	@Override
	public String execute() throws Exception {
		String sql = "select id,name from bp_fine_route_point_tbl where route_id='"+pointId+"' ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("values", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		System.out.println(sql);
		return "data";
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

}
