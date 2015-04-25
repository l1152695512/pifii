package com.yf.tradecontrol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class TransControlmp implements Msfactory{
	/** 日志 */
	private static Logger logger = Logger.getLogger(TransControlmp.class);
    /**
     * 返回解析为JSONObject的解析
     */
	public JSONObject transControlJson(JSONObject json) {
		logger.info("请求json数据：" + json.toString());
		String msg = "";
		JSONObject result = new JSONObject();
		String tradeCode = json.getString("tradeCode");
		if (tradeCode == null || "".equals(tradeCode.trim())) {
			msg = "业务操作|tradeCode为空";
			logger.error(msg);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		}
		Map<String, TradeBean> tradeBeanMap = GlobalVar.getTradeBeanMap();
		TradeBean tradeBean = (TradeBean) tradeBeanMap.get(tradeCode);
		if (tradeBean == null) {
			msg = "|交易服务平台|未找到相应的接口配置，请联系科技人员!";
			logger.error(msg);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		}
		try {
			ReflctBean reflcBean = tradeBean.getAcsMethod();
			String className = reflcBean.getClassName();
			String methodName = reflcBean.getMethodName();
			logger.info("|交易时间日志|当前执行的类为：" + className);
			logger.info("|交易时间日志|当前执行的方法为：" + methodName);
			Object oArgs[] = { json };
			if (reflcBean == null || className == null || methodName == null || oArgs == null) {
				logger.debug("|服务平台|交易码、类名或方法名为空|");
			} else {
				Class classInstance = Class.forName(className);
				Object newInstance = classInstance.newInstance();
				Class oMethodArgType[] = new Class[oArgs.length];
				for (int i = 0; i < oArgs.length; i++) {
					oMethodArgType[i] = oArgs[i].getClass();
				}
				Method method = classInstance.getMethod(methodName,oMethodArgType);
				result = (JSONObject) method.invoke(newInstance, oArgs);
				logger.info("返回json数据：" + result.toString());
			}
		} catch (ClassNotFoundException e) {
			msg = "|服务平台|没有找到具有指定名称的类的定义|";
			logger.error(msg, e);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		} catch (InstantiationException e) {
			msg = "|服务平台|创建实例的对象是一接口或抽象类|";
			logger.error(msg, e);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		} catch (IllegalAccessException e) {
			msg = "|服务平台|无法访问指定类、字段、方法|";
			logger.error( msg, e);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		} catch (SecurityException e) {
			msg = "|服务平台|安全管理器抛出的异常|";
			logger.error(msg, e);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		} catch (NoSuchMethodException e) {
			msg = "|服务平台|无法找到某一特定方法|";
			logger.error(msg, e);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		} catch (IllegalArgumentException e) {
			msg = "|服务平台|向调用方法中传递了不合法或不正确的参数|";
			logger.error(msg, e);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		} catch (InvocationTargetException e) {
			msg = "|服务平台|调用方法或构造方法抛出异常|";
			logger.error(msg, e);
			result.put("returnCode", -1);
			result.put("data", new JSONObject());
			result.put("desc", msg);
			return result;
		}

		return result;
	}

}

