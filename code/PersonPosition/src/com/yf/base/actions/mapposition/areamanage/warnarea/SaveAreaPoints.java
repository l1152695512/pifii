package com.yf.base.actions.mapposition.areamanage.warnarea;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveAreaPoints extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String areaId;
	private String[] points;

	@Override
	public String execute() throws Exception {
		String deleteOldPointsSql = "delete from bp_coarse_area_point_tbl where area_id = '"+areaId+"' ";
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
				String sql = "insert into bp_coarse_area_point_tbl(id,area_id,previous_point_id,next_point_id,location_x,location_y,add_date) values('"+
							myId+"','"+areaId+"','"+previousPointId+"','"+nextPointId+"','"+location[0]+"','"+location[1]+"',now())";
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
	
	public String getAreaId() {
		return areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public String[] getPoints() {
		return points;
	}
	
	public void setPoints(String[] points) {
		this.points = points;
	}
}
