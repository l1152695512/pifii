package com.yf.base.db.vo;

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

public class SysUsergroupActionDAO extends HibernateDaoSupport  {
		 private static final Log log = LogFactory.getLog(SysUsergroupActionDAO.class);
	

	protected void initDao() {
		//do nothing
	}
    
    public void save(SysUsergroupAction transientInstance) {
        log.debug("saving SysUsergroupAction instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(SysUsergroupAction persistentInstance) {
        log.debug("deleting SysUsergroupAction instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public SysUsergroupAction findById( java.lang.String id) {
        log.debug("getting SysUsergroupAction instance with id: " + id);
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
        log.debug("finding SysUsergroupAction instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding SysUsergroupAction instance with property: " + propertyName
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
		log.debug("finding all SysUsergroupAction instances");
		try {
			String queryString = "from SysUsergroupAction";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public SysUsergroupAction merge(SysUsergroupAction detachedInstance) {
        log.debug("merging SysUsergroupAction instance");
        try {
            SysUsergroupAction result = (SysUsergroupAction) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SysUsergroupAction instance) {
        log.debug("attaching dirty SysUsergroupAction instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(SysUsergroupAction instance) {
        log.debug("attaching clean SysUsergroupAction instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static SysUsergroupActionDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (SysUsergroupActionDAO) ctx.getBean("SysUsergroupActionDAO");
	}
}