package com.yf.base.actions.patrolmanageold.routeassign;

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
	private String routeId;
	
	@Override
	public String execute() throws Exception {
		List<String> sqls = new ArrayList<String>();
		for(int i=0;i<personIds.length;i++){
			if(!"".equals(personIds[i])){
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				sqls.add("insert into bp_route_person_assign_tbl(id,route_id,person_id,add_date) values('"+id+"','"+routeId+"','"+personIds[i]+"',now()) ");
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
	
	public String getRouteId() {
		return routeId;
	}
	
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
}
