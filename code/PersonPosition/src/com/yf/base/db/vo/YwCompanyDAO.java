package com.yf.base.db.vo;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * YwCompany entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.yf.base.db.vo.YwCompany
 * @author MyEclipse Persistence Tools
 */

public class YwCompanyDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(YwCompanyDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(YwCompany transientInstance) {
		log.debug("saving YwCompany instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(YwCompany persistentInstance) {
		log.debug("deleting YwCompany instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public YwCompany findById(java.lang.String id) {
		log.debug("getting YwCompany instance with id: " + id);
		try {
			YwCompany instance = (YwCompany) getHibernateTemplate().get(
					"com.yf.base.db.vo.YwCompany", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(YwCompany instance) {
		log.debug("finding YwCompany instance by example");
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
		log.debug("finding YwCompany instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from YwCompany as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all YwCompany instances");
		try {
			String queryString = "from YwCompany";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public YwCompany merge(YwCompany detachedInstance) {
		log.debug("merging YwCompany instance");
		try {
			YwCompany result = (YwCompany) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(YwCompany instance) {
		log.debug("attaching dirty YwCompany instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(YwCompany instance) {
		log.debug("attaching clean YwCompany instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static YwCompanyDAO getFromApplicationContext(ApplicationContext ctx) {
		return (YwCompanyDAO) ctx.getBean("YwCompanyDAO");
	}
}