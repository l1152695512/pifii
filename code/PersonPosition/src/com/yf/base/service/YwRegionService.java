/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.yf.base.service;

import java.util.List;
import com.yf.service.IService;
import com.yf.data.base.PageList;
import com.yf.data.base.SortBy;
import com.yf.data.base.TransactionalCallBack;
import com.yf.base.db.handler.DaoHandler;

import org.hibernate.criterion.DetachedCriteria;

import org.springframework.orm.hibernate3.HibernateCallback;

import java.io.Serializable;
import com.yf.base.db.vo.YwRegion;

public interface YwRegionService extends IService<YwRegion,java.lang.String> {
	/**
	 * 获得所有数据记录
	 * @return 包含所有记录java.util.List;
	 */
	public List getAll();
	/**
	 * 获得所有数据记录
	 * @param o 排序对象{@link SortBy}
	 * @return 包含所有记录java.util.List;
	 */
	public List getAllWithOrderBy(SortBy o);
	/**
	 * 返回默认的分页对象，默认页大小参见{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByDefaultPageSize(int currentPage);
	/**
	 * 返回默认的分页对象，默认页大小参见{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByDefaultPageSizeWithOrderBy(int currentPage,SortBy o);
	/**
	 * 返回默认的分页对象，默认页大小参见{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByDefaultPageSize(String currentPage);
	/**
	 * 返回默认的分页对象，默认页大小参见{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByDefaultPageSizeWithOrderBy(String currentPage,SortBy o);
	/**
	 * 返回指定页面大小的分页对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByPage(int pageSize,int currentPage);
	/**
	 * 返回指定页面大小的分页对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByPageWithOrderBy(int pageSize,int currentPage,SortBy o);
	/**
	 * 返回指定页面大小的分页对象
	 * @param pageSize 页面大小，可以转换为整数的字符串，方便在JSP调用
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByPage(String pageSize,String currentPage);
	/**
	 * 返回指定页面大小的分页对象
	 * @param pageSize 页面大小，可以转换为整数的字符串，方便在JSP调用
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList getByPageWithOrderBy(String pageSize,String currentPage,SortBy o);
	/**
	 * 根据给定的vo进行查询，并返回指定页面大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param pageSize 页面大小，可以转换为整数的字符串，方便在JSP调用
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByPage(YwRegion vo,String pageSize,String currentPage);
	/**
	 * 根据给定的vo进行查询，并返回指定页面大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param pageSize 页面大小，可以转换为整数的字符串，方便在JSP调用
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByPageWithOrderBy(YwRegion vo,String pageSize,String currentPage,SortBy o);
	/**
	 * 根据给定的vo进行查询，并返回指定页面大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByPage(YwRegion vo,int pageSize,int currentPage);
	/**
	 * 根据给定的vo进行查询，并返回指定页面大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByPageWithOrderBy(YwRegion vo,int pageSize,int currentPage,SortBy o);
	/**
	 * 根据给定的vo进行查询，并返回默认页{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByDefaultPage(YwRegion vo,String currentPage);
	/**
	 * 根据给定的vo进行查询，并返回默认页{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param currentPage 需要返回的页，可以转换为整数的字符串，方便在JSP调用
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByDefaultPageWithOrderBy(YwRegion vo,String currentPage,SortBy o);
	/**
	 * 根据给定的vo进行查询，并返回默认页{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByDefaultPage(YwRegion vo,int currentPage);
	/**
	 * 根据给定的vo进行查询，并返回默认页{@link com.westwind.constants.SystemConstants.DEFAULT_PAGE_SIZE}大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList findByExampleByDefaultPageWithOrderBy(YwRegion vo,int currentPage,SortBy o);
	/**
	 * 根据给定的vo进行查询,返回所有符合条件的vo
	 * @param vo 设置了属性的vo对象
	 * @return 结果
	 */
	public List findByExample(YwRegion vo);
	/**
	 * 根据给定的vo进行查询,返回所有符合条件的vo
	 * @param vo 设置了属性的vo对象
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果
	 */
	public List findByExampleWithOrderBy(YwRegion vo,SortBy o);
	/**
	 * 根据ID查找vo
	 * @param id
	 * @return vo
 	 */
	public YwRegion findById(java.lang.String id);
	/**
	 * 根据ID列表查找vo 列表,不包括找不到的对象。
	 * @param ids
	 * @return vo 列表
 	 */
	public List findByIds(java.lang.String[] ids);
	/**
	 * 根据ID列表查找vo 列表，找不到的对象用null代替。
	 * @param ids
	 * @return vo 列表
 	 */
	public List findByIdsIncludeNull(java.lang.String[] ids);
	/**
	 * 根据某个属性的值查找vo
	 * @param propName
	 * @param value
	 * @return 结果列表
 	 */
	public List findByProperty(String propName,Object value);
	/**
	 * 根据某个属性的值查找vo，并排序
	 * @param propName
	 * @param value
	 * @param o 排序对象{@link SortBy}
	 * @return 结果列表
 	 */
	public List findByPropertyWithOrderBy(String propName,Object value,SortBy o);
	/**
	 * 根据某个属性的值查找vo,分页
	 * @param propName
	 * @param value
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByPage(String propName,Object value,int pageSize,int currentPage);
	/**
	 * 根据某个属性的值查找vo,分页
	 * @param propName
	 * @param value
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByPage(String propName,Object value,String pageSize,String currentPage);
	/**
	 * 根据某个属性的值查找vo，并排序
	 * @param propName
	 * @param value
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,SortBy o);
	/**
	 * 根据某个属性的值查找vo，并排序分页
	 * @param propName
	 * @param value
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByPageWithOrderBy(String propName,Object value,String pageSize,String currentPage,SortBy o);
	/**
	 * 根据某个属性的值查找vo，按默认大小分页
	 * @param propName
	 * @param value
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByDefaultPage(String propName,Object value,int currentPage);
	/**
	 * 根据某个属性的值查找vo，按默认大小分页
	 * @param propName
	 * @param value
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByDefaultPage(String propName,Object value,String currentPage);
	/**
	 * 根据某个属性的值查找vo，并排序，按默认大小分页
	 * @param propName
	 * @param value
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link SortBy}
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,SortBy o);
	/**
	 * 根据某个属性的值查找vo，并排序，按默认大小分页
	 * @param propName
	 * @param value
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象
 	 */
	public PageList findByPropertyByDefaultPageWithOrderBy(String propName,Object value,String currentPage,SortBy o);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @return 结果列表
 	 */
	public List findByDetachedCriteria(DetachedCriteria dc);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，并分页
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，并分页
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByDetachedCriteriaByPage(DetachedCriteria dc,String pageSize,String currentPage);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，并按默认大小分页
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，并按默认大小分页
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,String currentPage);
	/**
	 * 更新给定的vo，根据vo的id决定更新的是哪个vo
	 * @param vo 给定的vo
	 */
	public void update(YwRegion vo);
	/**
	 * 更新给定的vo，根据vo的id是否是unsave-value决定更新还是插入vo到数据库
	 * @param vo 给定的vo
	 */
	public void saveOrUpdate(YwRegion vo);
	/**
	 * 添加给定的vo到数据库
	 * @param vo 给定的vo
	 */
	public void add(YwRegion vo);
	/**
	 * 根据id从数据库删除vo对应的记录
	 * @param id
	 */
	public void deleteById(Serializable id);
	/**
	 * 根据id列表从数据库删除vo对应的记录
	 * @param ids
	 */
	public void deleteByIdList(List ids);
	/**
	 * 根据id列表从数据库删除vo对应的记录
	 * @param ids
	 */
	public void deleteByIdArray(Serializable[] ids);
	/**
	 * 删除vo列表
	 * @param voList
	 */
	public void deleteList(List voList);
	/**
	 * 从数据库删除vo对应的记录
	 * @param vo
	 */
	public void delete(YwRegion vo);
	/**
	 * 检查vo的合法性，本方法默认是返回true，即所有vo都是合法的。可以根据实际情况在子类改写该方法
	 * @param vo 需要检测的vo
	 * @return
	 */
	public boolean validate(YwRegion vo);
	/**
	 * 获得离线查询对象的快捷方法
	 * @return 对应 泛型 T的DetachedCriteria 对象
	 */
	public DetachedCriteria createCriteria();
	/**
	 * 获得离线查找对象，并设定初始化对象路径。see also {@link BasicDAO.setDetachedCriteriaInitPaths(DetachedCriteria detachedCriteria,String[] associatePaths)}
	 * 注意，如果使用此方法获得离线查询对象自动带有对应路径的别名，例如xxx.yyy.zzz.vvv,则有别名: xxx,xxx_yyy,xxx_yyy_zzz,xxx_yyy_zzz_vvv ，如果后续设置条件，则必须沿用这些别名
	 * @param associatePaths 以"."分割的属性查询链条（例如xxx.yyy.zzz）所组成的数组
	 * @return 离线查询对象
	 */
	public DetachedCriteria createCriteriaWithAssociatePaths(String[] associatePaths);
	/**
	 * 直接使用Hibernate Session ，处理复杂的查询，例如大数据量迭代的更新、ScrollabelResults控制数据库的cursor等。
	 * @param callBack 
	 * @return 处理结果，可能是null(void)或者List等表示结果的对象
	 */
	public Object execute(HibernateCallback callBack);
	/**
	 * 曝露事务回调接口。
	 * @param callBack 
	 * @return 处理结果或者Json对象等可以直接使用的对象，避免lazy-init、lazy-load异常
	 */
	public Object executeTransactional(TransactionalCallBack callBack);
	/**
	 * 直接获取DaoHandler
	 * @return DaoHandler
	 */
	public DaoHandler getDaoHandler();
	/**
	 * 获得记录行数
	 * @return 行数
	 */
	public int getRowCount();
	/**
	 * 通过DetachedCriteria获得记录行数
	 * @param callBack 
	 * @return 行数
	 */
	public int getRowCountByDc(DetachedCriteria dc);
}
