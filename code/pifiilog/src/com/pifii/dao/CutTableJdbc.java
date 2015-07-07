package com.pifii.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.dbutils.DbUtils;

import com.pifii.util.DBUtil;
import com.pifii.util.Log;
import com.pifii.util.ResourcesUtil;
public class CutTableJdbc {
	 private static Connection conn =DBUtil.openConnection(ResourcesUtil.getVbyKey("jdbcUrl"), ResourcesUtil.getVbyKey("user"), ResourcesUtil.getVbyKey("pass"));
	 private static  Statement stmt =null;
	 private static org.slf4j.Logger log = Log.get();
	
	public static void reamTableName(boolean s,String oldtablename,String rual){
		try {
			stmt = conn.createStatement(); 
			log.debug("alter TABLE  '"+oldtablename+"' rename to '"+oldtablename+rual+"' ");
			if(s){
				stmt.executeUpdate("alter TABLE "+oldtablename+" rename to "+oldtablename+rual+" ");
				createTable(oldtablename,oldtablename+rual);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断 库中是否存在特定的表
	 * @return
	 */
	public static boolean isHere(String dbname,String taname){
		try {
			stmt = conn.createStatement(); 
			ResultSet res= stmt.executeQuery("select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='"+dbname+"' and `TABLE_NAME`='"+taname+"' ");
			log.debug("select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='"+dbname+"' and `TABLE_NAME`='"+taname+"' ");
			log.debug(res.toString());
			if(res.next()){
				log.debug("表已经存在了");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 创建一个表（根据已经存在的表）
	 * @param newtab
	 * @param baktab
	 */
	public static void createTable(String newtab,String baktab){
		try {
			stmt = conn.createStatement();
			String sql = "create table "+newtab+" select * from "+baktab+" where 0 ; " ; 
			stmt.executeUpdate(sql);
			String alsql = "alter table "+newtab+" modify id integer primary key auto_increment ; " ; 
			stmt.executeUpdate(alsql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param newtab 插入数据的表
	 * @param outerday 取数据的表
	 * @param outerdayday 条件 create_date eg:2015-01-01
	 */
	public static void insertTab(String newtab,String outerday,String outerdayday){
		try {
			stmt = conn.createStatement();
			String sql = "insert into  "+newtab+" (device_no,type,input_mac,ip,link,create_date) select device_no,type,input_mac,ip,link,create_date from "+outerday+" where create_date  like '"+outerdayday+"%' order by create_date limit 1;" ; 
			log.info("插入数据库的SQL"+sql);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
		创建时间：   2015年1月4日
		创建人： liting 
	 * isHere(判断表中的数据是否存在 ：可以加where条件)    
	 * @param   tab 表名称  day 日期 【规定  ：用作判断表中的日期：】    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static boolean isdureHere(String tab,String day){
		try {
			String sql = "select * from "+tab+" where create_date not like '"+day+"%' " ; 
			PreparedStatement 		Prestmt = conn.prepareStatement(sql);
			ResultSet  res= Prestmt.executeQuery();
			if(res.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 
		创建时间：   2015年1月4日
		创建人： liting 
	 * isHere(判断表中的数据是否存在 ：可以加where条件)    
	 * @param   tab 表名称  day 日期 【规定  ：用作判断表中的日期：】    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static List<String> getReplace(String tab,String day){
		List<String> lt=new ArrayList<String>();
		try {
			String sql = "select DATE_FORMAT(create_date,'%Y-%m-%d') as datatime from "+tab+" where create_date not like '"+day+"%' group by datatime  " ; 
			PreparedStatement 	Prestmt = conn.prepareStatement(sql);
			ResultSet  res= Prestmt.executeQuery();
			while(res.next()){
				lt.add(res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lt;
}
	/**
	 * 
		创建时间：   2015年1月4日
		创建人： liting 
	 * isHere(判断表中的数据是否存在 ：可以加where条件)    
	 * @param   tab 表名称  day 日期 【规定  ：用作判断表中的日期：】    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static List<String> getReplace(String tab){
		List<String> lt=new ArrayList<String>();
		try {
			String sql = "select DATE_FORMAT(create_date,'%Y-%m-%d') as datatime from "+tab+" group by create_date  " ; 
			PreparedStatement 	Prestmt = conn.prepareStatement(sql);
			ResultSet  res= Prestmt.executeQuery();
			while(res.next()){
				lt.add(res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lt;
}
	/**
	 * 
		创建时间：   2015年1月4日
		创建人： liting 
	 * insertTab(指定要插入的表的数据（都是select出来的：） 要插入的字段  例如：device_no,type,input_mac,ip,link,create_date )  不适合批量插入 【一次执行 要么成功要么失败】  
	 * @param   name    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static void insertDureTab(String tab,String newtab,String day){
		try {
			stmt = conn.createStatement();
			String sql = "insert into  "+tab+" device_no,type,input_mac,ip,link  select device_no,type,input_mac,ip,link from "+newtab+"  where  create_date like '"+day+"%'; " ; 
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
		创建时间：   2015年1月4日
		创建人： liting 
	 * deleteDureTab(删除表中的数据 传入表明和 where条件)    
	 * @param   name    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static void deleteDureTab(String newtab,String wheret){
		try {
			stmt = conn.createStatement();
			String sql = "DELETE  from  "+newtab+" where create_date like '"+wheret+"%'; " ; 
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createtables(String tabname){
		try {
			stmt=conn.createStatement();
			String sql="REATE TABLE `"+tabname+"` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `device_no` varchar(40) DEFAULT NULL COMMENT '设备编号', `type` varchar(10) DEFAULT NULL COMMENT '手机类型',`input_mac` varchar(50) DEFAULT NULL COMMENT '接入设备mac',`ip` varchar(20) DEFAULT NULL,`link` longtext COMMENT '访问地址', `create_date` datetime DEFAULT NULL,PRIMARY KEY (`id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
