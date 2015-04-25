package com.yf.util.dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

public class DBConnectionManager {
	static private DBConnectionManager instance;// 唯一数据库连接池管理实例类
	static private int clients; // 客户连接数
	private Vector<DBConfigBean> drivers = new Vector<DBConfigBean>();// 驱动信息
	private Hashtable pools = new Hashtable();// 连接池
	private Logger logger = Logger.getLogger(this.getClass().getName());// 日志类
	/**
	 * 实例化管理类
	 */
	public DBConnectionManager() {
		this.init();
	}
	/**
	 * 得到唯一实例管理类
	 * 
	 * @return
	 */
	static synchronized public DBConnectionManager getInstance() {
		if (instance == null) {
			instance = new DBConnectionManager();
		}
		return instance;
	}
	/**
	 * 释放连接
	 * 
	 * @param name
	 * @param con
	 */
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);// 根据关键名字得到连接池
		if (pool != null)
			pool.freeConnection(con);// 释放连接
	}
	
	/**
	 * 释放连接
	 * 
	 * @param name
	 * @param con
	 */
	public void freeConnection2(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);// 根据关键名字得到连接池
		if (pool != null)
			pool.freeConnection2(con);// 释放连接
	}
	
	/**
	 * 得到一个连接根据连接池的名字name
	 * 
	 * @param name
	 * @return
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = null;
		Connection con = null;
		pool = (DBConnectionPool) pools.get(name);// 从名字中获取连接池
		con = pool.getConnection();// 从选定的连接池中获得连接
		if (con != null)
			logger.debug("得到连接1");
		return con;
	}
	/**
	 * 得到一个连接根据连接池的名字name
	 * 
	 * @param name
	 * @return
	 */
	public Connection getConnection2(String name) {
		DBConnectionPool pool = null;
		Connection con = null;
		pool = (DBConnectionPool) pools.get(name);// 从名字中获取连接池
		con = pool.getConnection();// 从选定的连接池中获得连接
		if (con != null)
			logger.debug("得到连接2");
		return con;
	}

	/**
	 * 得到一个连接，根据连接池的名字和等待时间
	 * 
	 * @param name
	 * @param time
	 * @return
	 */
	public Connection getConnection(String name, long timeout) {
		DBConnectionPool pool = null;
		Connection con = null;
		pool = (DBConnectionPool) pools.get(name);// 从名字中获取连接池
		Iterator iter = pools.keySet().iterator();
		while (iter.hasNext()) {
			iter.next().toString();
		}
		con = pool.getConnection();// 从选定的连接池中获得连接
		logger.debug("得到连接3");
		return con;
	}

	/**
	 * 释放所有连接
	 */
	public synchronized void release() {
		Enumeration allpools = pools.elements();
		while (allpools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allpools.nextElement();
			if (pool != null)
				pool.release();
		}
		pools.clear();
	}

	/**
	 * 创建新连接
	 * 
	 * @return
	 */
	public Connection newConnection(String name) {
		Connection conn =null;
		logger.info("newConnection(): new Connection");
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);// 从名字中获取连接池
		if (pool != null){
			conn = pool.newConnection();
		}
		return conn;

	}
	
	/**
	 * 创建连接池
	 * 
	 * @param props
	 */
	private void createPools(DBConfigBean dsb) {
		DBConnectionPool dbpool = new DBConnectionPool();
		dbpool.setName(dsb.getName());
		dbpool.setDriver(dsb.getDriver());
		dbpool.setUrl(dsb.getUrl());
		dbpool.setUser(dsb.getUsername());
		dbpool.setPassword(dsb.getPassword());
		dbpool.setMaxConn(dsb.getMaxconn());
		pools.put(dsb.getName(), dbpool);
	}

	/**
	 * 初始化连接池的参数
	 */
	private void init() {
		// 加载驱动程序
		this.loadDrivers();
		// 创建连接池
		Iterator alldriver = drivers.iterator();
		while (alldriver.hasNext()) {
			this.createPools((DBConfigBean) alldriver.next());
			logger.debug("创建连接池");

		}
		logger.debug("创建连接池完毕");
	}

	/**
	 * 加载驱动程序
	 * 
	 * @param props
	 */
	private void loadDrivers() {
		LoadPoolConfig pd = LoadPoolConfig.getInstanse();
		// 读取数据库配置文件
		drivers = pd.getPoolNameVector();
		logger.debug("加载驱动程序");
	}

	/**
	 * 获取jdbc新连接对象
	 * @param name
	 * @return
	 */
	public Connection getJDBCConn(String configName) {
		Iterator alldriver = drivers.iterator();
		Connection con = null;
		while (alldriver.hasNext()) {
			DBConfigBean dbConfigBean = (DBConfigBean) alldriver.next();
			if (configName.equals(dbConfigBean.getName())) {
				try {
					Class.forName(dbConfigBean.getDriver());
					con = DriverManager.getConnection(dbConfigBean.getUrl(), dbConfigBean.getUsername(), dbConfigBean.getPassword());
					logger.debug("JDBC获取连接成功");
				} catch (ClassNotFoundException e) {
					logger.debug("JDBC加载驱动失败");
					e.printStackTrace();
				} catch (SQLException e) {
					logger.debug("JDBC获取连接失败");
					e.printStackTrace();
				}
			}
		}
		return con;
	}

}
