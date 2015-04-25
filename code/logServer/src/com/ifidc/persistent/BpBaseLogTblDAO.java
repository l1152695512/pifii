/*     */ package com.ifidc.persistent;
/*     */ 
/*     */ import java.util.List;

/*     */ import org.hibernate.LockMode;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/*   import com.pifii.dao.CutTable;
  */ 
/*     */ public class BpBaseLogTblDAO extends HibernateDaoSupport
/*     */ {
/*  24 */   private static final Logger log = LoggerFactory.getLogger(BpBaseLogTblDAO.class);
/*     */   public static final String DEVICE_NO = "deviceNo";
/*     */   public static final String TYPE = "type";
/*     */   public static final String INPUT_MAC = "inputMac";
/*     */   public static final String IP = "ip";
/*     */   public static final String LINK = "link";
/*     */ 
/*     */   protected void initDao()
/*     */   {
/*     */   }
/*     */ 

/**
     public void saveCut(BpBaseLogTbl transientInstance)
  {
				SessionFactory sf= super.getHibernateTemplate().getSessionFactory();
				CutTable ct=new CutTable("bpbaselogtbl","bpbaselogtbl1");
				Session seon=sf.openSession(ct);
    log.debug("saving BpBaseLogTbl instance");
     try {
			//	getHibernateTemplate().save(transientInstance);
				Transaction trs= seon.beginTransaction();
				trs.begin();
				seon.save(transientInstance);
				trs.commit();
       log.debug("save successful");
   } catch (RuntimeException re) {
       log.error("save failed", re);
      throw re;
    }finally{
			seon.close();
		}
  } */
 
 

/*     */   public void save(BpBaseLogTbl transientInstance)
/*     */   {
/*  37 */     log.debug("saving BpBaseLogTbl instance");
/*     */     try {
				super.getHibernateTemplate().save(transientInstance);
/*  40 */       log.debug("save successful");
/*     */     } catch (RuntimeException re) {
/*  42 */       log.error("save failed", re);
/*  43 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(BpBaseLogTbl persistentInstance) {
/*  48 */     log.debug("deleting BpBaseLogTbl instance");
/*     */     try {
/*  50 */       getHibernateTemplate().delete(persistentInstance);
/*  51 */       log.debug("delete successful");
/*     */     } catch (RuntimeException re) {
/*  53 */       log.error("delete failed", re);
/*  54 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   public BpBaseLogTbl findById(Integer id) {
/*  59 */     log.debug("getting BpBaseLogTbl instance with id: " + id);
/*     */     try {
/*  61 */       BpBaseLogTbl instance = (BpBaseLogTbl)getHibernateTemplate().get(
/*  62 */         "com.ifidc.persistent.BpBaseLogTbl", id);
/*  63 */       return instance;
/*     */     } catch (RuntimeException re) {
/*  65 */       log.error("get failed", re);
/*  66 */     }return null;
/*     */   }
/*     */ 
/*     */   public List findByExample(BpBaseLogTbl instance)
/*     */   {
/*  71 */     log.debug("finding BpBaseLogTbl instance by example");
/*     */     try {
/*  73 */       List results = getHibernateTemplate().findByExample(instance);
/*  74 */       log.debug("find by example successful, result size: " + 
/*  75 */         results.size());
/*  76 */       return results;
/*     */     } catch (RuntimeException re) {
/*  78 */       log.error("find by example failed", re);
/*  79 */     }return null;
/*     */   }
/*     */ 
/*     */   public List findByProperty(String propertyName, Object value)
/*     */   {
/*  84 */     log.debug("finding BpBaseLogTbl instance with property: " + 
/*  85 */       propertyName + ", value: " + value);
/*     */     try {
/*  87 */       String queryString = "from BpBaseLogTbl as model where model." + 
/*  88 */         propertyName + "= ?";
/*  89 */       return getHibernateTemplate().find(queryString, value);
/*     */     } catch (RuntimeException re) {
/*  91 */       log.error("find by property name failed", re);
/*  92 */     }return null;
/*     */   }
/*     */ 
/*     */   public List findByDeviceNo(Object deviceNo)
/*     */   {
/*  97 */     return findByProperty("deviceNo", deviceNo);
/*     */   }
/*     */ 
/*     */   public List findByType(Object type) {
/* 101 */     return findByProperty("type", type);
/*     */   }
/*     */ 
/*     */   public List findByInputMac(Object inputMac) {
/* 105 */     return findByProperty("inputMac", inputMac);
/*     */   }
/*     */ 
/*     */   public List findByIp(Object ip) {
/* 109 */     return findByProperty("ip", ip);
/*     */   }
/*     */ 
/*     */   public List findByLink(Object link) {
/* 113 */     return findByProperty("link", link);
/*     */   }
/*     */ 
/*     */   public List findAll() {
/* 117 */     log.debug("finding all BpBaseLogTbl instances");
/*     */     try {
/* 119 */       String queryString = "from BpBaseLogTbl";
/* 120 */       return getHibernateTemplate().find(queryString);
/*     */     } catch (RuntimeException re) {
/* 122 */       log.error("find all failed", re);
/* 123 */     }return null;
/*     */   }
/*     */ 
/*     */   public BpBaseLogTbl merge(BpBaseLogTbl detachedInstance)
/*     */   {
/* 128 */     log.debug("merging BpBaseLogTbl instance");
/*     */     try {
/* 130 */       BpBaseLogTbl result = (BpBaseLogTbl)getHibernateTemplate().merge(
/* 131 */         detachedInstance);
/* 132 */       log.debug("merge successful");
/* 133 */       return result;
/*     */     } catch (RuntimeException re) {
/* 135 */       log.error("merge failed", re);
/* 136 */     }return null;
/*     */   }
/*     */ 
/*     */   public void attachDirty(BpBaseLogTbl instance)
/*     */   {
/* 141 */     log.debug("attaching dirty BpBaseLogTbl instance");
/*     */     try {
/* 143 */       getHibernateTemplate().saveOrUpdate(instance);
/* 144 */       log.debug("attach successful");
/*     */     } catch (RuntimeException re) {
/* 146 */       log.error("attach failed", re);
/* 147 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void attachClean(BpBaseLogTbl instance) {
/* 152 */     log.debug("attaching clean BpBaseLogTbl instance");
/*     */     try {
/* 154 */       getHibernateTemplate().lock(instance, LockMode.NONE);
/* 155 */       log.debug("attach successful");
/*     */     } catch (RuntimeException re) {
/* 157 */       log.error("attach failed", re);
/* 158 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static BpBaseLogTblDAO getFromApplicationContext(ApplicationContext ctx)
/*     */   {
/* 164 */     return (BpBaseLogTblDAO)ctx.getBean("BpBaseLogTblDAO");
/*     */   }
/*     */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.ifidc.persistent.BpBaseLogTblDAO
 * JD-Core Version:    0.6.0
 */