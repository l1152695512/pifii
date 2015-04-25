package com.yf.base.db.vo;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.SysUser
 * @author MyEclipse Persistence Tools
 */

public class SysUserDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SysUserDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(SysUser transientInstance) {
		log.debug("saving SysUser instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysUser persistentInstance) {
		log.debug("deleting SysUser instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysUser findById(java.lang.String id) {
		log.debug("getting SysUser instance with id: " + id);
		try {
			SysUser instance = (SysUser) getHibernateTemplate().get(
					"com.yf.base.db.vo.SysUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SysUser instance) {
		log.debug("finding SysUser instance by example");
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
		log.debug("finding SysUser instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from SysUser as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all SysUser instances");
		try {
			String queryString = "from SysUser";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysUser merge(SysUser detachedInstance) {
		log.debug("merging SysUser instance");
		try {
			SysUser result = (SysUser) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysUser instance) {
		log.debug("attaching dirty SysUser instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysUser instance) {
		log.debug("attaching clean SysUser instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SysUserDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SysUserDAO) ctx.getBean("SysUserDAO");
	}
}