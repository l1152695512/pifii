package com.yf.base.actions.datastatistics.warn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class WarnAreaInfoData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	
	private String personId;
	private String phone;
	private String areaId;
	private String areaTimeId;
	private String date;
	private String areaType;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select DATE_FORMAT(prl.upload_time,'%H:%i:%s') time,bct.locationX,bct.locationY ");
		dataSql.append("from bp_fine_area_time_tbl t ");
		dataSql.append("join bp_phone_rfid_location_tbl prl on (");
		dataSql.append("t.id = '"+areaTimeId+"' and prl.phone = '"+phone+"' and date(prl.upload_time) = '"+date+"' ");
		dataSql.append("and TIME(prl.upload_time) >= t.start_time and TIME(prl.upload_time) <= t.end_time) ");
		dataSql.append("join bp_card_tbl bct on (bct.card_mark = prl.card_id) ");
		dataSql.append("order by prl.upload_time ");
		System.err.println(dataSql.toString());
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(dataSql.toString());
		List<Map<String,Object>> warnArea = Commons.getWarnAreas(dbhelper).get(areaId);
		List<Map<String,Object>> returnData = new ArrayList<Map<String,Object>>();
		Iterator<Map<String,Object>> ite = dataList.iterator();
		//将违法/合法的时间点转换为时间区间
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			int pointPosition = Commons.checkPointInWarnArea(warnArea,(Double)rowData.get("locationX"),(Double)rowData.get("locationY"));
			if(("9".equals(areaType) && 1 == pointPosition) 
					|| ("10".equals(areaType) && -1 == pointPosition)){//非法位置
				if(returnData.size() == 0 || !"".equals(returnData.get(returnData.size()-1).get("end_time"))){
					Map<String,Object> data = new HashMap<String,Object>();
					data.put("start_time", rowData.get("time"));
					data.put("end_time", "");
					returnData.add(data);
				}
			}else{
				if(returnData.size() > 0 && "".equals(returnData.get(returnData.size()-1).get("end_time"))){
					returnData.get(returnData.size()-1).put("end_time",rowData.get("time"));
				}
			}
		}
		if(returnData.size() > 0 && "".equals(returnData.get(returnData.size()-1).get("end_time"))){
			returnData.get(returnData.size()-1).put("end_time",dataList.get(dataList.size()-1).get("time"));
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", returnData.toArray());
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
	
	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAreaId() {
		return areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaTimeId() {
		return areaTimeId;
	}
	
	public void setAreaTimeId(String areaTimeId) {
		this.areaTimeId = areaTimeId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getAreaType() {
		return areaType;
	}
	
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	
}
