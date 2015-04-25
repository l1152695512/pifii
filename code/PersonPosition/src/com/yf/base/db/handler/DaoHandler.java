/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */

package com.yf.base.db.handler;
import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import com.yf.data.base.ICommonDAO;
import org.apache.commons.logging.LogFactory;

import com.yf.base.db.dao.SysActionDAO;
import com.yf.base.db.dao.SysActionItemDAO;
import com.yf.base.db.dao.SysDictionaryDAO;
import com.yf.base.db.dao.SysMenuDAO;
import com.yf.base.db.dao.SysUserDAO;
import com.yf.base.db.dao.SysUsergroupActionDAO;
import com.yf.base.db.dao.SysUsergroupDAO;
import com.yf.base.db.dao.SysUsergroupMenusDAO;
import com.yf.base.db.dao.SysUsergroupUserDAO;
import com.yf.base.db.dao.YwCompanyDAO;
import com.yf.base.db.dao.YwRegionDAO;
public class DaoHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private ICommonDAO commonDAO;

	private SysActionDAO sysActionDAO;

	private SysActionItemDAO sysActionItemDAO;

	private SysDictionaryDAO sysDictionaryDAO;

	private SysMenuDAO sysMenuDAO;

	private SysUserDAO sysUserDAO;

	private SysUsergroupActionDAO sysUsergroupActionDAO;

	private SysUsergroupDAO sysUsergroupDAO;

	private SysUsergroupMenusDAO sysUsergroupMenusDAO;

	private SysUsergroupUserDAO sysUsergroupUserDAO;

	private YwCompanyDAO ywCompanyDAO;

	private YwRegionDAO ywRegionDAO;

	public void setCommonDAO(ICommonDAO commonDAO){
		this.commonDAO = commonDAO;
	}

	public void setSysActionDAO(SysActionDAO sysActionDAO){
		this.sysActionDAO = sysActionDAO;
	}

	public void setSysActionItemDAO(SysActionItemDAO sysActionItemDAO){
		this.sysActionItemDAO = sysActionItemDAO;
	}

	public void setSysDictionaryDAO(SysDictionaryDAO sysDictionaryDAO){
		this.sysDictionaryDAO = sysDictionaryDAO;
	}

	public void setSysMenuDAO(SysMenuDAO sysMenuDAO){
		this.sysMenuDAO = sysMenuDAO;
	}

	public void setSysUserDAO(SysUserDAO sysUserDAO){
		this.sysUserDAO = sysUserDAO;
	}

	public void setSysUsergroupActionDAO(SysUsergroupActionDAO sysUsergroupActionDAO){
		this.sysUsergroupActionDAO = sysUsergroupActionDAO;
	}

	public void setSysUsergroupDAO(SysUsergroupDAO sysUsergroupDAO){
		this.sysUsergroupDAO = sysUsergroupDAO;
	}

	public void setSysUsergroupMenusDAO(SysUsergroupMenusDAO sysUsergroupMenusDAO){
		this.sysUsergroupMenusDAO = sysUsergroupMenusDAO;
	}

	public void setSysUsergroupUserDAO(SysUsergroupUserDAO sysUsergroupUserDAO){
		this.sysUsergroupUserDAO = sysUsergroupUserDAO;
	}

	public void setYwCompanyDAO(YwCompanyDAO ywCompanyDAO){
		this.ywCompanyDAO = ywCompanyDAO;
	}

	public void setYwRegionDAO(YwRegionDAO ywRegionDAO){
		this.ywRegionDAO = ywRegionDAO;
	}

	public ICommonDAO getCommonDAO(){
		return this.commonDAO;
	}

	public SysActionDAO getSysActionDAO(){
		return this.sysActionDAO;
	}

	public SysActionItemDAO getSysActionItemDAO(){
		return this.sysActionItemDAO;
	}

	public SysDictionaryDAO getSysDictionaryDAO(){
		return this.sysDictionaryDAO;
	}

	public SysMenuDAO getSysMenuDAO(){
		return this.sysMenuDAO;
	}

	public SysUserDAO getSysUserDAO(){
		return this.sysUserDAO;
	}

	public SysUsergroupActionDAO getSysUsergroupActionDAO(){
		return this.sysUsergroupActionDAO;
	}

	public SysUsergroupDAO getSysUsergroupDAO(){
		return this.sysUsergroupDAO;
	}

	public SysUsergroupMenusDAO getSysUsergroupMenusDAO(){
		return this.sysUsergroupMenusDAO;
	}

	public SysUsergroupUserDAO getSysUsergroupUserDAO(){
		return this.sysUsergroupUserDAO;
	}

	public YwCompanyDAO getYwCompanyDAO(){
		return this.ywCompanyDAO;
	}

	public YwRegionDAO getYwRegionDAO(){
		return this.ywRegionDAO;
	}

	public void init() throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = DaoHandler.class.getDeclaredFields();
		//System.out.println(fields.length);
		for (int i = 0; i < fields.length; i++) {
			logger.debug("fields field:"+fields[i]);
			if(fields[i].getName().endsWith("DAO")){
				if(fields[i].get(this) == null)throw new RuntimeException("没有注入DAO："+fields[i].getName());
			}
		}
	}

}
