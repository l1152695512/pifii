/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.yf.base.service;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import org.hibernate.criterion.DetachedCriteria;

import org.springframework.orm.hibernate3.HibernateCallback;

import com.yf.data.base.PageList;
import com.yf.data.base.SortBy;
import com.yf.data.base.TransactionalCallBack;
import com.yf.data.base.BasicDAO;
import com.yf.base.db.vo.SysUser;
import com.yf.base.db.handler.DaoHandler;

/**
 * 
 * @version 1.0
 * 对应com.yf.base.db.vo.SysUser
 * WebRoot\WEB-INF\spring\serviceContext.xml中class改为对应的子类即可
 *
 */
public class SysUserServiceImpl implements SysUserService {;

	private DaoHandler daoHandler;

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List getAll() {
		return this.daoHandler.getSysUserDAO().findWithCriteria(DetachedCriteria.forClass(SysUser.class));
	}

	public List getAllWithOrderBy(SortBy o) {
		return this.daoHandler.getSysUserDAO().findWithCriteria(BasicDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SysUser.class)));
	}

	public PageList getByDefaultPageSize(int currentPage) {
		return this.getByPage(com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,currentPage);
	}

	public PageList getByDefaultPageSizeWithOrderBy(int currentPage,SortBy o) {
		return this.getByPageWithOrderBy(com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,currentPage,o);
	}

	public PageList getByDefaultPageSize(String currentPage) {
		return this.getByPage(com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,Integer.parseInt(currentPage));
	}

	public PageList getByDefaultPageSizeWithOrderBy(String currentPage,SortBy o) {
		return this.getByPageWithOrderBy(com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,Integer.parseInt(currentPage),o);
	}

	public PageList getByPage(int pageSize,int currentPage) {
		return this.daoHandler.getSysUserDAO().findWithCriteriaByPage(DetachedCriteria.forClass(SysUser.class),currentPage,pageSize);
	}

	public PageList getByPageWithOrderBy(int pageSize,int currentPage,SortBy o) {
		return this.daoHandler.getSysUserDAO().findWithCriteriaByPage(BasicDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SysUser.class)),currentPage,pageSize);
	}

	public PageList getByPage(String pageSize,String currentPage) {
		int pageSizeInt=Integer.parseInt(pageSize);
		int currentPageInt=Integer.parseInt(currentPage);
		return this.getByPage(pageSizeInt,currentPageInt);
	}

	public PageList getByPageWithOrderBy(String pageSize,String currentPage,SortBy o) {
		int pageSizeInt=Integer.parseInt(pageSize);
		int currentPageInt=Integer.parseInt(currentPage);
		return this.getByPageWithOrderBy(pageSizeInt,currentPageInt,o);
	}

	public PageList findByExampleByPage(SysUser vo,String pageSize,String currentPage) {
		int pageSizeInt=Integer.parseInt(pageSize);
		int currentPageInt=Integer.parseInt(currentPage);
		return this.findByExampleByPage(vo,pageSizeInt,currentPageInt);
	}

	public PageList findByExampleByPageWithOrderBy(SysUser vo,String pageSize,String currentPage,SortBy o) {
		int pageSizeInt=Integer.parseInt(pageSize);
		int currentPageInt=Integer.parseInt(currentPage);
		return this.findByExampleByPageWithOrderBy(vo,pageSizeInt,currentPageInt,o);
	}

	public PageList findByExampleByPage(SysUser vo,int pageSize,int currentPage) {
		return this.daoHandler.getSysUserDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageList findByExampleByPageWithOrderBy(SysUser vo,int pageSize,int currentPage,SortBy o) {
		return this.daoHandler.getSysUserDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageList findByExampleByDefaultPage(SysUser vo,String currentPage) {
		return this.findByExampleByPage(vo,com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,Integer.parseInt(currentPage));
	}

	public PageList findByExampleByDefaultPageWithOrderBy(SysUser vo,String currentPage,SortBy o) {
		return this.findByExampleByPageWithOrderBy(vo,com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,Integer.parseInt(currentPage),o);
	}

	public PageList findByExampleByDefaultPage(SysUser vo,int currentPage) {
		return this.findByExampleByPage(vo,com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,currentPage);
	}

	public PageList findByExampleByDefaultPageWithOrderBy(SysUser vo,int currentPage,SortBy o) {
		return this.findByExampleByPageWithOrderBy(vo,com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,currentPage,o);
	}

	public List findByExample(SysUser vo) {
		return this.daoHandler.getSysUserDAO().findExactLike(vo);
	}

	public List findByExampleWithOrderBy(SysUser vo,SortBy o) {
		return this.daoHandler.getSysUserDAO().findExactLikeWithOrderBy(vo,o);
	}

	public SysUser findById(java.lang.String id) {
		return this.daoHandler.getSysUserDAO().findById(id);
	}

	public List findByIds(java.lang.String[] ids) {
		List result = new ArrayList();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			SysUser vo = this.daoHandler.getSysUserDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List findByIdsIncludeNull(java.lang.String[] ids) {
		List result = new ArrayList();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			SysUser vo = this.daoHandler.getSysUserDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List findByProperty(String propName,Object value) {
		return this.daoHandler.getSysUserDAO().findByProperty(propName,value);
	}

	public List findByPropertyWithOrderBy(String propName,Object value,SortBy o) {
		return this.daoHandler.getSysUserDAO().findByPropertyWithOrderBy(SysUser.class, propName, value, o);
	}

	public PageList findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return this.daoHandler.getSysUserDAO().findByPropertyByPage(SysUser.class,propName,value,currentPage,pageSize);
	}

	public PageList findByPropertyByPage(String propName,Object value,String pageSize,String currentPage) {
		return this.findByPropertyByPage(propName,value,Integer.parseInt(pageSize),Integer.parseInt(currentPage));
	}

	public PageList findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,SortBy o) {
		return this.daoHandler.getSysUserDAO().findByPropertyByPageWithOrderBy(SysUser.class,propName,value,currentPage,pageSize,o);
	}

	public PageList findByPropertyByPageWithOrderBy(String propName,Object value,String pageSize,String currentPage,SortBy o) {
		return this.findByPropertyByPageWithOrderBy(propName,value,Integer.parseInt(pageSize),Integer.parseInt(currentPage),o);
	}

	public PageList findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,currentPage);
	}

	public PageList findByPropertyByDefaultPage(String propName,Object value,String currentPage) {
		return this.findByPropertyByDefaultPage(propName,value,Integer.parseInt(currentPage));
	}

	public PageList findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,SortBy o) {
		return this.daoHandler.getSysUserDAO().findByPropertyByPageWithOrderBy(SysUser.class, propName, value, currentPage, com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE, o);
	}

	public PageList findByPropertyByDefaultPageWithOrderBy(String propName,Object value,String currentPage,SortBy o) {
		return this.findByPropertyByDefaultPageWithOrderBy(propName, value, Integer.parseInt(currentPage),o);
	}

	public List findByDetachedCriteria(DetachedCriteria dc) {
		return this.daoHandler.getSysUserDAO().findWithCriteria(dc);
	}

	public PageList findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return this.daoHandler.getSysUserDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageList findByDetachedCriteriaByPage(DetachedCriteria dc,String pageSize,String currentPage) {
		return this.findByDetachedCriteriaByPage(dc,Integer.parseInt(pageSize),Integer.parseInt(currentPage));
	}

	public PageList findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,com.yf.data.create.SystemConstants.DEFAULT_PAGE_SIZE,currentPage);
	}

	public PageList findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,String currentPage) {
		return this.findByDetachedCriteriaByDefaultPage(dc,Integer.parseInt(currentPage));
	}

	public void update(SysUser vo) {
		this.daoHandler.getSysUserDAO().update(vo);
	}

	public void saveOrUpdate(SysUser vo) {
		this.daoHandler.getSysUserDAO().saveOrUpdate(vo);
	}

	public void add(SysUser vo) {
		this.daoHandler.getSysUserDAO().save(vo);
	}

	public void delete(SysUser vo) {
		this.daoHandler.getSysUserDAO().delete(vo);
	}

	public void deleteById(Serializable id) {
		this.daoHandler.getSysUserDAO().delete(SysUser.class, id);
	}

	public void deleteByIdList(List ids) {
		this.daoHandler.getSysUserDAO().deleteListByPk(SysUser.class, ids);
	}

	public void deleteByIdArray(Serializable[] ids) {
		this.daoHandler.getSysUserDAO().deleteListByPk(SysUser.class, ids);
	}

	public void deleteList(List voList) {
		this.daoHandler.getSysUserDAO().deleteList(voList);
	}

	public boolean validate(SysUser vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(SysUser.class);
	}

	public DetachedCriteria createCriteriaWithAssociatePaths(String[] associatePaths){
		DetachedCriteria detachedCriteria = this.createCriteria();
		this.daoHandler.getCommonDAO().setDetachedCriteriaInitPaths(detachedCriteria, associatePaths);
		return detachedCriteria;
	}

	public Object execute(HibernateCallback callBack){
		return this.daoHandler.getCommonDAO().execute(callBack);
	}

	public Object executeTransactional(TransactionalCallBack callBack){
		return callBack.execute(this);
	}

	public DaoHandler getDaoHandler(){
		return this.daoHandler;
	}
	public int getRowCount(){
		return this.getRowCountByDc(this.createCriteria());
	}
	public  int getRowCountByDc(DetachedCriteria dc){
		return this.daoHandler.getSysUserDAO().getRowCount(dc);
	}
}
