package com.yf.base.db.dao;
import com.yf.data.base.IBasicDAO;
import com.yf.base.db.vo.SysAction;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 	* A data access object (DAO) providing persistence and search support for SysAction entities.
 			* Transaction control of the save(), update() and delete() operations 
	 * @see com.yf.base.db.vo.SysAction
  * @author MyEclipse Persistence Tools 
 */

public interface SysActionDAO extends IBasicDAO {
	
		//do nothing
    
    public abstract  void save(SysAction transientInstance) ;
    
	public abstract  void delete(SysAction persistentInstance) ;
    
    public abstract  SysAction findById( java.lang.String id) ;
    
    public abstract  List findByExample(SysAction instance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

	public abstract  List findAll() ;
	
    public abstract  SysAction merge(SysAction detachedInstance) ;

    public abstract  void attachDirty(SysAction instance) ;
    
    public abstract  void attachClean(SysAction instance) ;

}
