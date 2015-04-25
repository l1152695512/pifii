package com.yf.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件读取
 * 
 */
public class CfgTools {
    private static Logger logger = LoggerFactory.getLogger(CfgTools.class);
    /* DesignPattern: singleton */
    private static CfgTools ct = null;
    // 配置文件路径
    private String m_CfgFilePath = null;

    /**
     * 应用根目录
     */
    private String rootPath = null;

    //
    private boolean isWindows = false;

    private boolean isLinux = false;

    private HashMap m_PropertiesMap = new HashMap();

    private long m_LastModifiedTime = 0;

    private int m_WatchTime = 60000;

    private boolean m_IsWatched = true;

    private String classPath = null;

    private String cfgFileName = "ccflowip.properties";

    /**
     * 构造函数，初始化根目录，并读配置文件
     * 
     */
    private CfgTools() {

        classPath = CfgTools.class.getClassLoader().getResource("").toString();// file:/D:/publish/Elvis_GZ_V4/WEB-INF/classes/
        
        if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
            isWindows = true;
            isLinux = false;
            classPath = classPath.replace("file:/", "");// file:/D:/publish/Elvis_GZ_V4/WEB-INF/classes/
        } else {
            isWindows = false;
            isLinux = true;
            classPath = classPath.replace("file:", "");// file:/opt/web/Elvis_GZ_V4/WEB-INF/classes/
        }
        
        rootPath = classPath;
        rootPath = rootPath.replace("%20", " ");
        
        m_CfgFilePath = rootPath + cfgFileName;
        logger.info("classPath:" + classPath);
        logger.info("rootPath:" + rootPath);
    }

    /**
     * DesignPattern: singleton
     * 
     * @return 唯一的cfg实例
     */
    public static synchronized CfgTools getInstance() {
        if (ct == null) {
            try {
                ct = new CfgTools();
                ct.readConfig();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return ct;
    }

    /**
     * 获取应用根目录（网站根目录,如d:\Elvis_GZ）
     * 
     * @return
     */
    public String getRoot() {
        return ct.rootPath;
    }

    /**
     * 读取配置文件的路径
     * 
     * @return 配置文件的路径
     */
    public String getCfgFilePath() {
        return ct.m_CfgFilePath;
    }

    /**
     * 读取监控的间隔时间
     * 
     * @return 间隔时间
     */
    public int getWatchTime() {
        return ct.m_WatchTime;
    }

    /**
     * 设置监控的间隔时间
     * 
     * @param watchTime
     *            间隔时间，单位为毫秒,小于等于零时表示不监控
     */
    public void setWatchTime(int watchTime) {
        if (watchTime <= 0) {
            ct.m_IsWatched = false;
        } else {
            ct.m_IsWatched = true;
            ct.m_WatchTime = watchTime;
        }
    }

    /**
     * 读取Properties配置文件
     * 
     * @param propPath
     *            properties配置文件绝对路径
     * @return Map
     */
    public static Map loadProperties(String propPath) {
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream(propPath);
            properties.load(file);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        Map propMap = new HashMap();
        propMap.putAll(properties);
        return propMap;
    }

    /**
     * 从配置文件中读取必要的信息
     * 
     * @throws IOException
     */
    public void readConfig() throws IOException {
        try {
            logger.info("Reading cfg. config file:" + ct.m_CfgFilePath);
            FileInputStream file = new FileInputStream(ct.m_CfgFilePath);
            Properties properties = new Properties();
            
            properties.load(file);

            if (ct.m_PropertiesMap != null) {
                ct.m_PropertiesMap.clear();
            }
            ct.m_PropertiesMap.putAll(properties);
            file.close();
            logger.info("cfg loaded.");
        } catch (Exception e) {
            logger.error("读取配置文件失败." + e.getMessage(), e);
        }
    }

    /**
     * 根据关键字返回目录绝对路径
     * 
     * @param key
     *            关键字
     * @return String 属性值
     */
    public String getRealPath(String key) {
        if (key == null)
            throw new IllegalArgumentException("Key can't be null!");
        return ct.getRoot() + ct.getString(key);
    }

    /**
     * 取WEB-INF绝对路径
     * 
     * @return WEB-INF绝对路径目录(eg:X:/XXX//WEB-INF/)
     */
    public String getWebInf() {
        if (ct.classPath.indexOf("WEB-INF") != -1) {
            return ct.getRoot() + "/WEB-INF/";
        } else {
            logger.warn("not WEB-INF path in current environment. return the root path instead.");
            return ct.getRoot();
        }
    }

    /**
     * 取类的运行根目录的绝对路径
     * 
     * @return classes绝对路径目录(eg:在web环境中返回X:/XXX//WEB-INF/classes/)
     */
    public String getClassPath() {
        return ct.classPath;
    }

    /**
     * 根据关键字查询属性值
     * 
     * @param key
     *            关键字
     * @return String属性值
     */
    public String getString(String key) {
        if (key == null)
            throw new IllegalArgumentException("Key can't be null!");

        return (String) ct.m_PropertiesMap.get(key);
    }

    /**
     * 根据关键字查询属性值
     * 
     * @param key
     *            关键字
     * @return int类型的属性值
     */
    public int getInt(String key) {
        return Integer.parseInt(ct.getString(key));
    }

    /**
     * 根据关键字查询属性值
     * 
     * @param key
     *            关键字
     * @return long类型的属性值
     */
    public long getLong(String key) {
        return Long.parseLong(ct.getString(key));
    }

    /**
     * 根据关键字查询属性值
     * 
     * @param key
     *            关键字
     * @return boolean类型的属性值
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(ct.getString(key));
    }

    /**
     * 根据关键字查询属性值
     * 
     * @param key
     *            关键字
     * @return double类型的属性值
     */
    public double getDouble(String key) {
        return Double.parseDouble(ct.getString(key));
    }

    /**
     * 根据key和分隔符取属性值的字符串数组
     * 
     * @param key
     * @param splitChar
     * @return String[]
     */
    public String[] getArray(String key, String splitChar) {
        String value = ct.getString(key);
        String[] valuses = null;
        if (value != null && value.indexOf(splitChar) != -1) {
            valuses = value.split(splitChar);
        }
        return valuses;
    }

    public boolean isLinux() {
        return isLinux;
    }

    public boolean isWindows() {
        return isWindows;
    }

    public boolean isIsWatched() {
        return m_IsWatched;
    }

    public void setIsWatched(boolean isWatched) {
        m_IsWatched = isWatched;
    }
}
