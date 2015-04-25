package com.yf.util.dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;

import org.apache.log4j.Logger;

public class DBConnectionPool {
	private static final Logger logger = Logger.getLogger(DBConnectionPool.class);
	private Connection con = null;

	private int inUsed = 0; // 使用的连接数

	private LinkedList freeConnections = new LinkedList();// 容器，空闲连接

	private int minConn; // 最小连接数

	private int maxConn; // 最大连接

	private String name; // 连接池名字

	private String password; // 密码

	private String url; // 数据库连接地址

	private String driver; // 驱动

	private String user; // 用户名

	public Timer timer; // 定时

	/**
	 * 
	 */
	public DBConnectionPool() {
	}

	/**
	 * 创建连接池
	 * 
	 * @param driver
	 * @param name
	 * @param URL
	 * @param user
	 * @param password
	 * @param maxConn
	 */
	public DBConnectionPool(String name, String driver, String URL,
			String user, String password, int maxConn) {
		this.name = name;
		this.driver = driver;
		this.url = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
	}

	/**
	 * 用完，释放连接
	 * 
	 * @param con
	 */
	public synchronized void freeConnection(Connection con) {
		if(con==null){
			logger.error(String.format("freeConnection()释放连接池[%s:%s]连接失败 : inused=%d,连接为空",this.name,this.url,this.inUsed));
			return;
		}
		logger.info("释放连接，freeConnection()->hashcode=" + con.hashCode());
		try {
			if(con.isClosed()){
				this.inUsed--;
				return ;
			}
		} catch (SQLException e) {
			logger.error("freeConnection(),释放连接池,判断关闭异常", e);
			
		}
		
		this.freeConnections.add(con);// 添加到空闲连接的末尾
		this.inUsed--;
		if(this.inUsed<0){
			logger.error(String.format("连接池[%s:%s]状态不正确 : inused=%d",this.name,this.url,this.inUsed));
			boolean flag=true;
			LinkedList tempFreeConnections = new LinkedList();
			for(int i=0;i<this.freeConnections.size();i++){
				flag=true;
				for(int j=0;j<tempFreeConnections.size();j++){
					if(this.freeConnections.get(i) == tempFreeConnections.get(j)){
						flag = false;
						this.inUsed++;
						break;
					}
				}
				if(flag){
					tempFreeConnections.add(this.freeConnections.get(i));
				}
			}
			this.freeConnections = tempFreeConnections;
			if(this.inUsed<0){
				logger.error(String.format("调整连接池[%s:%s]状态失败 : inused=%d",this.name,this.url,this.inUsed));
			}
		}
	}
	


	/**
	 * 用完，释放连接
	 * 
	 * @param con
	 */
	public synchronized void freeConnection2(Connection con) {
		if(con==null){
			logger.error(String.format("freeConnection2()释放连接池[%s:%s]连接失败 : inused=%d,连接为空",this.name,this.url,this.inUsed));
			return;
		}
		logger.info("释放连接，freeConnection2()->hashcode=" + con.hashCode());
		try {
			if(con.isClosed()){
				this.inUsed--;
				return ;
			}
		} catch (SQLException e) {
			logger.error("freeConnection2(),释放2连接池,判断关闭异常", e);
			
		}
		this.freeConnections.add(con);// 添加到空闲连接的末尾
		this.inUsed--;
		if(this.inUsed<0){
			logger.error(String.format("连接池[%s:%s]状态不正确 : inused=%d",this.name,this.url,this.inUsed));
			boolean flag=true;
			LinkedList tempFreeConnections = new LinkedList();
			for(int i=0;i<this.freeConnections.size();i++){
				flag=true;
				for(int j=0;j<tempFreeConnections.size();j++){
					if(this.freeConnections.get(i) == tempFreeConnections.get(j)){
						flag = false;
						this.inUsed++;
						break;
					}
				}
				if(flag){
					tempFreeConnections.add(this.freeConnections.get(i));
				}
			}
			this.freeConnections = tempFreeConnections;
			if(this.inUsed<0){
				logger.error(String.format("调整连接池[%s:%s]状态失败 : inused=%d",this.name,this.url,this.inUsed));
			}
		}
	}


	/**
	 * timeout 根据timeout得到连接
	 * 
	 * @param timeout
	 * @return
	 */
//	public synchronized Connection getConnection(long timeout) {
//		Connection con = null;
//		if (this.freeConnections.size() > 0) {
//			con = (Connection) this.freeConnections.get(0);
//			if (con == null)
//				con = getConnection(timeout); // 继续获得连接
//		} else {
//			con = newConnection(); // 新建连接
//		}
//		if (this.maxConn == 0 || this.maxConn < this.inUsed) {
//			con = null;// 达到最大连接数，暂时不能获得连接了。
//		}
//		if (con != null) {
//			this.inUsed++;
//		}
//		return con;
//	}

	/**
	 * 
	 * 从连接池里得到连接
	 * 
	 * @return
	 */
	public synchronized Connection getConnection() {
		Connection con = (Connection) this.freeConnections.poll();
		try {
			if(con != null&&!con.isClosed()){
				logger.debug("得到　" + this.name + "　的连接，现有" + inUsed
						+ "个连接在使用!" +",getConnection()->hashcode=" + con.hashCode());
			}else{
				con = newConnection();
			}
		} catch (SQLException e) {
			logger.error(e, new Throwable(e.getMessage()));
		}
		if (this.maxConn == 0 || this.maxConn < this.inUsed) {
			con = null;// 达到最大连接数，暂时不能获得连接了。
		}
		if (con != null) {
			this.inUsed++;
		}
		return con;
	}
	
	/**
	 * 
	 * 从连接池里得到连接
	 * 
	 * @return
	 */
	public synchronized Connection getConnection2() {
		Connection con = (Connection) this.freeConnections.poll();
		if(con != null){
			logger.debug("得到　" + this.name + "　的连接，现有" + inUsed
					+ "个连接在使用!" + "getConnection2()->hashcode=" + con.hashCode());
		}else{
			con = newConnection();
		}
		if (this.maxConn == 0 || this.maxConn < this.inUsed) {
			con = null;// 达到最大连接数，暂时不能获得连接了。
		}
		if (con != null) {
			this.inUsed++;
		}
		return con;
	}

	/**
	 * 释放全部连接
	 * 
	 */
	public synchronized void release() {
		logger.info("release(),开始释放全部连接。");
		Iterator allConns = this.freeConnections.iterator();
		while (allConns.hasNext()) {
			Connection con = (Connection) allConns.next();
			try {
				logger.debug("release()->hashcode=" + con.hashCode());
				con.close();
			} catch (Exception e) {
				logger.error("关闭连接时出现异常",e);
				e.printStackTrace();
			}

		}
		this.freeConnections.clear();
		logger.info("release(),释放全部连接结束。");
	}

	/**
	 * 创建新连接
	 * 
	 * @return
	 */
	public Connection newConnection() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("sorry can't find db driver!", e);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			logger.error("sorry can't create Connection!", e1);
		}
		return con;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the maxConn
	 */
	public int getMaxConn() {
		return maxConn;
	}

	/**
	 * @param maxConn
	 *            the maxConn to set
	 */
	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	/**
	 * @return the minConn
	 */
	public int getMinConn() {
		return minConn;
	}

	/**
	 * @param minConn
	 *            the minConn to set
	 */
	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
}
