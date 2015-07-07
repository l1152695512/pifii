package com.cnvp.paladin.pifii.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;


import com.alibaba.druid.pool.DruidDataSource;
import com.cnvp.paladin.kit.PropertyKit;

public class DruidU {
	public static Logger log=LogU.get();
	private DruidU(){
		
	}

	public static Connection getConnect(){
		DruidDataSource dds=getDruidDataSource();
		try {
			return dds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("druid获取connection异常",e);
		}
		return null;
	}
	public static DruidDataSource getDruidDataSource() {  
		DruidDataSource dds = new DruidDataSource();  
      String user=  PropertyKit.get("jdbc.username");
      String jdbcUrl= PropertyKit.get("jdbc.url");
      String passwd=PropertyKit.get("jdbc.password");
      String driver=PropertyKit.get("jdbc.driverClassName");
        dds.setUsername(user);  
        dds.setUrl(jdbcUrl);  
        dds.setPassword(passwd);  
        dds.setDriverClassName(driver);  
//        dds.setInitialSize(initialSize);  
//        dds.setMaxActive(maxPoolSize);  
//        dds.setMaxWait(maxIdleTime);  
        dds.setTestWhileIdle(false);  
        dds.setTestOnReturn(false);  
        dds.setTestOnBorrow(false);  
        return dds;  
    } 
	public static void closeDruidConnection(Connection conn){
		if (conn!=null) {
			conn=null;
		}
	}
	
}
