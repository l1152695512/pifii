package com.yf.base.actions.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GpsData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String[] phones;
	
	@Override
	public String execute() throws Exception {
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from (");
		getListSql.append("select p.phone,p.name,l.location_x,l.location_y,DATE_FORMAT(l.upload_date,'%Y-%m-%d %H:%i:%s') upload_date  ");
		getListSql.append("from bp_person_tbl p left join bp_phone_location_tbl l on (p.phone = l.phone_imsi) ");
		getListSql.append("where p.phone in ("+arrayToSQLIn(phones)+") ");
		getListSql.append("union ");
		getListSql.append("select p.phone,p.name,c.locationX location_x,c.locationY location_y,DATE_FORMAT(rfid.upload_time,'%Y-%m-%d %H:%i:%s') upload_date  ");
		getListSql.append("from bp_person_tbl p join bp_community_tbl c on (p.communityId = c.id) ");
		getListSql.append("left join bp_phone_rfid_location_tbl rfid on (p.phone = rfid.phone) ");
		getListSql.append("where p.phone in ("+arrayToSQLIn(phones)+") ");
		getListSql.append("order by upload_date desc) a ");
		getListSql.append("group by phone ");
		
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}
	
	private String arrayToSQLIn(String[] params){
		if(null == params){
			return "''";
		}
		StringBuilder strB = new StringBuilder();
		for(int i=0;i<params.length;i++){
			strB.append("'"+params[i]+"',");
		}
		String sqlIn = strB.toString();
		if(sqlIn.length()>0){
			return sqlIn.substring(0, sqlIn.length()-1);
		}
		return "''";
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String[] getPhones() {
		return phones;
	}
	
	public void setPhones(String[] phones) {
		this.phones = phones;
	}
}
