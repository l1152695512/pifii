package com.yf.base.db.dao;
import com.yf.data.base.IBasicDAO;
import com.yf.base.db.vo.SysUsergroupAction;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 	* A data access object (DAO) providing persistence and search support for SysUsergroupAction entities.
 			* Transaction control of the save(), update() and delete() operations 
	 * @see com.yf.base.db.vo.SysUsergroupAction
  * @author MyEclipse Persistence Tools 
 */

public interface SysUsergroupActionDAO extends IBasicDAO {
	
		//do nothing
    
    public abstract  void save(SysUsergroupAction transientInstance) ;
    
	public abstract  void delete(SysUsergroupAction persistentInstance) ;
    
    public abstract  SysUsergroupAction findById( java.lang.String id) ;
    
    public abstract  List findByExample(SysUsergroupAction instance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

	public abstract  List findAll() ;
	
    public abstract  SysUsergroupAction merge(SysUsergroupAction detachedInstance) ;

    public abstract  void attachDirty(SysUsergroupAction instance) ;
    
    public abstract  void attachClean(SysUsergroupAction instance) ;

}
