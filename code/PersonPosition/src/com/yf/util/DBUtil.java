package com.yf.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.RowSet;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.sun.rowset.CachedRowSetImpl;

public class DBUtil {

	private String driverClassName = "";
	private String url = "";
	private String username = "";
	private String password = "";
	private Connection conn = null;
	private QueryRunner qRunner;
	private Statement stmt = null;
	private PreparedStatement psmt = null;


	public PreparedStatement getPsmt() {
		return psmt;
	}

	public void setPsmt(PreparedStatement psmt) {
		this.psmt = psmt;
	}

	public void getProperties() {
		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("prtproject.properties"));

			driverClassName = new String(props.getProperty(
					"jdbc.driverClassName").getBytes("ISO-8859-1"), "utf-8");
			url = new String(props.getProperty("jdbc.url").getBytes(
					"ISO-8859-1"), "utf-8");
			username = new String(props.getProperty("jdbc.username").getBytes(
					"ISO-8859-1"), "utf-8");
			password = new String(props.getProperty("jdbc.password").getBytes(
					"ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			getProperties();
			DbUtils.loadDriver(driverClassName);
			conn = DriverManager.getConnection(url, username, password);
			qRunner = new QueryRunner();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnections() {
		try {
			getProperties();
			DbUtils.loadDriver(driverClassName);
			Connection conns = DriverManager.getConnection(url, username, password);
			return conns;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[] statisticsBySql(String sql) {
		Object[] array = new Object[1];
		try {
			getConnection();

			array = qRunner.query(conn, sql, new ArrayHandler());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DbUtils.closeQuietly(conn);
		}
		return array;
	}
	
	public List<?> getPreSalesBySql(String sql) {
		List<?> ls = null;
		try {
			getConnection();
			ls = qRunner.query(conn, sql, new ArrayListHandler());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DbUtils.closeQuietly(conn);
		}
		return ls;
	}
	public List<?> getMapListBySql(String sql) {
		List<?> ls = null;
		try {
			getConnection();
			ls = qRunner.query(conn, sql, new MapListHandler());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DbUtils.closeQuietly(conn);
		}
		return ls;
	}
	
	public int update(String sql) {
		int update = -1;
		try {
			getConnection();
		
			update = qRunner.update(conn, sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DbUtils.closeQuietly(conn);
		}
		return update;
	}
	
	public void test() {
		try {
			// 以下部分代码采用MapListHandler存储方式查询
			Debug.println("***Using MapListHandler***");
			
			int nowYear=2011;
		
			getConnection();
		    String sql="select count(1)as count,room_type from yw_room_tbl  t  group by t.room_type ";
			List lMap = qRunner.query(conn, sql,
					new MapListHandler());

			Debug.println("id ------------- name ");
			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				Debug.println(vals.get("room_type") + "  ------------- "
						+ vals.get("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	/**
	 * 执行单条sql
	 * @param sql
	 * @return boolean
	 */
	public boolean executeFor(String sql) {
		boolean flag = false;
		int update = -1;
		try {
			getConnection();
			stmt = conn.createStatement();
			stmt.execute(sql);
			update = stmt.getUpdateCount();
			flag = update != -1 ? true : flag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);
		}
		return flag;
	}
	
	/**
	 * 执行多条sql
	 * @param sqlList
	 * @return boolean
	 */
	public boolean executeFor(List<String> sqlList) {
		boolean flag = false;
		if(sqlList.size()>0){
			try {
				getConnection();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				for (int i=0; i<sqlList.size(); i++) {
					stmt.addBatch((String)sqlList.get(i));
				}
				stmt.executeBatch();
				conn.commit();
				flag = true;
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally {
				DbUtils.closeQuietly(stmt);
				DbUtils.closeQuietly(conn);
			}
		}else{
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 根据主键ID，批量删除数据
	 * @param tableName
	 * @param ids
	 * @return boolean
	 */
	public boolean deleteByIds(String tableName,String[] ids){
		boolean flag = false;
		if(ids.length > 0){
			getConnection();
			try {
				conn.setAutoCommit(false);
				psmt = conn.prepareStatement("delete from "+tableName+" where Auto_C_000=?");
				psmt.clearBatch();
				for (int i = 0; i < ids.length; i++) {
					psmt.setString(1, ids[i]);
					psmt.addBatch();
				}
				psmt.executeBatch();
				conn.commit();
				flag = true;
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally {
				DbUtils.closeQuietly(psmt);
				DbUtils.closeQuietly(conn);
			}
		}
		return flag;
	}
	
	public boolean insert(String sql, Object[] elements) {
		boolean flag = false;
		try {
			getConnection();
			conn.setAutoCommit(false);
			psmt = conn.prepareStatement(sql);
			for (int i = 0; i < elements.length; i++) {
				setPreparedStatement(i + 1, elements[i]);
			}
			int vINT = psmt.executeUpdate();
			if (vINT != 0) {
				flag = true;
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(psmt);
			DbUtils.closeQuietly(conn);
		}
		return flag;
	}
	
	/**
	 * 设置预定sql语句的参数
	 * @param i
	 * @param object
	 * @throws SQLException
	 */
	private void setPreparedStatement(int i, Object object) throws SQLException {
		if (object instanceof String) {
			psmt.setString(i, (String) object);
		}
		if (object instanceof Short) {

			psmt.setShort(i, (Short) object);
		}
		if (object instanceof Long) {

			psmt.setLong(i, (Long) object);
		}
		if (object instanceof Integer) {

			psmt.setInt(i, (Integer) object);
		}
		if (object instanceof Boolean) {

			psmt.setBoolean(i, (Boolean) object);
		}
		if (object instanceof Byte) {

			psmt.setByte(i, (Byte) object);
		}
		if (object instanceof Double) {

			psmt.setDouble(i, (Double) object);
		}
		if (object instanceof Float) {
			psmt.setFloat(i, (Float) object);
		}
		if(object instanceof Date){
			psmt.setDate(i, (Date)object);
		}
		if(object instanceof Time){
			psmt.setTime(i, (Time)object);
		}

	}
	
	/**
	 * 通过map创建insert语句
	 * 
	 * @param tablename
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String ctIstSl(String tablename, Map map) throws Exception {
		Set<String> keyset = map.keySet();
		final String reg = ",";
		StringBuilder builder = new StringBuilder(" insert into ");
		builder.append(" " + tablename + " (");
		int i = 0;
		for (Iterator iter = keyset.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (map.get(element) == null) {
				continue;
			}
			builder.append(element + reg);
			i++;
		}
		String name = builder.toString();
		if (name.endsWith(reg)) {
			name = name.substring(0, name.length() - 1);
		}
		name += ")values(";
		for (; i > 0; i--) {
			name += "?";
			if (i != 1) {
				name += reg;
			}
		}
		name += ")";
		return name;
	}
	
	public static Object[] delNullArray(Object[] obj) {
		List list = new ArrayList();
		Object obje = null;
		for (int i = 0; i < obj.length; i++) {
			obje = obj[i];
			if (obje != null) {
				list.add(obje);
			}
		}
		return list.toArray();
	}
	
	/**
	 * 通过map创建update语句
	 * 
	 * @param tablename
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String ctUpdateSl(String tablename, Map map,String where) throws Exception {
		Set<String> keyset = map.keySet();
		final String reg = " =? ";
		final String reg1 = ",";
		StringBuilder builder = new StringBuilder("update");
		builder.append(" " + tablename + " set ");
		int i = 0;
		for (Iterator iter = keyset.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (map.get(element) == null) {
				continue;
			}
			if (i != 0 ) {
				builder.append(reg1);
			}
			builder.append(element + reg);
			i++;
		}
		builder.append(where);
		String name = builder.toString();	
		return name;
	}
	
	public RowSet select(String sql) throws Exception {
		CachedRowSetImpl rowset = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			getConnection();
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			rowset.populate(rs);
		} catch (Exception e) {
			rollback(conn);
			throw new Exception("查询数据失败");
		} finally {
			try{
				conn.setAutoCommit(true);
			}catch(SQLException sqle){
			}
			disconnect(statement, conn, rs);
		}
		return rowset;
	}
	
	/**
	 * 数据库回滚
	 * 
	 * @param conn Connection对象
	 * @return Connection对象
	 */
	public Connection rollback(Connection conn) {
		try {
			if(conn!=null){
				conn.rollback();
			}
		} catch (SQLException e1) {

		}
		return conn;
	}
	
	public void disconnect(Statement statement, Connection conn, ResultSet rs) throws SQLException {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {

			}
		}
		if (conn != null) {
			conn.close();
		}

	}

	public static void main(String[] args) {
		DBUtil d = new DBUtil();
		d.test();
	}
	
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public QueryRunner getQRunner() {
		return qRunner;
	}

	public void setQRunner(QueryRunner runner) {
		qRunner = runner;
	}
	
	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}
}
