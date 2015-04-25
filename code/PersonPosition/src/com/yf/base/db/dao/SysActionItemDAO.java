package com.yf.base.db.dao;
import com.yf.data.base.IBasicDAO;
import com.yf.base.db.vo.SysActionItem;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 	* A data access object (DAO) providing persistence and search support for SysActionItem entities.
 			* Transaction control of the save(), update() and delete() operations 
	 * @see com.yf.base.db.vo.SysActionItem
  * @author MyEclipse Persistence Tools 
 */

public interface SysActionItemDAO extends IBasicDAO {
	
		//do nothing
    
    public abstract  void save(SysActionItem transientInstance) ;
    
	public abstract  void delete(SysActionItem persistentInstance) ;
    
    public abstract  SysActionItem findById( java.lang.String id) ;
    
    public abstract  List findByExample(SysActionItem instance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

	public abstract  List findAll() ;
	
    public abstract  SysActionItem merge(SysActionItem detachedInstance) ;

    public abstract  void attachDirty(SysActionItem instance) ;
    
    public abstract  void attachClean(SysActionItem instance) ;

}
