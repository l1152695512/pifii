package com.yf.base.actions.mapposition.routemanage.routeassign;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveAssignPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String[] personIds;
	private String routeTimeId;
	
	@Override
	public String execute() throws Exception {
		String deleteOldPointsSql = "delete from bp_coarse_route_time_assign_tbl where route_time_id = '"+routeTimeId+"' ";
		dbhelper.delete(deleteOldPointsSql);
		List<String> sqls = new ArrayList<String>();
		for(int i=0;i<personIds.length;i++){
			if(!"".equals(personIds[i])){
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				sqls.add("insert into bp_coarse_route_time_assign_tbl(id,route_time_id,person_id,add_date) values('"+id+"','"+routeTimeId+"','"+personIds[i]+"',now()) ");
			}
		}
		if(dbhelper.executeFor(sqls)){
			return super.execute();
		}
		return "failure";
	}
	
	public String[] getPersonIds() {
		return personIds;
	}
	
	public void setPersonIds(String[] personIds) {
		this.personIds = personIds;
	}
	
	public String getRouteTimeId() {
		return routeTimeId;
	}
	
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
	}
	
}
