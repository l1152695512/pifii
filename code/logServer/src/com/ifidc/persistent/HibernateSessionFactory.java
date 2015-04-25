/*     */ package com.ifidc.persistent;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import org.hibernate.HibernateException;
/*     */ import org.hibernate.Session;
/*     */ import org.hibernate.SessionFactory;
/*     */ import org.hibernate.cfg.Configuration;
/*     */ 
/*     */ public class HibernateSessionFactory
/*     */ {
/*  22 */   private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
/*  23 */   private static final ThreadLocal<Session> threadLocal = new ThreadLocal();
/*  24 */   private static Configuration configuration = new Configuration();
/*     */   private static SessionFactory sessionFactory;
/*  26 */   private static String configFile = CONFIG_FILE_LOCATION;
/*     */ 
/*     */   static {
/*     */     try {
/*  30 */       configuration.configure(configFile);
/*  31 */       sessionFactory = configuration.buildSessionFactory();
/*     */     } catch (Exception e) {
/*  33 */       System.err
/*  34 */         .println("%%%% Error Creating SessionFactory %%%%");
/*  35 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Session getSession()
/*     */     throws HibernateException
/*     */   {
/*  49 */     Session session = (Session)threadLocal.get();
/*     */ 
/*  51 */     if ((session == null) || (!session.isOpen())) {
/*  52 */       if (sessionFactory == null) {
/*  53 */         rebuildSessionFactory();
/*     */       }
/*  55 */       session = sessionFactory != null ? sessionFactory.openSession() : 
/*  56 */         null;
/*  57 */       threadLocal.set(session);
/*     */     }
/*     */ 
/*  60 */     return session;
/*     */   }
/*     */ 
/*     */   public static void rebuildSessionFactory()
/*     */   {
/*     */     try
/*     */     {
/*  69 */       configuration.configure(configFile);
/*  70 */       sessionFactory = configuration.buildSessionFactory();
/*     */     } catch (Exception e) {
/*  72 */       System.err
/*  73 */         .println("%%%% Error Creating SessionFactory %%%%");
/*  74 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void closeSession()
/*     */     throws HibernateException
/*     */   {
/*  84 */     Session session = (Session)threadLocal.get();
/*  85 */     threadLocal.set(null);
/*     */ 
/*  87 */     if (session != null)
/*  88 */       session.close();
/*     */   }
/*     */ 
/*     */   public static SessionFactory getSessionFactory()
/*     */   {
/*  97 */     return sessionFactory;
/*     */   }
/*     */ 
/*     */   public static void setConfigFile(String configFile)
/*     */   {
/* 106 */     configFile = configFile;
/* 107 */     sessionFactory = null;
/*     */   }
/*     */ 
/*     */   public static Configuration getConfiguration()
/*     */   {
/* 115 */     return configuration;
/*     */   }
/*     */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.ifidc.persistent.HibernateSessionFactory
 * JD-Core Version:    0.6.0
 */