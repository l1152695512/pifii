package com.yf.base.actions.map.area;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyMap extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String dependentId;
	private String dependentName;
	private String id;
	private String name;
//	private String map;
	private String locationX;
	private String locationY;
	
	private String districtId;
	private String areaType;
	//用于添加小区时，到数据库查找对应的区名称，用省名称和市名称主要防止查找到的区名称重复
	private String provinceName;
	private String cityName;
	private String districtName;
	private String userId;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(StringUtils.isBlank(id)  && StringUtils.isNotBlank(dependentId)){//依赖于其他小区的创建
			StringBuffer sql = new StringBuffer();
			sql.append("select bct.districtId,bct.name community_name,bct.user_id,bct.locationX,bct.locationY,bct.area_type,yrt1.name district_name,yrt2.name city_name,yrt3.name province_name ");
			sql.append("from bp_community_tbl bct join yw_region_tbl yrt1 on (bct.districtId = yrt1.rid) ");
			sql.append("join yw_region_tbl yrt2 on (yrt1.pid = yrt2.rid) ");
			sql.append("join yw_region_tbl yrt3 on (yrt2.pid = yrt3.rid) ");
			sql.append("where bct.id='"+dependentId+"' ");
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				userId = null==data.get("user_id")?"":data.get("user_id").toString();
				System.err.println("userId="+userId);
				districtId = null==data.get("districtId")?"":data.get("districtId").toString();
				dependentName = null==data.get("community_name")?"":data.get("community_name").toString();
				locationX = null==data.get("locationX")?"":data.get("locationX").toString();
				locationY = null==data.get("locationY")?"":data.get("locationY").toString();
				districtName = null==data.get("district_name")?"":data.get("district_name").toString();
				cityName = null==data.get("city_name")?"":data.get("city_name").toString();
				provinceName = null==data.get("province_name")?"":data.get("province_name").toString();
			}else{
				return "failure";
			}
		}else if(StringUtils.isNotBlank(id)){//修改小区
			StringBuffer sql = new StringBuffer();
			sql.append("select bct.dependent_community,bct.districtId,bct.user_id,bct.name community_name,bct.locationX,bct.locationY,bct.area_type,yrt1.name district_name,yrt2.name city_name,yrt3.name province_name ");
			sql.append("from bp_community_tbl bct join yw_region_tbl yrt1 on (bct.districtId = yrt1.rid) ");
			sql.append("join yw_region_tbl yrt2 on (yrt1.pid = yrt2.rid) ");
			sql.append("join yw_region_tbl yrt3 on (yrt2.pid = yrt3.rid) ");
			sql.append("where bct.id='"+id+"' ");
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				userId = null==data.get("user_id")?"":data.get("user_id").toString();
				dependentId = null==data.get("dependent_community")?"":data.get("dependent_community").toString();
				districtId = null==data.get("districtId")?"":data.get("districtId").toString();
				name = null==data.get("community_name")?"":data.get("community_name").toString();
//				map = null==data.get("map")?"":data.get("map").toString();
				locationX = null==data.get("locationX")?"":data.get("locationX").toString();
				locationY = null==data.get("locationY")?"":data.get("locationY").toString();
				districtName = null==data.get("district_name")?"":data.get("district_name").toString();
				cityName = null==data.get("city_name")?"":data.get("city_name").toString();
				provinceName = null==data.get("province_name")?"":data.get("province_name").toString();
				areaType = null==data.get("area_type")?"1":data.get("area_type").toString();
			}else{
				return "failure";
			}
		}else{
//			StringBuffer sql = new StringBuffer();
//			sql.append("select r3.rid ");
//			sql.append("from yw_region_tbl r1 join yw_region_tbl r2 on (r2.name = ? and r2.pid = r1.rid) ");
//			sql.append("join yw_region_tbl r3 on (r3.name like ? and r3.pid = r2.rid) ");
//			sql.append("where r1.name = ? ");
//			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(
//					sql.toString(),new Object[]{cityName,districtName,provinceName});
//			if(dataList.size()>0){
//				districtId = dataList.get(0).get("rid").toString();
//			}else if(StringUtils.isBlank(districtId)){
//				districtId = districtName;
//			}
		}
		return super.execute();
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
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

//	public String getMap() {
//		return map;
//	}
//	
//	public void setMap(String map) {
//		this.map = map;
//	}
	
	public String getDistrictId() {
		return districtId;
	}
	
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	
	public String getAreaType() {
		return areaType;
	}
	
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	
	public String getDistrictName() {
		return districtName;
	}
	
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
