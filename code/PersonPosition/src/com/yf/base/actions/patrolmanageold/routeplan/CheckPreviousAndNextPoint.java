package com.yf.base.actions.patrolmanageold.routeplan;

import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class CheckPreviousAndNextPoint extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);

	private String msg;
	private String id;
	private String routeId;
	private String previousPoint;
	private String nextPoint;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isBlank(previousPoint) && StringUtils.isBlank(nextPoint)){
			StringBuffer sql = new StringBuffer();
			sql.append("select count(*) count from bp_fine_route_point_tbl where route_id = '");
			sql.append(routeId);
			sql.append("' ");
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				long count = null==data.get("count")?0:Long.parseLong(data.get("count").toString());
				if(count>0){
					if(StringUtils.isBlank(id) || count > 1){
						this.msg = "添加点的上一个连接点和下一个连接点不能同时为空！";
						return "failure";
					}
				}
			}
		}else if(StringUtils.isBlank(previousPoint)){//成为起始点
			StringBuffer sql = new StringBuffer();
			sql.append("select id from bp_fine_route_point_tbl where route_id = '"+routeId+"' and (previous_point_id is null or trim(previous_point_id) = '') ");
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				String startPointId = null==data.get("id")?"":data.get("id").toString();
				if(!startPointId.equals(nextPoint)){
					if(StringUtils.isBlank(id) || !id.equals(startPointId)){
						this.msg = "作为起始点，需要该点的下一个点为以前的起始点，请重新选择该点的下一个点！";
						return "failure";
					}
				}
			}
		}else if(StringUtils.isBlank(nextPoint)){//成为终点
			StringBuffer sql = new StringBuffer();
			sql.append("select id from bp_fine_route_point_tbl where route_id = '"+routeId+"' and (next_point_id is null or trim(next_point_id) = '') ");
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				String endPointId = null==data.get("id")?"":data.get("id").toString();
				if(!endPointId.equals(previousPoint)){
					if(StringUtils.isBlank(id) || !id.equals(endPointId)){
						this.msg = "作为最后一个点，需要该点的上一个点为以前的终点，请重新选择该点的上一个点！";
						return "failure";
					}
				}
			}
		}else if(StringUtils.isNotBlank(previousPoint) && StringUtils.isNotBlank(nextPoint)){
			if(previousPoint.equals(nextPoint)){
				this.msg = "该点的上一个点和下一个点不能一样，请重新选择该点的上一个点或下一个点！";
				return "failure";
			}
			StringBuffer sql = new StringBuffer();
			sql.append("select next_point_id from bp_fine_route_point_tbl where id = '");
			sql.append(previousPoint);
			sql.append("' ");
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				String nextPointId = null==data.get("next_point_id")?"":data.get("next_point_id").toString();
				if(!nextPointId.equals(nextPoint)){//判断要插入的点所在的两点之间有没有其他的点
					if(StringUtils.isBlank(id) || !id.equals(nextPointId)){
						this.msg = "前一个点和后一个点之间不能包含其他点，<br>请重新选择该点的上一个点或下一个点！";
						return "failure";
					}
					StringBuffer sql2 = new StringBuffer();
					sql2.append("select next_point_id from bp_fine_route_point_tbl where id = '");
					sql2.append(id);
					sql2.append("' ");
					List<Map<String,Object>> dataList2 = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql2.toString());
					if(dataList2.size()>0){
						Map<String,Object> data2 = dataList2.get(0);
						String nextPointId2 = null==data2.get("next_point_id")?"":data2.get("next_point_id").toString();
						if(!nextPointId2.equals(nextPoint)){
							this.msg = "前一个点和后一个点之间不能包含其他点，<br>请重新选择该点的上一个点或下一个点！";
							return "failure";
						}
					}
				}
			}
		}
		return super.execute();
	}

	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRouteId() {
		return routeId;
	}
	
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public String getPreviousPoint() {
		return previousPoint;
	}

	public void setPreviousPoint(String previousPoint) {
		this.previousPoint = previousPoint;
	}

	public String getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(String nextPoint) {
		this.nextPoint = nextPoint;
	}
	
}
