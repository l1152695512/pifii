package com.yf.base.db.dao;
import com.yf.data.base.IBasicDAO;
import com.yf.base.db.vo.SysDictionary;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysDictionary entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.SysDictionary
 * @author MyEclipse Persistence Tools
 */

public interface SysDictionaryDAO extends IBasicDAO {

		// do nothing

	public abstract  void save(SysDictionary transientInstance) ;

	public abstract  void delete(SysDictionary persistentInstance) ;

	public abstract  SysDictionary findById(java.lang.String id) ;

	public abstract  List findByExample(SysDictionary instance) ;

	public abstract  List findByProperty(String propertyName, Object value) ;

	public abstract  List findAll() ;

	public abstract  SysDictionary merge(SysDictionary detachedInstance) ;

	public abstract  void attachDirty(SysDictionary instance) ;

	public abstract  void attachClean(SysDictionary instance) ;

}
