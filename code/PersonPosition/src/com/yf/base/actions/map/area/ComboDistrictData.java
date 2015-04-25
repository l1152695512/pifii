package com.yf.base.actions.map.area;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class ComboDistrictData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String provinceName;
	private String cityName;
	
	@Override
	public String execute() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select r3.rid id,r3.name ");
		sql.append("from yw_region_tbl r1 join yw_region_tbl r2 on (r2.name = ? and r2.pid = r1.rid) ");
		sql.append("join yw_region_tbl r3 on (r3.pid = r2.rid) ");
		sql.append("where r1.name = ? ");
		List<?> dataList = dbhelper.getMapListBySql(sql.toString(),new Object[]{cityName,provinceName});
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
	
	public String getProvinceName() {
		return provinceName;
	}
	
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
