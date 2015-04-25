package com.yf.base.actions.patrolmanage.routeassign;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.mapposition.routemanage.RouteUtilService;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DeleteRouteTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String[] ids;

	@Override
	public String execute() throws Exception {
//		List<String> sqlList = new ArrayList<String>();
//		for(int i=0;i<ids.length;i++){
//			if(!"".equals(ids[i])){
//				sqlList.add("delete from bp_fine_route_time_tbl where id='"+ids[i]+"'");
//			}
//		}
//		if(dbhelper.executeFor(sqlList)){
//			return super.execute();
//		}
		if(RouteUtilService.removeRouteTimes(ids,"bp_fine_route_time_tbl","bp_fine_route_time_assign_tbl","bp_fine_route_point_time_tbl")){
			return super.execute();
		}
		return "failure";
	}

	public String[] getIds() {
		return ids;
	}
	
	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
