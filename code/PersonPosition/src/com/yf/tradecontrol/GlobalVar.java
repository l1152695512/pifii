package com.yf.tradecontrol;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.jdom.Document;
import org.jdom.Element;

import com.yf.util.dbhelper.DBResourceBean;

public class GlobalVar {

	public static String ACSLOG = "";
	// 连接池名称
	public static final String POOLNAME_YFBIZDB = "yfbizdb";
	// 数据库配置类
	private static DBResourceBean dbResourceBean;
	// 文件默认根路径
	public static String WORKPATH = GlobalVar.WORKPATH;//WEB-INF路径下
	// 工具默认根路径
	public static String TOOLSPATH = GlobalVar.TOOLSPATH;//WebRoot路径下
	// 业务类
	private static Hashtable<String, TradeBean> tradeBeanMap = new Hashtable<String, TradeBean>();

	public static void setWorkPath(String root) {
		WORKPATH = root;
	}

	public static void setToolsPath(String root) {
		TOOLSPATH = root;
	}
	/**
	 * 初始化全局变量
	 * @return 初始化成功/失败
	 */
	public static boolean initGlobalVar() {
		if (!createLogFile()) {
			return false;
		}
		if (!initDBConfig()) {
			return false;
		}
		if (!initTradeConfig()) {
			return false;
		}

		return true;
	}

	public static Hashtable<String, TradeBean> getTradeBeanMap() {
		return tradeBeanMap;
	}

	/**
	 * 建立log文件夹
	 * @return 建立成功/失败
	 */
	private static boolean createLogFile() {
		try {
			File logDir = new File(WORKPATH + File.separator + "log");
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			String log4jfile = WORKPATH + File.separator + "config"
					+ File.separator + "log4j.properties";
			PropertyConfigurator.configure(log4jfile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 初始化业务配置：读取配置文件 tradeConfig.xml
	 * 
	 * @return
	 */
	private static boolean initTradeConfig() {
		try {
			JDomHandler domUseDocument = new JDomHandler();
			Document doc = domUseDocument.loadXmlByPath(WORKPATH
					+ File.separator + "config" + File.separator
					+ "tradeConfig.xml");
			domUseDocument.setDoc(doc);
			List tradeList = domUseDocument.getNodeValues("trades/trade");
			for (int i = 0; i < tradeList.size(); i++) {
				Element tradeDoc = (Element) tradeList.get(i);
				String tradeCode = tradeDoc.getChildText("tradeCode");
				String description = tradeDoc.getChildText("description");
				String tradeClassName = tradeDoc.getChildText("className");
				String tradeMethodName = tradeDoc.getChildText("methodName");
				ReflctBean acsMethodBean = new ReflctBean();
				acsMethodBean.setClassName(tradeClassName);
				acsMethodBean.setMethodName(tradeMethodName);
				TradeBean tradeBean = new TradeBean();
				tradeBean.setTradeCode(tradeCode);
				tradeBean.setDescription(description);
				tradeBean.setAcsMethod(acsMethodBean);
				tradeBeanMap.put(tradeCode, tradeBean);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * 设置数据库配置：读取配置文件 DBConfig.xml
	 * 
	 * @return 设置成功/失败
	 */
	private static boolean initDBConfig() {
		try {
			JDomHandler domUseDocument = new JDomHandler();
			StringBuffer path = new StringBuffer(GlobalVar.WORKPATH);
			path.append(File.separator).append("config").append(File.separator)
					.append("DBConfig.xml");
			Document doc = domUseDocument.loadXmlByPath(path.toString());
			domUseDocument.setDoc(doc);
			List dbList = domUseDocument.getNodeValues("db-config/pool");
			for (int i = 0; i < dbList.size(); i++) {
				Element dbDoc = (Element) dbList.get(i);
				String name = dbDoc.getChildText("name");
				// 默认只获取yfbizdb连接池到内存当中
				if (name.equals(GlobalVar.POOLNAME_YFBIZDB)) {
					String type = dbDoc.getChildText("type");
					String driver = dbDoc.getChildText("driver");
					String url = dbDoc.getChildText("url");
					String username = dbDoc.getChildText("username");
					String password = dbDoc.getChildText("password");
					String maxconn = dbDoc.getChildText("maxconn");
					DBResourceBean dbresourceBean = new DBResourceBean();
					dbresourceBean.setName(name);
					dbresourceBean.setType(type);
					dbresourceBean.setDriver(driver);
					dbresourceBean.setUrl(url);
					dbresourceBean.setUsername(username);
					dbresourceBean.setPassword(password);
					dbresourceBean.setMaxconn(maxconn);
					GlobalVar.dbResourceBean = dbresourceBean;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
