package com.yf.base.db.dao;
import com.yf.data.base.BasicDAO;
import com.yf.base.db.vo.SysDictionary;

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

public class SysDictionaryDAOImpl extends BasicDAO implements SysDictionaryDAO {
	private static final Log log = LogFactory.getLog(SysDictionaryDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(SysDictionary transientInstance) {
		if(log.isDebugEnabled())log.debug("saving SysDictionary instance");
		try {
			getHibernateTemplate().save(transientInstance);
			if(log.isDebugEnabled())log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysDictionary persistentInstance) {
		if(log.isDebugEnabled())log.debug("deleting SysDictionary instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			if(log.isDebugEnabled())log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysDictionary findById(java.lang.String id) {
		if(log.isDebugEnabled())log.debug("getting SysDictionary instance with id: " + id);
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
		if(log.isDebugEnabled())log.debug("finding SysDictionary instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			if(log.isDebugEnabled())log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		if(log.isDebugEnabled())log.debug("finding SysDictionary instance with property: "
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
		if(log.isDebugEnabled())log.debug("finding all SysDictionary instances");
		try {
			String queryString = "from SysDictionary";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysDictionary merge(SysDictionary detachedInstance) {
		if(log.isDebugEnabled())log.debug("merging SysDictionary instance");
		try {
			SysDictionary result = (SysDictionary) getHibernateTemplate()
					.merge(detachedInstance);
			if(log.isDebugEnabled())log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysDictionary instance) {
		if(log.isDebugEnabled())log.debug("attaching dirty SysDictionary instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			if(log.isDebugEnabled())log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysDictionary instance) {
		if(log.isDebugEnabled())log.debug("attaching clean SysDictionary instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			if(log.isDebugEnabled())log.debug("attach successful");
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
