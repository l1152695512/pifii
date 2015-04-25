package com.yf.base.db.dao;
import com.yf.data.base.IBasicDAO;
import com.yf.base.db.vo.YwRegion;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * YwRegion entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.YwRegion
 * @author MyEclipse Persistence Tools
 */

public interface YwRegionDAO extends IBasicDAO {

		// do nothing

	public abstract  void save(YwRegion transientInstance) ;

	public abstract  void delete(YwRegion persistentInstance) ;

	public abstract  YwRegion findById(java.lang.String id) ;

	public abstract  List findByExample(YwRegion instance) ;

	public abstract  List findByProperty(String propertyName, Object value) ;

	public abstract  List findAll() ;

	public abstract  YwRegion merge(YwRegion detachedInstance) ;

	public abstract  void attachDirty(YwRegion instance) ;

	public abstract  void attachClean(YwRegion instance) ;

}
