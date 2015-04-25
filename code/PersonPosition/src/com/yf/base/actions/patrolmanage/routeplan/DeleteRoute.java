package com.yf.base.actions.patrolmanage.routeplan;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.mapposition.routemanage.RouteUtilService;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DeleteRoute extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String routeId;
	
	@Override
	public String execute() throws Exception {
//		List<String> sqls = new ArrayList<String>();
//		sqls.add("delete from bp_fine_route_tbl where id='"+routeId+"' ");
//		sqls.add("delete from bp_fine_route_point_tbl where route_id='"+routeId+"' ");
//		if(dbhelper.executeFor(sqls)){
//			return super.execute();
//		}
		if(RouteUtilService.removeRoute(routeId,"bp_fine_route_tbl","bp_fine_route_point_tbl","bp_fine_route_time_tbl","bp_fine_route_point_time_tbl","bp_fine_route_time_assign_tbl")){
			return super.execute();
		}
		return "failure";
	}

	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
}
