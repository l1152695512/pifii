package com.yf.base.actions.patrolmanage.routeassign;

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
		dbhelper.delete("delete from bp_fine_route_time_assign_tbl where route_time_id = '"+routeTimeId+"' ");
		List<String> sqls = new ArrayList<String>();
		for(int i=0;i<personIds.length;i++){
			if(!"".equals(personIds[i])){//当前台传如的数组为空时，这里的长度也会是1，此时会报错，长度变成1的原因是：即使前台数组长度为0，在请求数据时一样会传一个该参数（数组的长度有多少个就会传多少个这样的参数），
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				sqls.add("insert into bp_fine_route_time_assign_tbl(id,route_time_id,person_id,add_date) values('"+id+"','"+routeTimeId+"','"+personIds[i]+"',now()) ");
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
