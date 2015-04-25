package com.yf.base.actions.map.area;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveMapInfo extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String userId;
	private String userIdField;
	private String districtId;
	private String name;
	private String map;
	private String locationX;
	private String locationY;
	private String areaType;
	private String dependentId;
	private String dependentName;
	
	public String execute(){
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
//			deleteOldMapIfExist();
			sql.append("update bp_community_tbl ");
			sql.append("set name = ?,area_type=? ");
			sql.append("where id = ? ");
			if(dbhelper.update(sql.toString(),new Object[]{name,areaType,id})){
				return SUCCESS;
			}
		}else{
			sql.append("insert into bp_community_tbl(id,user_id,districtId,name,locationX,locationY,createDate,area_type,dependent_community) ");
			sql.append("VALUES(?,?,?,?,?,?,now(),?,?) ");
			Object[] params = new Object[8];
			params[0] = UUID.randomUUID().toString().replaceAll("-", "");
			params[1] = userId;
			if(StringUtils.isBlank(userId)){
				params[1] = userIdField;
			}
			params[2] = districtId;
			if(StringUtils.isNotBlank(dependentName)){
				name = dependentName+"("+name+")";
			}
			params[3] = name;
//			params[3] = map;
			params[4] = locationX;
			params[5] = locationY;
			params[6] = areaType;
			params[7] = dependentId;
			if(dbhelper.insert(sql.toString(),params)){
				return SUCCESS;
			}
		}
		return "failure";
	}

	@SuppressWarnings("unchecked")
	private void deleteOldMapIfExist(){
		String sql = "select map from bp_community_tbl where id='"+id+"' ";
		List<Map<String,String>> dataList = (List<Map<String, String>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,String> data = dataList.get(0);
			String oldMap = null==data.get("map")?"":data.get("map").toString();
			if(StringUtils.isNotBlank(oldMap) && !oldMap.equals(map)){
//				String str = SaveMapInfo.class.getClassLoader().getResource("").getPath();
//				String localPath = str.substring(1, str.indexOf("WEB-INF"));
				String localPath = GlobalVar.TOOLSPATH+"/";
				String mapAbsolutePath = localPath+oldMap;
				File mapFile = new File(mapAbsolutePath);
				if(mapFile.exists()){
					mapFile.delete();
				}
			}
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getUserIdField() {
		return userIdField;
	}
	
	public void setUserIdField(String userIdField) {
		this.userIdField = userIdField;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getDistrictId() {
		return districtId;
	}
	
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getMap() {
		return map;
	}
	
	public void setMap(String map) {
		this.map = map;
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
	
	public String getAreaType() {
		return areaType;
	}
	
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	
	public String getDependentId() {
		return dependentId;
	}
	
	public void setDependentId(String dependentId) {
		this.dependentId = dependentId;
	}
	
	public String getDependentName() {
		return dependentName;
	}
	
	public void setDependentName(String dependentName) {
		this.dependentName = dependentName;
	}
}
