package com.yf.base.actions.mapposition.routemanage.routeassign;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.mapposition.routemanage.RouteUtilService;

public class DeleteRouteTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String[] ids;

	@Override
	public String execute() throws Exception {
		if(RouteUtilService.removeRouteTimes(ids,"bp_coarse_route_time_tbl","bp_coarse_route_time_assign_tbl","bp_coarse_route_point_time_tbl")){
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
