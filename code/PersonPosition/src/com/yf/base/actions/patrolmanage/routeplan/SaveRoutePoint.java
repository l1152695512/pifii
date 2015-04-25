package com.yf.base.actions.patrolmanage.routeplan;

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
	private String name;
	private String routeId;
	private String locationX;
	private String locationY;
	private String msg;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		Connection conn = dbhelper.DBconnect(GlobalVar.POOLNAME_YFBIZDB);
		PreparedStatement preparedStatement = null;
		try {
			conn.setAutoCommit(false);
			sql.append("insert into bp_fine_route_point_tbl(id,route_id,name,location_x,location_y,previous_point_id,next_point_id,add_date) ");
			sql.append("VALUES(?,?,?,?,?,?,?,now()) ");
			preparedStatement = conn.prepareStatement(sql.toString());
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, routeId);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, locationX);
			preparedStatement.setString(5, locationY);
			String lastPointId = getLastPointId();
			preparedStatement.setString(6, lastPointId);
			preparedStatement.setString(7, "");
			int rowChangeNum = preparedStatement.executeUpdate();
			if (rowChangeNum != 0) {
				preparedStatement.close();
				if(StringUtils.isNotBlank(lastPointId)){
					String sqlUpdate = "update bp_fine_route_point_tbl set next_point_id = '"+id+"' where id = '"+lastPointId+"'";
					if(!dbhelper.update(sqlUpdate)){
						throw new Exception("更新数据出现异常！");
					}
				}
				conn.commit();
				msg = id;
				return super.execute();
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

	@SuppressWarnings("unchecked")
	private String getLastPointId(){
		String sql = "select id from bp_fine_route_point_tbl where route_id = '"+routeId+"' and (next_point_id = '' or next_point_id is null) ";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			return dataList.get(0).get("id").toString();
		}
		return "";
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
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
