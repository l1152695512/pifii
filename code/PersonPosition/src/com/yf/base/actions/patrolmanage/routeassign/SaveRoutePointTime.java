package com.yf.base.actions.patrolmanage.routeassign;

import java.util.UUID;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveRoutePointTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String routePointId;
	private String routeTimeId;
	private String startTime;
	private String endTime;
	
	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			sql.append("update bp_fine_route_point_time_tbl set start_time=?,end_time=?,update_time=now() where id=? ");
			Object[] params = new Object[3];
			params[0] = startTime;
			params[1] = endTime;
			params[2] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			sql.append("insert into bp_fine_route_point_time_tbl(id,route_point_id,route_time_id,start_time,end_time,update_time) ");
			sql.append("VALUES(?,?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[5];
			params[0] = id;
			params[1] = routePointId;
			params[2] = routeTimeId;
			params[3] = startTime;
			params[4] = endTime;
			if(dbhelper.insert(sql.toString(),params)){
				return super.execute();
			}
		}
		return "failure";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoutePointId() {
		return routePointId;
	}
	public void setRoutePointId(String routePointId) {
		this.routePointId = routePointId;
	}
	public String getRouteTimeId() {
		return routeTimeId;
	}
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
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

}
