package com.yf.base.db.dao;
import com.yf.data.base.IBasicDAO;
import com.yf.base.db.vo.SysUsergroupMenus;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysUsergroupMenus entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.SysUsergroupMenus
 * @author MyEclipse Persistence Tools
 */

public interface SysUsergroupMenusDAO extends IBasicDAO {

		// do nothing

	public abstract  void save(SysUsergroupMenus transientInstance) ;

	public abstract  void delete(SysUsergroupMenus persistentInstance) ;

	public abstract  SysUsergroupMenus findById(java.lang.String id) ;

	public abstract  List findByExample(SysUsergroupMenus instance) ;

	public abstract  List findByProperty(String propertyName, Object value) ;

	public abstract  List findAll() ;

	public abstract  SysUsergroupMenus merge(SysUsergroupMenus detachedInstance) ;

	public abstract  void attachDirty(SysUsergroupMenus instance) ;

	public abstract  void attachClean(SysUsergroupMenus instance) ;

}
