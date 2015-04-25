package com.yf.base.actions.mapposition.routemanage.routeplan;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.mapposition.routemanage.RouteUtilService;

public class DeleteRoute extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String routeId;
	
	@Override
	public String execute() throws Exception {
		if(RouteUtilService.removeRoute(routeId,"bp_coarse_route_tbl","bp_coarse_route_point_tbl","bp_coarse_route_time_tbl","bp_coarse_route_point_time_tbl","bp_coarse_route_time_assign_tbl")){
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
