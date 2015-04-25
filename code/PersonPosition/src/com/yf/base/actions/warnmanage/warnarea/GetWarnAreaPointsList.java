package com.yf.base.actions.warnmanage.warnarea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetWarnAreaPointsList extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String id;
	private String warnAreaId;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select id,name,previous_point_id,next_point_id " +
				"from bp_fine_area_point_tbl " +
				"where area_id='"+warnAreaId +
				"' order by previous_point_id ";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		//排序
		List<Map<String,Object>> sortDataList = new ArrayList<Map<String,Object>>();
		int removeWarnAreaPointIndex = -1;
		if(dataList.size()>0){
			sortDataList.add(dataList.get(0));
			if(dataList.get(0).get("id").toString().equals(id)){
				removeWarnAreaPointIndex = 0;
			}
			dataList.remove(0);
		}
		while(dataList.size()>0){
			String nextPointId = sortDataList.get(sortDataList.size()-1).get("next_point_id").toString();
			if(StringUtils.isBlank(nextPointId)){
				break;
			}
			Map<String,Object> nextPoint = findPoint(dataList,nextPointId);
			if(null == nextPoint){
				break;
			}
			if(removeWarnAreaPointIndex == -1 && nextPoint.get("id").toString().equals(id)){
				removeWarnAreaPointIndex = sortDataList.size();
			}
			sortDataList.add(nextPoint);
		}
		if(removeWarnAreaPointIndex != -1){
			sortDataList.remove(removeWarnAreaPointIndex);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("values", sortDataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	private Map<String,Object> findPoint(List<Map<String,Object>> dataList,String pointId){
		Map<String,Object> findData = null;
		for(int i=0;i<dataList.size();i++){
			if(dataList.get(i).get("id").equals(pointId)){
				findData = dataList.get(i);
				dataList.remove(i);
				return findData;
			}
		}
		return findData;
	}
	
	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getWarnAreaId() {
		return warnAreaId;
	}
	
	public void setWarnAreaId(String warnAreaId) {
		this.warnAreaId = warnAreaId;
	}
}
