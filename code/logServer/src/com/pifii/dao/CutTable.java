/*package com.pifii.dao;

import org.hibernate.EmptyInterceptor;

public class CutTable extends EmptyInterceptor{
	private String targetTableName;// 目标母表名
	private String tempTableName;// 操作子表名

	public CutTable() {}//为其在spring好好利用 我们生成公用无参构造方法

	public CutTable(String targetTableName,String tempTableName ){
		this.targetTableName=targetTableName;
		this.tempTableName=tempTableName;
		
	}
	
	public java.lang.String onPrepareStatement(java.lang.String sql) {
		sql = sql.replaceAll(targetTableName, tempTableName);
		return sql;

	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public String getTempTableName() {
		return tempTableName;
	}

	public void setTempTableName(String tempTableName) {
		this.tempTableName = tempTableName;
	}

}
*/