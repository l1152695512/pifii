package com.yf.util.dbhelper;

import java.text.DecimalFormat;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;

public class PublicUtil {
	private final static Logger logger = Logger.getLogger(PublicUtil.class);
	
	/***
	 * 新建database区表，如果存在不操作，不存在则新建
	 * @param areaNo
	 * @throws Exception
	 */
	public static void createDatabaseTbl(String areaNo) throws Exception {
		String sql = "select 1 from user_tables where table_name ='BP_DATABASE_"+areaNo.trim()+"_TBL'".toUpperCase();
		DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
		RowSet rs = dbhelper.select(sql);
		if(!rs.next()){//不存在新建表
			logger.info("BP_DATABASE_"+areaNo.trim()+"_TBL'表不存在，准备新建中...");
			StringBuffer createSql = new StringBuffer("CREATE TABLE BP_DATABASE_"+areaNo.trim()+"_TBL (");
			createSql.append("D_NO VARCHAR2(40) NOT NULL,");//设备编号
			createSql.append("D_TYPE VARCHAR2(10) NOT NULL,");//设备类型
			createSql.append("C_ID VARCHAR2(40) NOT NULL,");//客户编号
			createSql.append("C_TYPE VARCHAR2(10) NOT NULL,");//客户类型
			createSql.append("LAST_TIME DATE,");//上次记录时间
			createSql.append("CREATE_TIME DATE,");//当前记录时间
			createSql.append("D_CURRENT NUMBER(10,2),");//电流
			createSql.append("D_VOLTAGE NUMBER(10,2),");//电压
			createSql.append("D_POWER NUMBER(10,2),");//功率
			createSql.append("KWH NUMBER(10,2),");//千瓦时
			createSql.append("REMARK VARCHAR2(255)");//备注
			createSql.append(" )");
			logger.info("新建BP_DATABASE_"+areaNo.trim()+"_TBL表sql:");
			logger.info(createSql.toString());
			boolean flag = dbhelper.executeFor(createSql.toString());
			if(flag){
				logger.info("BP_DATABASE_"+areaNo.trim()+"_TBL表新建成功！");
			}else{
				logger.info("BP_DATABASE_"+areaNo.trim()+"_TBL表新建失败,请检查创建sql！");
			}
		}else{
			logger.info("BP_DATABASE_"+areaNo.trim()+"_TBL'表已存在！");
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		DecimalFormat df = new DecimalFormat("000000");
		System.out.println(df.format(524));
		new java.sql.Date(System.currentTimeMillis());
		createDatabaseTbl("010102");
	}

}
