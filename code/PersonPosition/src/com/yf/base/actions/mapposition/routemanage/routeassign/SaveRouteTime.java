package com.yf.base.actions.mapposition.routemanage.routeassign;

import java.util.List;
import java.util.Map;
import java.util.UUID;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveRouteTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String routeId;
	private String id;
	private String name;
	private String startTime;
	private String endTime;
	private String isUsed;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			String sqlOldData = "select start_time,end_time from bp_coarse_route_time_tbl where id = '"+id+"'";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sqlOldData);
			if(dataList.size() > 0){
				Map<String,Object> rowData = dataList.get(0);
				String oldStartTime = rowData.get("start_time").toString();
				String oldEndTime = rowData.get("end_time").toString();
				if(!oldStartTime.equals(startTime) || !oldEndTime.equals(endTime)){//修改了路线时间，则删除路线点的时间段设置
					String sqlDelete = "delete from bp_coarse_route_point_time_tbl where route_time_id = '"+id+"'";
					dbhelper.delete(sqlDelete);
				}
			}
			sql.append("update bp_coarse_route_time_tbl set name=?,start_time=?,end_time=?,is_used=? where id=? ");
			Object[] params = new Object[5];
			params[0] = name;
			params[1] = startTime;
			params[2] = endTime;
			params[3] = "1".equals(isUsed);
			params[4] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			sql.append("insert into bp_coarse_route_time_tbl(id,route_id,name,start_time,end_time,is_used,add_date) ");
			sql.append("VALUES(?,?,?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[6];
			params[0] = id;
			params[1] = routeId;
			params[2] = name;
			params[3] = startTime;
			params[4] = endTime;
			params[5] = "1".equals(isUsed);
			if(dbhelper.insert(sql.toString(),params)){
				return super.execute();
			}
		}
		return "failure";
	}
	
	public String getRouteId() {
		return routeId;
	}
	
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getIsUsed() {
		return isUsed;
	}
	
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	
}
