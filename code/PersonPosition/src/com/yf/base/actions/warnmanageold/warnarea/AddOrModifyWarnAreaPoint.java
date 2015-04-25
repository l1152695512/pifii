package com.yf.base.actions.warnmanageold.warnarea;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyWarnAreaPoint extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String warnAreaId;
	private String name;
	private String locationX;
	private String locationY;
	private String previousPoint;
	private String nextPoint;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select name,location_x,location_y,previous_point_id,next_point_id " +
					"from bp_fine_area_point_tbl " +
					"where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				name = null==data.get("name")?"":data.get("name").toString();
				locationX = null==data.get("location_x")?"":data.get("location_x").toString();
				locationY = null==data.get("location_y")?"":data.get("location_y").toString();
				previousPoint = null==data.get("previous_point_id")?"":data.get("previous_point_id").toString();
				nextPoint = null==data.get("next_point_id")?"":data.get("next_point_id").toString();
			}else{
				return "failure";
			}
		}else{
			String sql = "select id from bp_fine_area_point_tbl where area_id='"+warnAreaId+"' and (next_point_id is null or trim(next_point_id) = '') ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				previousPoint = null==data.get("id")?"":data.get("id").toString();
			}
		}
		return super.execute();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getPreviousPoint() {
		return previousPoint;
	}

	public void setPreviousPoint(String previousPoint) {
		this.previousPoint = previousPoint;
	}

	public String getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(String nextPoint) {
		this.nextPoint = nextPoint;
	}

}
