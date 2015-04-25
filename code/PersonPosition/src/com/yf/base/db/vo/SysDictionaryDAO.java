package com.yf.base.db.vo;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysDictionary entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.SysDictionary
 * @author MyEclipse Persistence Tools
 */

public class SysDictionaryDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SysDictionaryDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(SysDictionary transientInstance) {
		log.debug("saving SysDictionary instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysDictionary persistentInstance) {
		log.debug("deleting SysDictionary instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysDictionary findById(java.lang.String id) {
		log.debug("getting SysDictionary instance with id: " + id);
		try {
			SysDictionary instance = (SysDictionary) getHibernateTemplate()
					.get("com.yf.base.db.vo.SysDictionary", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SysDictionary instance) {
		log.debug("finding SysDictionary instance by example");
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
		log.debug("finding SysDictionary instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SysDictionary as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all SysDictionary instances");
		try {
			String queryString = "from SysDictionary";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysDictionary merge(SysDictionary detachedInstance) {
		log.debug("merging SysDictionary instance");
		try {
			SysDictionary result = (SysDictionary) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysDictionary instance) {
		log.debug("attaching dirty SysDictionary instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysDictionary instance) {
		log.debug("attaching clean SysDictionary instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SysDictionaryDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (SysDictionaryDAO) ctx.getBean("SysDictionaryDAO");
	}
}