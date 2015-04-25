package com.yf.base.actions.patrolmanageold.routeplan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveRoutePoint extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String routeId;
	private String name;
	private String locationX;
	private String locationY;
	private String previousPoint;
	private String nextPoint;
	private String effectiveRange;
	private String effectiveStartTime;
	private String effectiveEndTime;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		Connection conn = dbhelper.DBconnect(GlobalVar.POOLNAME_YFBIZDB);
		PreparedStatement preparedStatement = null;
		try {
			conn.setAutoCommit(false);
			if(StringUtils.isNotBlank(id)){//更新
				sql.append("update bp_fine_route_point_tbl ");
				sql.append("set name = ?,previous_point_id = ?,next_point_id = ?,");
				sql.append("effective_range = ?,effective_start_time = ?,effective_end_time = ? ");
				sql.append("where id = ? ");
				preparedStatement = conn.prepareStatement(sql.toString());
				preparedStatement.setString(1, name);
				preparedStatement.setString(2, previousPoint);
				preparedStatement.setString(3, nextPoint);
				preparedStatement.setString(4, effectiveRange);
				preparedStatement.setString(5, effectiveStartTime);
				preparedStatement.setString(6, effectiveEndTime);
				preparedStatement.setString(7, id);
				int rowChangeNum = preparedStatement.executeUpdate();
				if (rowChangeNum != 0) {
					String[] oldRoutePoints = getOldData();
					//如果前一个点更改了，需要改两处
					if(!previousPoint.equals(oldRoutePoints[0])){
						//将该点以前的前一个点的后一个点换成该点以前的后一个点
						if(StringUtils.isNotBlank(oldRoutePoints[0])){
							if(!changeNextPointId(conn,oldRoutePoints[1],oldRoutePoints[0])){
								throw new Exception("改变数据出现异常！");
							}
						}
						//将更改后的该点的前一个点的后一个点改成该点
						if(StringUtils.isNotBlank(previousPoint)){
							if(!changeNextPointId(conn,id,previousPoint)){
								throw new Exception("改变数据出现异常！");
							}
						}
					}
					//如果后一个点更改了，需要改两处
					if(!nextPoint.equals(oldRoutePoints[1])){
						//将该点以前的后一个点的前一个点换成该点以前的前一个点
						if(StringUtils.isNotBlank(oldRoutePoints[1])){
							if(!changePreviousPointId(conn,oldRoutePoints[0],oldRoutePoints[1])){
								throw new Exception("改变数据出现异常！");
							}
						}
						//将更改后的该点的后一个点的前一个点改成该点
						if(StringUtils.isNotBlank(nextPoint)){
							if(!changePreviousPointId(conn,id,nextPoint)){
								throw new Exception("改变数据出现异常！");
							}
						}
					}
					conn.commit();
					return super.execute();
				}
			}else{//插入
				sql.append("insert into bp_fine_route_point_tbl(id,route_id,name,location_x,location_y,");
				sql.append("previous_point_id,next_point_id,effective_range,effective_start_time,effective_end_time,add_date) ");
				sql.append("VALUES(?,?,?,?,?,?,?,?,?,?,now()) ");
				preparedStatement = conn.prepareStatement(sql.toString());
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				preparedStatement.setString(1, id);
				preparedStatement.setString(2, routeId);
				preparedStatement.setString(3, name);
				preparedStatement.setString(4, locationX);
				preparedStatement.setString(5, locationY);
				preparedStatement.setString(6, previousPoint);
				preparedStatement.setString(7, nextPoint);
				preparedStatement.setString(8, effectiveRange);
				preparedStatement.setString(9, effectiveStartTime);
				preparedStatement.setString(10, effectiveEndTime);
				int rowChangeNum = preparedStatement.executeUpdate();
				if (rowChangeNum != 0) {
					preparedStatement.close();
					if(StringUtils.isNotBlank(previousPoint)){
						if(!changeNextPointId(conn,id,previousPoint)){
							throw new Exception("改变数据出现异常！");
						}
					}
					if(StringUtils.isNotBlank(nextPoint)){
						if(!changePreviousPointId(conn,id,nextPoint)){
							throw new Exception("改变数据出现异常！");
						}
					}
					conn.commit();
					return super.execute();
				}
			}
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
			}
			dbhelper.DBdisconnect(GlobalVar.POOLNAME_YFBIZDB,conn);
		}
		return "failure";
	}

	private boolean changeNextPointId(Connection conn,String nextPointId,String id) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("update bp_fine_route_point_tbl ");
		sql.append("set next_point_id = '");
		sql.append(nextPointId);
		sql.append("' ");
		sql.append("where id = '");
		sql.append(id);
		sql.append("' ");
		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
		int rowChangeNum = preparedStatement.executeUpdate();
		if (rowChangeNum != 0) {
			preparedStatement.close();
			return true;
		}
		return false;
	}
	
	
	private boolean changePreviousPointId(Connection conn,String previousPointId,String id) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("update bp_fine_route_point_tbl ");
		sql.append("set previous_point_id = '");
		sql.append(previousPointId);
		sql.append("' ");
		sql.append("where id = '");
		sql.append(id);
		sql.append("' ");
		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
		int rowChangeNum = preparedStatement.executeUpdate();
		if (rowChangeNum != 0) {
			preparedStatement.close();
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private String[] getOldData(){
		StringBuffer sql = new StringBuffer();
		sql.append("select previous_point_id,next_point_id from bp_fine_route_point_tbl where id = '");
		sql.append(id);
		sql.append("' ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
		if(dataList.size()>0){
			Map<String,Object> data = dataList.get(0);
			String previousPoint = null==data.get("previous_point_id")?null:data.get("previous_point_id").toString();
			String nextPoint = null==data.get("next_point_id")?null:data.get("next_point_id").toString();
			String[] routePoints = new String[2];
			routePoints[0] = previousPoint;
			routePoints[1] = nextPoint;
			return routePoints;
		}
		return null;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
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

	public String getEffectiveRange() {
		return effectiveRange;
	}

	public void setEffectiveRange(String effectiveRange) {
		this.effectiveRange = effectiveRange;
	}

	public String getEffectiveStartTime() {
		return effectiveStartTime;
	}

	public void setEffectiveStartTime(String effectiveStartTime) {
		this.effectiveStartTime = effectiveStartTime;
	}

	public String getEffectiveEndTime() {
		return effectiveEndTime;
	}

	public void setEffectiveEndTime(String effectiveEndTime) {
		this.effectiveEndTime = effectiveEndTime;
	}
	
	
}
