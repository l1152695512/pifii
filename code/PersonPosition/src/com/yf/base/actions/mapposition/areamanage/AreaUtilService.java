package com.yf.base.actions.mapposition.areamanage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AreaUtilService {
	private static DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	
	public static boolean removeArea(String areaId,String table,String pointTable,String timeTable,String timeAssignTable){
		boolean success = false;
		Connection conn = dbhelper.DBconnect(GlobalVar.POOLNAME_YFBIZDB);
		PreparedStatement preparedStatement = null;
		try {
			conn.setAutoCommit(false);
			String sql;
			
			//删除路线点
			sql = "delete from "+pointTable+" where area_id = '"+areaId+"'";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			
			//删除路线时间上分配的人员
			sql = "delete from "+timeAssignTable+" where area_time_id in (" +
					"select id from "+timeTable+" where area_id = '"+areaId+"')";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			//删除路线时间
			sql = "delete from "+timeTable+" where area_id = '"+areaId+"'";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.execute();
			preparedStatement.close();
			
			//删除路线
			sql = "delete from "+table+" where id = '"+areaId+"'";
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
	
	public static boolean removeAreaTimes(String[] areaTimeIds,String timeTable,String timeAssignTable){
		boolean success = false;
		Connection conn = dbhelper.DBconnect(GlobalVar.POOLNAME_YFBIZDB);
		PreparedStatement preparedStatement = null;
		try {
			conn.setAutoCommit(false);
			String sql;
			for(int i=0;i<areaTimeIds.length;i++){
				if(!"".equals(areaTimeIds[i])){
					//删除路线时间上分配的人员
					sql = "delete from "+timeAssignTable+" where area_time_id = '"+areaTimeIds[i]+"'";
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.execute();
					preparedStatement.close();
					//删除路线时间
					sql = "delete from "+timeTable+" where id = '"+areaTimeIds[i]+"'";
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
