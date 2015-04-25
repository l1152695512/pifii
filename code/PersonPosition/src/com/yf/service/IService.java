// Decompiled by DJ v2.9.9.61 Copyright 2000 Atanas Neshkov  Date: 2011-2-24 10:59:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) deadcode ansi 
// Source File Name:   IService.java

package com.yf.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.yf.data.base.PageList;
import com.yf.data.base.SortBy;
import com.yf.data.base.TransactionalCallBack;

public interface IService <T ,Serializable>
{

    public abstract List getAll();

    public abstract List getAllWithOrderBy(SortBy orderby);

    public abstract PageList getByDefaultPageSize(int i);

    public abstract PageList getByDefaultPageSizeWithOrderBy(int i, SortBy orderby);

    public abstract PageList getByDefaultPageSize(String s);

    public abstract PageList getByDefaultPageSizeWithOrderBy(String s, SortBy orderby);

    public abstract PageList getByPage(int i, int j);

    public abstract PageList getByPageWithOrderBy(int i, int j, SortBy orderby);

    public abstract PageList getByPage(String s, String s1);

    public abstract PageList getByPageWithOrderBy(String s, String s1, SortBy orderby);

    public abstract PageList findByExampleByPage(T t, String s, String s1);

    public abstract PageList findByExampleByPageWithOrderBy(T t, String s, String s1, SortBy orderby);

    public abstract PageList findByExampleByPage(T t, int i, int j);

    public abstract PageList findByExampleByPageWithOrderBy(T t, int i, int j, SortBy orderby);

    public abstract PageList findByExampleByDefaultPage(T t, String s);

    public abstract PageList findByExampleByDefaultPageWithOrderBy(T obj, String s, SortBy orderby);

    public abstract PageList findByExampleByDefaultPage(T obj, int i);

    public abstract PageList findByExampleByDefaultPageWithOrderBy(T obj, int i, SortBy orderby);

    public abstract List findByExample(T obj);

    public abstract List findByExampleWithOrderBy(T obj, SortBy orderby);

    public abstract Object findById(Serializable serializable);

    public abstract List findByIds(Serializable aserializable[]);

    public abstract List findByIdsIncludeNull(Serializable aserializable[]);

    public abstract List findByProperty(String s, Object obj);

    public abstract List findByPropertyWithOrderBy(String s, Object obj, SortBy orderby);

    public abstract PageList findByPropertyByPage(String s, Object obj, int i, int j);

    public abstract PageList findByPropertyByPage(String s, Object obj, String s1, String s2);

    public abstract PageList findByPropertyByPageWithOrderBy(String s, Object obj, int i, int j, SortBy orderby);

    public abstract PageList findByPropertyByPageWithOrderBy(String s, Object obj, String s1, String s2, SortBy orderby);

    public abstract PageList findByPropertyByDefaultPage(String s, Object obj, int i);

    public abstract PageList findByPropertyByDefaultPage(String s, Object obj, String s1);

    public abstract PageList findByPropertyByDefaultPageWithOrderBy(String s, Object obj, int i, SortBy orderby);

    public abstract PageList findByPropertyByDefaultPageWithOrderBy(String s, Object obj, String s1, SortBy orderby);

    public abstract List findByDetachedCriteria(DetachedCriteria detachedcriteria);

    public abstract PageList findByDetachedCriteriaByPage(DetachedCriteria detachedcriteria, int i, int j);

    public abstract PageList findByDetachedCriteriaByPage(DetachedCriteria detachedcriteria, String s, String s1);

    public abstract PageList findByDetachedCriteriaByDefaultPage(DetachedCriteria detachedcriteria, int i);

    public abstract PageList findByDetachedCriteriaByDefaultPage(DetachedCriteria detachedcriteria, String s);

    public abstract void update(T obj);

    public abstract void saveOrUpdate(T obj);

    public abstract void add(T obj);
    
    //aaa

   // public abstract void deleteById(Serializable serializable);
    
   // public abstract void deleteByIdArray(Serializable aserializable[]);
    
    //bbb
  

    public abstract void deleteByIdList(List list);


    public abstract void delete(T obj);

    public abstract void deleteList(List list);

    public abstract boolean validate(T obj);

    public abstract DetachedCriteria createCriteria();

    public abstract DetachedCriteria createCriteriaWithAssociatePaths(String as[]);

    public abstract Object execute(HibernateCallback hibernatecallback);

    public abstract Object executeTransactional(TransactionalCallBack transactionalcallback);

    public abstract int getRowCountByDc(DetachedCriteria detachedcriteria);

    public abstract int getRowCount();
}