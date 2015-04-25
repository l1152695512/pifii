package com.yf.util.dbhelper;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.yf.tradecontrol.GlobalVar;
import com.yf.tradecontrol.JDomHandler;



public class LoadPoolConfig {
	public static String workPath = GlobalVar.WORKPATH;// 工作路径
	private JDomHandler domUseDocument = new JDomHandler();
	private static final LoadPoolConfig _INSTANCE = new LoadPoolConfig();
	private  Vector<DBConfigBean> poolNameVector = new Vector<DBConfigBean>();
	
	
	private DBConfigBean dbConfigBean;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	/** socket配置文件 */
	private static final String DB_POOL_CONFIG = "DBConfig.xml";
	
	private String poolName;
	private String type;
	private String driver;
	private String url;
	private String username;
	private String password;
	private int maxconn;
	/**
	 * 构造函数
	 */
	private LoadPoolConfig() {
		init();
	}
	public static LoadPoolConfig getInstanse(){
		return _INSTANCE;
	}
	/**
	 * 
	 * 解析xml文件，获取服务器ip地址、端口号、连接池最小、最大连接数
	 *
	 */ 
	public void init() { 
		
		String xmlFilePath = GlobalVar.WORKPATH + File.separator + "config" + File.separator+DB_POOL_CONFIG;
		Document document = null;
		try {
			document = parse(xmlFilePath);
		} catch (Exception e) {
			logger.error("获取SOCKET连接池配置文件DBConfig.xml失败!", e);
		}
		domUseDocument.setDoc(document);
		List poolConfigList = null;
		try {
			poolConfigList = domUseDocument.getNodeValues("db-config/pool");
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		for(int i = 0 ; i <poolConfigList.size();i++ ){
			Element element = (Element)poolConfigList.get(i);
			poolName =element.getChildText("name");
			type = element.getChildText("type");
			driver =element.getChildText("driver").trim();
			url = element.getChildText("url").trim();
			username = element.getChildText("username").trim();
			password =element.getChildText("password").trim();
			maxconn =Integer.parseInt(element.getChildText("maxconn").trim());
			dbConfigBean=new DBConfigBean();
			dbConfigBean.setName(poolName);
			dbConfigBean.setType(type);
			dbConfigBean.setDriver(driver);
			dbConfigBean.setUrl(url);
			dbConfigBean.setUsername(username);
			dbConfigBean.setPassword(password);
			dbConfigBean.setMaxconn(maxconn);
			poolNameVector.add(dbConfigBean);
			logger.debug("连接池名称："+poolName);
			
			logger.debug("连接池类型："+type);
			logger.debug("连接池驱动："+driver);
			logger.debug("连接池地址："+url);
			logger.debug("用户名："+username);
			logger.debug("密码："+password);
			logger.debug("最大连接数："+maxconn);
			//System.out.print("==="+password);
		}
	}
private Document parse(String xmlFile) throws Exception {
		Document doc = domUseDocument.loadXmlByPath(xmlFile);
		
		JDomHandler domhandler=new JDomHandler();
		domhandler.setDoc(doc);
		return doc;
	}



public Vector<DBConfigBean> getPoolNameVector() {
	return poolNameVector;
	
}
public static void main(String [] args){
	LoadPoolConfig config = LoadPoolConfig.getInstanse();
}
}

