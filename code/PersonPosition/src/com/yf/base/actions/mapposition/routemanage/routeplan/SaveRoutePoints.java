package com.yf.base.actions.mapposition.routemanage.routeplan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveRoutePoints extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String routeId;
	private String[] points;

	@Override
	public String execute() throws Exception {
		String deletePointsTimeSql = "delete from bp_coarse_route_point_time_tbl where route_point_id in (select id from bp_coarse_route_point_tbl where route_id = '"+routeId+"')";
		dbhelper.delete(deletePointsTimeSql);
		String deleteOldPointsSql = "delete from bp_coarse_route_point_tbl where route_id = '"+routeId+"' ";
		dbhelper.delete(deleteOldPointsSql);//只有当删除了数据才会返回true，所以此处不能当该方法返回true时执行下面的代码，有时候可能本身就没有数据
		List<String> insertPoints = new ArrayList<String>();
		String previousPointId = "";
		String nextPointId = UUID.randomUUID().toString().replaceAll("-", "");
		for(int i=0;i<points.length;i++){
			if(!"".equals(points[i])){
				String myId = nextPointId;
				if(i == points.length-1){
					nextPointId = "";
				}else{
					nextPointId = UUID.randomUUID().toString().replaceAll("-", "");
				}
				String[] location = points[i].split(",");
				String sql = "insert into bp_coarse_route_point_tbl(id,route_id,previous_point_id,next_point_id,location_x,location_y,add_date) values('"+
						myId+"','"+routeId+"','"+previousPointId+"','"+nextPointId+"','"+location[0]+"','"+location[1]+"',now())";
				System.err.println(sql);
				insertPoints.add(sql);
				previousPointId = myId;
			}
		}
		if(dbhelper.executeFor(insertPoints)){
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
	
	public String[] getPoints() {
		return points;
	}
	
	public void setPoints(String[] points) {
		this.points = points;
	}
}
