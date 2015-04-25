package com.yf.base.db.dao;
import com.yf.data.base.BasicDAO;
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
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see com.yf.base.db.vo.SysUsergroupAction
  * @author MyEclipse Persistence Tools 
 */

public class SysUsergroupActionDAOImpl extends BasicDAO implements SysUsergroupActionDAO {
		 private static final Log log = LogFactory.getLog(SysUsergroupActionDAO.class);
	
	protected void initDao() {
		//do nothing
	}
    
    public void save(SysUsergroupAction transientInstance) {
        if(log.isDebugEnabled())log.debug("saving SysUsergroupAction instance");
        try {
            getHibernateTemplate().save(transientInstance);
            if(log.isDebugEnabled())log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(SysUsergroupAction persistentInstance) {
        if(log.isDebugEnabled())log.debug("deleting SysUsergroupAction instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            if(log.isDebugEnabled())log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public SysUsergroupAction findById( java.lang.String id) {
        if(log.isDebugEnabled())log.debug("getting SysUsergroupAction instance with id: " + id);
        try {
            SysUsergroupAction instance = (SysUsergroupAction) getHibernateTemplate()
                    .get("com.yf.base.db.vo.SysUsergroupAction", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findByExample(SysUsergroupAction instance) {
        if(log.isDebugEnabled())log.debug("finding SysUsergroupAction instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            if(log.isDebugEnabled())log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      if(log.isDebugEnabled())log.debug("finding SysUsergroupAction instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from SysUsergroupAction as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findAll() {
		if(log.isDebugEnabled())log.debug("finding all SysUsergroupAction instances");
		try {
			String queryString = "from SysUsergroupAction";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public SysUsergroupAction merge(SysUsergroupAction detachedInstance) {
        if(log.isDebugEnabled())log.debug("merging SysUsergroupAction instance");
        try {
            SysUsergroupAction result = (SysUsergroupAction) getHibernateTemplate()
                    .merge(detachedInstance);
            if(log.isDebugEnabled())log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SysUsergroupAction instance) {
        if(log.isDebugEnabled())log.debug("attaching dirty SysUsergroupAction instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            if(log.isDebugEnabled())log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(SysUsergroupAction instance) {
        if(log.isDebugEnabled())log.debug("attaching clean SysUsergroupAction instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            if(log.isDebugEnabled())log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static SysUsergroupActionDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (SysUsergroupActionDAO) ctx.getBean("SysUsergroupActionDAO");
	}
}
