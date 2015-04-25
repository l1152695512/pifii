package com.yf.base.actions.mapposition.datastatistics.warn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class ComboWarnArea extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	
	@Override
	public String execute() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,name from bp_coarse_area_tbl ");
		List<?> dataList = dbhelper.getMapListBySql(sql.toString());
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
	
}
