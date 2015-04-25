package com.yf.hibernate;

import java.util.List;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.ForeignKey;

public class ReverseStrategy extends DelegatingReverseEngineeringStrategy {

	public ReverseStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	@Override
	public String tableToClassName(TableIdentifier tableIdentifier) {
		String tableClassName = super.tableToClassName(tableIdentifier);
		return tableClassName.substring(0,tableClassName.length()-3);//为了避免数据库表名使用特殊关键字导致Hibernate生成的SQL失效，每个表名都有TBL三个字母做结尾。
	}

	@Override
	public String foreignKeyToCollectionName(String keyname,
			TableIdentifier fromTable, List fromColumns,
			TableIdentifier referencedTable, List referencedColumns,
			boolean uniqueReference) {
		String collectionName = super.foreignKeyToCollectionName(keyname, fromTable, fromColumns,
				referencedTable, referencedColumns, uniqueReference);
		if(collectionName.endsWith("Tbls")) collectionName = collectionName.substring(0,collectionName.length()-4) + "s";
		return collectionName;
	}

	@Override
	public String foreignKeyToEntityName(String keyname,
			TableIdentifier fromTable, List fromColumnNames,
			TableIdentifier referencedTable, List referencedColumnNames,
			boolean uniqueReference) {
		String entityName = super.foreignKeyToEntityName(keyname, fromTable, fromColumnNames,
				referencedTable, referencedColumnNames, uniqueReference);
		if(entityName.endsWith("Tbl")) {
			entityName = entityName.substring(0,entityName.length()-3);
		}else if(entityName.indexOf("TblBy")!=-1){
			entityName = entityName.substring(0,entityName.indexOf("TblBy"))+entityName.substring(entityName.indexOf("TblBy")+5);
		}
		return entityName;
	}

	@Override
	public boolean isOneToOne(ForeignKey foreignKey) {
		//if(foreignKey.getName().contains("_121_")) return true;
		return super.isOneToOne(foreignKey);
	}

	@Override
	public void setSettings(ReverseEngineeringSettings settings) {
		super.setSettings(settings);
	}

	
	
}
