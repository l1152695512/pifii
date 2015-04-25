package com.yf.util.dbhelper;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

/**
 * @author Administrator
 * @vesion:v1.0
 */
public class DBHelper {
	private String poolName = "yfbizdb";// 数据库名
	CallableStatement callableStatement = null;// 预处理存储过程语句
	PreparedStatement preparedStatement = null;// 预处理sql语句
	Statement statement = null;// 预处理sql语句
	private static DBConnectionManager connectionManager;// 连接池管理对象
	private Logger logger = Logger.getLogger(this.getClass().getName());// 日志类

	public DBHelper(String poolName) {
		this.poolName = poolName;
	}

	static {
		connectionManager = DBConnectionManager.getInstance();
	}
	
	/**
	 * 执行单条sql
	 * @param sql
	 * @return boolean
	 */
	public boolean executeFor(String sql) {
		boolean flag = false;
		int update = -1;
		Connection conn = DBconnect(poolName);
		try {
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			statement.execute(sql);
			update = statement.getUpdateCount();
			conn.commit();
			flag = update != -1 ? true : flag;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			if (conn != null) {
				ResultSet rs = null;
				disconnect(statement,conn, rs,poolName);
			}
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
			Connection conn = DBconnect(poolName);
			try {
				conn.setAutoCommit(false);
				statement = conn.createStatement();
				for (int i=0; i<sqlList.size(); i++) {
					statement.addBatch((String)sqlList.get(i));
				}
				statement.executeBatch();
				conn.commit();
				flag = true;
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				rollback(conn);
			} finally {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException sqle) {
					logger.error(sqle.getMessage(), sqle);
				}
				if (conn != null) {
					ResultSet rs = null;
					disconnect(statement,conn, rs,poolName);
				}
			}
		}else{
			flag = true;
		}
		return flag;
	}
	
	public Object[] statisticsBySql(String sql) {
		Object[] array = new Object[1];
		Connection conn = DBconnect(poolName);
		QueryRunner qRunner = new QueryRunner();
		try {
			conn.setAutoCommit(false);
			array = qRunner.query(conn, sql, new ArrayHandler());
			logger.debug("查询语句为：" + sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			if (conn != null) {
				DBdisconnect(poolName, conn);
			}
		}
		return array;
	}

	public List<?> getPreSalesBySql(String sql) {
		List<?> ls = null;
		Connection conn = DBconnect(poolName);
		QueryRunner qRunner = new QueryRunner();
		try {
			conn.setAutoCommit(false);
			ls = qRunner.query(conn, sql, new ArrayListHandler());
			logger.debug("查询语句为：" + sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			if (conn != null) {
				DBdisconnect(poolName, conn);
			}
		}
		return ls;
	}
	
	public List<?> getMapListBySql(String sql) {
		List<?> ls = null;
		Connection conn = DBconnect(poolName);
		QueryRunner qRunner = new QueryRunner();
		try {
			conn.setAutoCommit(false);
			ls = qRunner.query(conn, sql, new MapListHandler());
			logger.debug("查询语句为：" + sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			if (conn != null) {
				DBdisconnect(poolName, conn);
			}
		}
		return ls;
	}
	
	public List<?> getMapListBySql(String sql,Object[] params) {
		List<?> ls = null;
		Connection conn = DBconnect(poolName);
		QueryRunner qRunner = new QueryRunner();
		try {
			conn.setAutoCommit(false);
			ls = qRunner.query(conn, sql, params, new MapListHandler());
			logger.debug("查询语句为：" + sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			if (conn != null) {
				DBdisconnect(poolName, conn);
			}
		}
		return ls;
	}
	
	/**
	 * 执行一个入参及无输出结果集的存储过程
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return 成功与否标识
	 * @throws DataAccessException
	 */
	public void storeProcedures(String procName, String input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		try {
			conn.setAutoCommit(false);
			callableStatement = conn.prepareCall("{call " + procName + "}");
			callableStatement.setString(1, input);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e) {
				logger.error("调用存储过程设置事务自动提交异常", e);
			}
			disconnect(callableStatement, conn, poolName);
		}
	}

	/**
	 * 执行多个入参及无输出结果集的存储过程
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return 成功与否标识
	 * @throws DataAccessException
	 */
	public void storeProcedures(String procName, List<String> input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		try {
			conn.setAutoCommit(false);
			callableStatement = conn.prepareCall("{call " + procName + "}");
			for (int i = 0; i < input.size(); i++) {
				callableStatement.setString(i + 1, input.get(i));
			}
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e) {
				logger.error("调用存储过程设置事务自动提交异常", e);
			}
			disconnect(callableStatement, conn, poolName);
		}
	}

	/**
	 * 执行单个入参及输出结果集的存储过程
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return RowSet
	 * @throws DataAccessException
	 */
	public RowSet storeProceduresRowSet(String procName, String input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		CachedRowSetImpl rowset = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			callableStatement = conn.prepareCall("{call " + procName + "}");
			callableStatement.setString(1, input);
			callableStatement.execute();
			rs = callableStatement.getResultSet();
			rowset.populate(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e) {
				logger.error("调用存储过程异常", e);
			}
			disconnect(callableStatement, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * @param procName
	 *            存储过程名称,sql2005调用
	 * @param input
	 *            入参
	 * @return RowSet
	 * @throws DataAccessException
	 */
	public RowSet storeProceduresRowSet2005(String procName, List<String> input)
			throws DataAccessException {
		// Connection conn = DBconnect(poolName);
		// 连接池不稳定，改为jdbc获取连接。2011.5.26 by yangzhiyuan
		Connection conn = connectionManager.getJDBCConn(poolName);
		CachedRowSetImpl rowset = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int num = input.size();
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			pstmt = conn.prepareCall(procName);
			for (int i = 0; i < num; i++) {
				pstmt.setString(i + 1, input.get(i));
			}
			rs = pstmt.executeQuery();
			rowset.populate(rs);
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			disconnect(pstmt, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * 执行多个入参及输出结果集的存储过程
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return RowSet
	 * @throws DataAccessException
	 */
	public RowSet storeProceduresRowSet(String procName, List<String> input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		CachedRowSetImpl rowset = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			callableStatement = conn.prepareCall("{call " + procName + "}");
			for (int i = 0; i < input.size(); i++) {
				callableStatement.setString(i + 1, input.get(i));
			}
			callableStatement.execute();
			rs = callableStatement.getResultSet();
			rowset.populate(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e) {
				logger.error("存储过程设置事务为自动提交异常", e);
			}
			disconnect(callableStatement, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * 一个入参、无输出结果集、输出成功与否标志 输出参数：返回码、返回信息 0:成功，非0：失败
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return 成功与否标识
	 * @throws DataAccessException
	 */
	public void storeProceduresFlag(String procName, String input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		try {
			conn.setAutoCommit(false);
			callableStatement = conn.prepareCall("{call " + procName + "}");
			callableStatement.setString(1, input);
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			callableStatement.execute();
			this.errCode = callableStatement.getString(2);
			this.errMsg = callableStatement.getString(3);
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			disconnect(callableStatement, conn, poolName);
		}
	}

	/**
	 * 一个入参、无输出结果集、输出成功与否标志 输出参数：返回码、返回信息 0:成功，非0：失败
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return 成功与否标识
	 * @throws DataAccessException
	 */
	public void storeProceduresFlag(String procName, List<String> input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		int num = input.size();
		try {
			conn.setAutoCommit(false);
			callableStatement = conn.prepareCall("{call " + procName + "}");
			for (int i = 0; i < num; i++) {
				callableStatement.setString(i + 1, input.get(i));
			}
			callableStatement.registerOutParameter(num + 1, Types.VARCHAR);
			callableStatement.registerOutParameter(num + 2, Types.VARCHAR);
			callableStatement.execute();
			this.errCode = callableStatement.getString(num + 1);
			this.errMsg = callableStatement.getString(num + 2);
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			disconnect(callableStatement, conn, poolName);
		}
	}

	/**
	 * 单个入参、输出结果集、输出成功与否标志 0:成功，非0：失败
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return RowSet、成功与否标志
	 * @throws DataAccessException
	 */
	public RowSet storeProceduresRowSetFlag(String procName, String input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		CachedRowSetImpl rowset = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			callableStatement = conn.prepareCall("{call " + procName + "}");
			callableStatement.setString(1, input);
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			callableStatement.execute();

			this.errCode = callableStatement.getString(2);
			this.errMsg = callableStatement.getString(3);

			if ("0".equals(this.errCode)) {
				rs = callableStatement.getResultSet();
				rowset.populate(rs);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			disconnect(callableStatement, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * 多个入参、输出结果集、输出成功与否标志 0:成功，非0：失败
	 * 
	 * @param procName
	 *            存储过程名称
	 * @param input
	 *            入参
	 * @return RowSet、成功与否标志
	 * @throws DataAccessException
	 */
	public RowSet storeProceduresRowSetFlag(String procName, List<String> input)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		CachedRowSetImpl rowset = null;
		ResultSet rs = null;
		int num = input.size();
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			callableStatement = conn.prepareCall("{call " + procName + "}");
			for (int i = 0; i < num; i++) {
				callableStatement.setString(i + 1, input.get(i));
			}
			callableStatement.registerOutParameter(num + 1, Types.VARCHAR);
			callableStatement.registerOutParameter(num + 2, Types.VARCHAR);
			callableStatement.execute();

			this.errCode = callableStatement.getString(num + 1);
			this.errMsg = callableStatement.getString(num + 2);

			if ("0".equals(this.errCode)) {
				rs = callableStatement.getResultSet();
				rowset.populate(rs);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
			throw new DataAccessException("存储过程操作失败");
		} finally {
			disconnect(callableStatement, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * 执行单条查询语句，适用于简单的sql查询语句
	 * 
	 * @param sql
	 *            sql语句
	 * @return 结果集
	 * @throws DataAccessException
	 */

	public RowSet select(String sql) throws DataAccessException {
		Connection conn = DBconnect(poolName);
		CachedRowSetImpl rowset = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			statement = conn.createStatement();
			logger.debug("查询语句为：" + sql);
			rs = statement.executeQuery(sql);
			rowset.populate(rs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			throw new DataAccessException("查询数据失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(statement, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * 执行单条查询语句，适用于简单的sql查询语句
	 * 
	 * @param sql
	 *            sql语句
	 * @return 结果集
	 * @throws DataAccessException
	 */

	public RowSet select2(String sql) throws DataAccessException {
		Connection conn = connectionManager.getConnection2(poolName);
		CachedRowSetImpl rowset = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			rowset.populate(rs);
		} catch (Exception e) {
			rollback(conn);
			throw new DataAccessException("查询数据失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect2(statement, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * 查询数据，根据传入的数据集查询数据
	 * 
	 * @param sql
	 *            sql语句
	 * @param elements
	 *            传入的要素
	 * @return 结果集
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	public RowSet select(String sql, Object[] elements)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		CachedRowSetImpl rowset = null;

		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			preparedStatement = conn.prepareStatement(sql);
			logger.info("查询语句为：");
			logger.info(sql);
			for (int i = 0; i < elements.length; i++) {
				setPreparedStatement(i + 1, elements[i]);
			}
			rs = preparedStatement.executeQuery();
			rowset.populate(rs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			throw new DataAccessException("查询数据失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, rs, poolName);
		}
		return rowset;
	}

	/**
	 * 执行单条查询语句，适用于简单的sql查询语句
	 * 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	public RowSet select(String sql, Map<Integer, Object> elements)
			throws DataAccessException {
		Connection conn = DBconnect(poolName);
		CachedRowSetImpl rowset = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			rowset = new CachedRowSetImpl();
			preparedStatement = conn.prepareStatement(sql);
			logger.info("查询语句为：");
			logger.info(sql);
			for (int i = 0; i < elements.size(); i++) {
				setPreparedStatement(i + 1, elements.get(i + 1));
			}
			rs = preparedStatement.executeQuery();
			rowset.populate(rs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			throw new DataAccessException("查询数据失败");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, rs, poolName);
		}
		return rowset;
	}

	public List execute(String procdure, List<ProcdureParmeter> list)
			throws SQLException, DataAccessException {
		List<Object> returnList;
		Object object = null;
		Connection conn = DBconnect(poolName);
		try {
			String procdureCall = "{call " + procdure + "}";

			callableStatement = conn.prepareCall(procdureCall);
			returnList = new ArrayList();
			int type = 0;
			int index = 0;
			for (ProcdureParmeter element : list) {
				type = element.getType();
				index = element.getIndex();
				if (type == ProcdureParmeter.IN || type == ProcdureParmeter.IO) {
					setProcStatement(index, element.getValue());
				}
				if (type == ProcdureParmeter.OUT || type == ProcdureParmeter.IO) {
					setProcRegisterParameters(index, element.getOutType());
				}
			}
			callableStatement.execute();
			for (ProcdureParmeter element : list) {
				type = element.getType();
				index = element.getIndex();
				if (type == ProcdureParmeter.OUT || type == ProcdureParmeter.IO) {
					try {
						object = callableStatement.getObject(index);
					} catch (Exception e) {
						continue;
					}
					returnList.add(object);
				}
			}
		} finally {
			disconnect(callableStatement, conn, poolName);
		}
		return returnList;
	}

	@Deprecated
	public Object[] execute(String procdure, Object[] inputElements,
			Class[] outputElements) throws DataAccessException {
		Connection conn = DBconnect(poolName);
		Object[] returnParameters;
		try {
			callableStatement = conn.prepareCall(procdure);
			int inputElementsLength = 0;
			int outputElementsLength = 0;
			if (inputElements != null) {
				inputElementsLength = inputElements.length;
			}
			if (outputElements != null) {
				outputElementsLength = outputElements.length;
			}
			returnParameters = new Object[outputElementsLength];
			for (int i = 0; i < inputElementsLength; i++) {
				setProcStatement(i + 1, inputElements[i]);
			}
			for (int j = 0; j < outputElementsLength; j++) {
				setProcRegisterParameters(inputElementsLength + j + 1,
						outputElements[j]);
			}

			callableStatement.execute();
			for (int j = 0; j < outputElementsLength; j++) {
				returnParameters[j] = callableStatement
						.getObject(inputElementsLength + j + 1);
			}

			return returnParameters;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			disconnect(callableStatement, conn, poolName);
		}
	}

	/**
	 * 插入数据库的基本方法，适用于简单的sql语句
	 * 
	 * @param sql
	 * @return
	 */
	public boolean insert(String sql) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			int vINT = preparedStatement.executeUpdate();
			conn.commit();
			if (vINT != 0) {
				vBOOL = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 根据传入的参数执行添加数据的方法，单条执行
	 * 
	 * @param sql
	 * @return
	 */
	public boolean insert(String sql, Object[] elements) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < elements.length; i++) {
				setPreparedStatement(i + 1, elements[i]);
			}
			int vINT = preparedStatement.executeUpdate();
			if (vINT != 0) {
				vBOOL = true;
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 根据传入的参数执行添加数据的方法，单条执行
	 * 
	 * @param sql
	 * @return
	 */
	public boolean insert(String sql, Map<Integer, Object> elements) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < elements.size(); i++) {
				setPreparedStatement(i + 1, elements.get(i + 1));
			}
			int vINT = preparedStatement.executeUpdate();
			conn.commit();
			if (vINT != 0) {
				vBOOL = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				logger.error("设置回退", e);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 删除单条记录的方法，适用于简单的sql语句
	 * 
	 * @param sql
	 * @return
	 */
	public boolean delete(String sql) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			int vINT = preparedStatement.executeUpdate();
			if (vINT != 0) {
				vBOOL = true;
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 根据传入的参数删除单条记录的方法
	 * 
	 * @param sql
	 * @return
	 */
	public boolean delete(String sql, Object[] elements) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < elements.length; i++) {
				setPreparedStatement(i + 1, elements[i]);
			}
			int vINT = preparedStatement.executeUpdate();
			if (vINT != 0) {
				vBOOL = true;
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 根据传入的参数删除单条记录的方法
	 * 
	 * @param sql
	 * @return
	 */
	public boolean delete(String sql, Map<Integer, Object> elements) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < elements.size(); i++) {
				setPreparedStatement(i + 1, elements.get(i + 1));
			}
			int vINT = preparedStatement.executeUpdate();
			conn.commit();
			if (vINT != 0) {
				vBOOL = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}
	
	/**
	 * 根据主键ID，批量删除数据
	 * @param tableName
	 * @param ids
	 * @return boolean
	 */
	public boolean deleteByIds(String tableName,String[] ids){
		boolean vBOOL = false;
		if(ids.length > 0){
			Connection conn = DBconnect(poolName);
			try {
				conn.setAutoCommit(false);
				preparedStatement = conn.prepareStatement("delete from "+tableName+" where Auto_C_000=?");
				preparedStatement.clearBatch();
				for (int i = 0; i < ids.length; i++) {
					preparedStatement.setString(1, ids[i]);
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
				conn.commit();
				vBOOL = true;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				rollback(conn);
				return false;
			} finally {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException sqle) {
					logger.error(sqle.getMessage(), sqle);
				}
				disconnect(preparedStatement, conn, poolName);
			}
		}
		return vBOOL;
	}

	/**
	 * 更新一条记录,适用与简单的update语句
	 * 
	 * @param sql
	 * @return
	 */
	public boolean update(String sql) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			int vINT = preparedStatement.executeUpdate();
			conn.commit();
			if (vINT != 0) {
				vBOOL = true;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 根据输入的参数执行更新操作
	 * 
	 * @param sql
	 * @return
	 */
	public boolean update(String sql, Object[] elements) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < elements.length; i++) {
				setPreparedStatement(i + 1, elements[i]);
			}
			int vINT = preparedStatement.executeUpdate();
			conn.commit();
			if (vINT != 0) {
				vBOOL = true;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 根据输入的参数执行更新操作
	 * 
	 * @param sql
	 * @return
	 */
	public boolean update(String sql, Map<Integer, Object> elements) {
		Connection conn = DBconnect(poolName);
		boolean vBOOL = false;
		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < elements.size(); i++) {
				setPreparedStatement(i + 1, elements.get(i + 1));
			}
			int vINT = preparedStatement.executeUpdate();
			conn.commit();
			if (vINT != 0) {
				vBOOL = true;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			rollback(conn);
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		return vBOOL;
	}

	/**
	 * 多条SQL一起插入数据的方法
	 * 
	 * @param sql
	 * @return 每100条做一个事物 str[0]=0:操作成功/ str[0]=1:操作失败 Str[1]:返回信息
	 */
	public String[] inserts(ArrayList<String> SQLList) {
		String[] srt = { "1", "" };
		Connection conn = DBconnect(poolName);
		if (SQLList == null || SQLList.size() == 0) {
			return srt;
		}
		logger.info("插入数据总条数" + SQLList.size());
		String returnStr = "总共有" + SQLList.size() + "条";
		int i_hundred = (SQLList.size() + 99) / 100;
		try {
			for (int j = 0; j < i_hundred; j++) {
				conn.setAutoCommit(false);
				for (int i = 100 * j; i < ((SQLList.size() - 100 * (j + 1)) > 0 ? 100 * (j + 1)
						: SQLList.size()); i++) {
					logger.info("插入sql第" + i + "条语句：" + SQLList.get(i));
					preparedStatement = conn.prepareStatement(SQLList.get(i));
					preparedStatement.executeUpdate();
					conn.commit();
				}
				returnStr = returnStr
						+ "|第"
						+ (100 * j + 1)
						+ "条到"
						+ ((SQLList.size() - 100 * (j + 1)) > 0 ? 100 * (j + 1)
								: SQLList.size()) + "条导入成功";
			}

		} catch (Exception e) {
			logger.error("批量导入数据异常" + e.getMessage(), e);
			rollback(conn);
			srt[0] = "1";
			srt[1] = "插入数据异常";
			return srt;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException sqle) {
				logger.error(sqle.getMessage(), sqle);
			}
			disconnect(preparedStatement, conn, poolName);
		}
		srt[0] = "0";
		srt[1] = returnStr;
		return srt;
	}

	/**
	 * 获得连接
	 * 
	 * @param poolName
	 * @return
	 */
	public Connection DBconnect(String poolName) {
		Connection conn = connectionManager.getConnection(poolName);
		try {
			logger.info("DBconnect()测试获取连接事务相关信息:" + conn.getAutoCommit() + ","
					+ conn.getTransactionIsolation() + ",hashcode="
					+ conn.hashCode());
		} catch (Exception e) {
			logger.error("DBconnect()输出事务信息异常", e);
		}
		return conn;
	}

	/**
	 * 释放连接
	 * 
	 * @param poolName
	 * @param con
	 */
	public void DBdisconnect(String poolName, Connection con) {
		try {
			// logger.info("测试释放连接事务相关信息:"+con.getAutoCommit()+","+con.getTransactionIsolation());
		} catch (Exception e) {
			logger.error("输出事务信息异常", e);
		}
		connectionManager.freeConnection(poolName, con);// 释放，但并未断开连接
	}

	/**
	 * 释放连接
	 * 
	 * @param poolName
	 * @param con
	 */
	public void DBdisconnect2(String poolName, Connection con) {
		connectionManager.freeConnection2(poolName, con);// 释放，但并未断开连接
	}

	/**
	 * 数据库回滚
	 * 
	 * @param conn
	 *            Connection对象
	 * @return Connection对象
	 */
	public Connection rollback(Connection conn) {
		try {
			if (conn != null) {
				conn.rollback();
			}
		} catch (SQLException e1) {
			conn = connectionManager.newConnection(poolName);
			logger.error("事务回滚异常：" + e1);

		}
		return conn;
	}

	/**
	 * 关闭需要关闭的一切
	 * 
	 * @param statement
	 * @param conn
	 * @param rs
	 * @param poolName
	 */
	public void disconnect(PreparedStatement ps, Connection conn,
			String poolName) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		if (conn != null) {
			DBdisconnect(poolName, conn);
		}
	}

	/**
	 * 关闭需要关闭的一切
	 * 
	 * @param statement
	 * @param conn
	 * @param rs
	 * @param poolName
	 */
	public void disconnect(Statement statement, Connection conn, ResultSet rs,
			String poolName) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("", e);
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {

				logger.error("", e);
			}
		}
		if (conn != null) {
			DBdisconnect(poolName, conn);

		}

	}

	/**
	 * 关闭需要关闭的一切
	 * 
	 * @param statement
	 * @param conn
	 * @param rs
	 * @param poolName
	 */
	public void disconnect2(Statement statement, Connection conn, ResultSet rs,
			String poolName) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("", e);
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {

				logger.error("", e);
			}
		}
		if (conn != null) {
			DBdisconnect(poolName, conn);

		}

	}

	private void setProcRegisterParameters(int i, Object object)
			throws SQLException {
		if (object.equals(String.class)) {
			callableStatement.registerOutParameter(i, Types.VARCHAR);
		}
		if (object.equals(Short.class)) {

			callableStatement.registerOutParameter(i, Types.NUMERIC);
		}
		if (object.equals(Long.class)) {

			callableStatement.registerOutParameter(i, Types.NUMERIC);
		}
		if (object.equals(Integer.class)) {

			callableStatement.registerOutParameter(i, Types.NUMERIC);
		}
		if (object.equals(Boolean.class)) {

			callableStatement.registerOutParameter(i, Types.BOOLEAN);
		}
		if (object.equals(Byte.class)) {

			callableStatement.registerOutParameter(i, Types.BIT);
		}
		if (object.equals(Double.class)) {

			callableStatement.registerOutParameter(i, Types.NUMERIC);
		}
		if (object.equals(Float.class)) {

			callableStatement.registerOutParameter(i, Types.NUMERIC);
		}
		if (object.equals(Date.class)) {
			callableStatement.registerOutParameter(i, Types.DATE);
		}
		if (object.equals(Timestamp.class)) {
			callableStatement.registerOutParameter(i, Types.TIMESTAMP);
		}
		/*
		 * if (object.equals(ResultSet.class)) {
		 * callableStatement.registerOutParameter(i,
		 * oracle.jdbc.OracleTypes.CURSOR); }
		 */
		if (object.equals(Clob.class)) {
			callableStatement.registerOutParameter(i, Types.CLOB);
		}
		if (object.equals(Blob.class)) {
			callableStatement.registerOutParameter(i, Types.BLOB);
		}
	}

	/**
	 * 设置预定存储过程语句的参数
	 * 
	 * @param i
	 * @param object
	 * @throws SQLException
	 */
	private void setProcStatement(int i, Object object) throws SQLException {
		if (object instanceof String) {
			callableStatement.setString(i, (String) object);
		}else if (object instanceof Short) {
			callableStatement.setShort(i, (Short) object);
		}else if (object instanceof Long) {
			callableStatement.setLong(i, (Long) object);
		}else if (object instanceof Integer) {
			callableStatement.setInt(i, (Integer) object);
		}else if (object instanceof Boolean) {
			callableStatement.setBoolean(i, (Boolean) object);
		}else if (object instanceof Byte) {
			callableStatement.setByte(i, (Byte) object);
		}else if (object instanceof Double) {
			callableStatement.setDouble(i, (Double) object);
		}else if (object instanceof Float) {
			callableStatement.setFloat(i, (Float) object);
		}else if (object instanceof Date) {
			callableStatement.setDate(i, (Date) object);
		}else if (object instanceof Timestamp) {
			callableStatement.setTimestamp(i, (Timestamp) object);
		}else if (object instanceof Clob) {
			callableStatement.setClob(i, (Clob) object);
		}else if (object instanceof Blob) {
			callableStatement.setBlob(i, (Blob) object);
		}
	}

	/**
	 * 设置预定sql语句的参数
	 * 
	 * @param i
	 * @param object
	 * @throws SQLException
	 */
	private void setPreparedStatement(int i, Object object) throws SQLException {
		if (object instanceof String) {
			preparedStatement.setString(i, (String) object);
		}else if (object instanceof Short) {
			preparedStatement.setShort(i, (Short) object);
		}else if (object instanceof Long) {
			preparedStatement.setLong(i, (Long) object);
		}else if (object instanceof Integer) {
			preparedStatement.setInt(i, (Integer) object);
		}else if (object instanceof Boolean) {
			preparedStatement.setBoolean(i, (Boolean) object);
		}else if (object instanceof Byte) {
			preparedStatement.setByte(i, (Byte) object);
		}else if (object instanceof Double) {
			preparedStatement.setDouble(i, (Double) object);
		}else if (object instanceof Float) {
			preparedStatement.setFloat(i, (Float) object);
		}else if (object instanceof Date) {
			preparedStatement.setDate(i, (Date) object);
		}else if (object instanceof Time) {
			preparedStatement.setTime(i, (Time) object);
		}else if (object instanceof Timestamp) {
			preparedStatement.setTimestamp(i, (Timestamp) object);
		}else{
			preparedStatement.setString(i, (String) object);
		}
	}

	/**
	 * 关闭需要关闭的一切
	 * 
	 * @param ps
	 * @param psd
	 * @param conn
	 * @param poolName
	 */
	public void disconnect(PreparedStatement ps, PreparedStatement psd,
			Connection conn, String poolName) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		if (psd != null) {
			try {
				psd.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			DBdisconnect(poolName, conn);
		}
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	private String errCode;
	private String errMsg;

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}
	
	public static String wrapFuzzyQuery(String srcStr) {
		String result = srcStr;
		//适用于sqlserver
//		result = StringUtils.replace(result, "[", "[[]");
//		result = StringUtils.replace(result, "_", "[_]");
//		result = StringUtils.replace(result, "%", "[%]");
//		result = StringUtils.replace(result, "^", "[^]");
		//适用于mysql
		result = StringUtils.replace(result, "\\", "\\\\");
		result = StringUtils.replace(result, "'", "\\'");
		result = StringUtils.replace(result, "_", "\\_");
		result = StringUtils.replace(result, "%", "\\%");
		result = "%" + result + "%";
		return result;
	}
}
