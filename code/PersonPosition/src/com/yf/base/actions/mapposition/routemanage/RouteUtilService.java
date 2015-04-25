package com.yf.base.actions.mapposition.routemanage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class RouteUtilService {
	private static DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	
	public static boolean removeRoute(String routeId,String table,String pointTable,String timeTable,String pointTimeTable,String timeAssignTable){
		boolean success = false;
		Connection conn = dbhelper.DBconnect(GlobalVar.POOLNAME_YFBIZDB);
		PreparedStatement preparedStatement = null;
		try {
			conn.setAutoCommit(false);
			
			//删除路线点的具体时间设置
			String sql;
			sql = "delete from "+pointTimeTable+" where route_point_id in (" +
					"select id from "+pointTable+" where route_id = '"+routeId+"')";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			//删除路线点
			sql = "delete from "+pointTable+" where route_id = '"+routeId+"'";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			
			//删除路线时间对应的路线点时间设置
			sql = "delete from "+pointTimeTable+" where route_time_id in (" +
					"select id from "+timeTable+" where route_id = '"+routeId+"')";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			//删除路线时间上分配的人员
			sql = "delete from "+timeAssignTable+" where route_time_id in (" +
					"select id from "+timeTable+" where route_id = '"+routeId+"')";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			//删除路线时间
			sql = "delete from "+timeTable+" where route_id = '"+routeId+"'";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			
			//删除路线
			sql = "delete from "+table+" where id = '"+routeId+"'";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			
			conn.commit();
			success = true;
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
		return success;
	}
	
	public static boolean removeRouteTimes(String[] routeTimeIds,String timeTable,String pointTimeTable,String timeAssignTable){
		boolean success = false;
		Connection conn = dbhelper.DBconnect(GlobalVar.POOLNAME_YFBIZDB);
		PreparedStatement preparedStatement = null;
		try {
			conn.setAutoCommit(false);
			String sql;
			for(int i=0;i<routeTimeIds.length;i++){
				if(!"".equals(routeTimeIds[i])){
					//删除路线时间对应的路线点时间设置
					sql = "delete from "+pointTimeTable+" where route_time_id = '"+routeTimeIds[i]+"'";
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.execute();
					preparedStatement.close();
					//删除路线时间上分配的人员
					sql = "delete from "+timeAssignTable+" where route_time_id = '"+routeTimeIds[i]+"'";
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.execute();
					preparedStatement.close();
					//删除路线时间
					sql = "delete from "+timeTable+" where id = '"+routeTimeIds[i]+"'";
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.execute();
					preparedStatement.close();
				}
			}
			conn.commit();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
			}
			dbhelper.DBdisconnect(GlobalVar.POOLNAME_YFBIZDB,conn);
		}
		return success;
	}
	
}
