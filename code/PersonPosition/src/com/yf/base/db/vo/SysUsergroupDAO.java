package com.yf.base.db.vo;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysUsergroup entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.SysUsergroup
 * @author MyEclipse Persistence Tools
 */

public class SysUsergroupDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SysUsergroupDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(SysUsergroup transientInstance) {
		log.debug("saving SysUsergroup instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysUsergroup persistentInstance) {
		log.debug("deleting SysUsergroup instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysUsergroup findById(java.lang.String id) {
		log.debug("getting SysUsergroup instance with id: " + id);
		try {
			SysUsergroup instance = (SysUsergroup) getHibernateTemplate().get(
					"com.yf.base.db.vo.SysUsergroup", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SysUsergroup instance) {
		log.debug("finding SysUsergroup instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding SysUsergroup instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SysUsergroup as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all SysUsergroup instances");
		try {
			String queryString = "from SysUsergroup";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysUsergroup merge(SysUsergroup detachedInstance) {
		log.debug("merging SysUsergroup instance");
		try {
			SysUsergroup result = (SysUsergroup) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysUsergroup instance) {
		log.debug("attaching dirty SysUsergroup instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysUsergroup instance) {
		log.debug("attaching clean SysUsergroup instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SysUsergroupDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (SysUsergroupDAO) ctx.getBean("SysUsergroupDAO");
	}
}