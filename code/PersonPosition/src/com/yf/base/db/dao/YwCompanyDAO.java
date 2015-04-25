package com.yf.base.db.dao;
import com.yf.data.base.IBasicDAO;
import com.yf.base.db.vo.YwCompany;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * YwCompany entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.YwCompany
 * @author MyEclipse Persistence Tools
 */

public interface YwCompanyDAO extends IBasicDAO {

		// do nothing

	public abstract  void save(YwCompany transientInstance) ;

	public abstract  void delete(YwCompany persistentInstance) ;

	public abstract  YwCompany findById(java.lang.String id) ;

	public abstract  List findByExample(YwCompany instance) ;

	public abstract  List findByProperty(String propertyName, Object value) ;

	public abstract  List findAll() ;

	public abstract  YwCompany merge(YwCompany detachedInstance) ;

	public abstract  void attachDirty(YwCompany instance) ;

	public abstract  void attachClean(YwCompany instance) ;

}
