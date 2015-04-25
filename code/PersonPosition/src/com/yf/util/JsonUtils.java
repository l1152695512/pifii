package com.yf.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;


public class JsonUtils {
	private static Logger logger = Logger.getLogger(JsonUtils.class);

	public static class JsonHelper {
		private static final String HIBERNATE_LAZY_INITIALIZER_PROPERTY = "hibernateLazyInitializer";
		private JsonConfig jsonConfig;

		public JsonConfig getJsonConfig() {
			return jsonConfig;
		}

		Set<String> excludes = new HashSet<String>();
		private boolean excludeChildren;
		private boolean excludeParent;
		private Class excludeClass;

		public JsonHelper() {
			jsonConfig = new JsonConfig();
			jsonConfig
					.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			excludes.add(HIBERNATE_LAZY_INITIALIZER_PROPERTY);
		}

		/**
		 * 设置要把日期转化为指定格式类型
		 * 
		 * @param pattern
		 * @return
		 */
		public JsonHelper setDateFormat(String pattern) {
			return registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor(pattern));
		}

		/**
		 *设置是否把boolean值作为字符串显示,例如 true to "true",在Ext.form.Radio类是必要的
		 * 
		 * @param propertyName
		 * @return
		 */
		public JsonHelper setBooleanToString(String propertyName) {
			BooleanToStringJsonValueProcessor processor = new BooleanToStringJsonValueProcessor(
					propertyName);
			return registerJsonValueProcessor(Boolean.class, processor)
					.registerJsonValueProcessor(
							ClassUtils.resolvePrimitiveClassName("boolean"),
							processor);
		}

		/**
		 * 注册Json值处理器
		 * 
		 * @param propertyType
		 * @param processor
		 * @return
		 */
		public JsonHelper registerJsonValueProcessor(Class propertyType,
				JsonValueProcessor processor) {
			jsonConfig.registerJsonValueProcessor(propertyType, processor);
			return this;
		}

		/**
		 * 设置是否把所有boolean值作为字符串显示,例如 true to "true",在Ext.form.Radio类是必要的
		 * 
		 * @return
		 */
		public JsonHelper setAllBooleanToString() {
			return setBooleanToString(null);
		}

		/**
		 * 排除属性中的子列表（即一对多的“多”端），对于Hibernate延迟加载属性排除是有用的。
		 * 
		 * @param classToFilte
		 * @return
		 */
		public JsonHelper excludeChildren(Class classToFilte) {
			this.excludeChildren = true;
			this.excludeClass = classToFilte;
			return this;
		}

		/**
		 * 排除属性中的父列表（即多对一的“一”端），对于Hibernate延迟加载属性排除是有用的。
		 * 
		 * @param classToFilte
		 * @return
		 */
		public JsonHelper excludeParent(Class classToFilte) {
			this.excludeParent = true;
			this.excludeClass = classToFilte;
			return this;
		}

		/**
		 * 排除属性中的父列表（即多对一的“一”端）和子列表（即一对多的“多”端），对于Hibernate延迟加载属性排除是有用的。
		 * 
		 * @param classToFilte
		 * @return
		 */
		public JsonHelper excludeForeignObject(Class classToFilte) {
			this.excludeParent = true;
			this.excludeChildren = true;
			this.excludeClass = classToFilte;
			return this;
		}

		private void setExcludePerClass(Class clazz) {
			Method[] methods = clazz.getDeclaredMethods();
			// Debug.println("xxxx"+methods.length);
			for (int j = 0; j < methods.length; j++) {
				String mName = methods[j].getName();
				if (mName.length() > 4 && mName.startsWith("get")
						&& methods[j].getParameterTypes().length == 0) {
					String beanFieldName = Character.toLowerCase(mName
							.charAt(3))
							+ mName.substring(4);
					Class returnType = methods[j].getReturnType();
					// Debug.println(returnType);
					if (!returnType.getName().equals("void")) {
						if (excludeChildren
								&& Collection.class
										.isAssignableFrom(returnType)) {
							addExclude(beanFieldName);
						} else if (excludeParent
								&& !ClassUtils.isPrimitiveOrWrapper(returnType)
								&& !returnType.getName().startsWith("java") // 非java开头的包名，以后可能要改进。
								&& !returnType.getName().startsWith("[L") // 非数组
						) {
							addExclude(beanFieldName);
						}
					}
				}
			}
		}

		private void setExcludeClass(Class voClass) {
			setExcludePerClass(voClass);
			Class[] classes = voClass.getClasses();
			for (int i = 0; i < classes.length; i++) {
				setExcludePerClass(classes[i]);
			}
			// voClass.get
		}

		private void execlude(Object object) {
			if (object != null && this.excludeClass != null)
				this.setExcludeClass(this.excludeClass);
			int size = excludes.size();
			if (size > 0)
				jsonConfig.setExcludes(excludes.toArray(new String[size]));
		}

		/**
		 * 添加“属性”到“排除列表”
		 * 
		 * @param exclude
		 * @return
		 */
		public JsonHelper addExclude(String exclude) {
			excludes.add(exclude);
			return this;
		}

		/**
		 * 从“排除列表”删除 “属性”
		 * 
		 * @param exclude
		 * @return
		 */
		public JsonHelper removeExclude(String exclude) {
			excludes.remove(exclude);
			return this;
		}

		/**
		 * 添加多个“属性”到“排除列表”
		 * 
		 * @param excludes
		 * @return
		 */
		public JsonHelper addExcludes(Collection<String> excludes) {
			this.excludes.addAll(excludes);
			return this;
		}

		/**
		 * 从“排除列表”删除多个 “属性”
		 * 
		 * @param excludes
		 * @return
		 */
		public JsonHelper removeExcludes(Collection<String> excludes) {
			this.excludes.removeAll(excludes);
			return this;
		}

		/**
		 * 添加多个“属性”到“排除列表”
		 * 
		 * @param excludes
		 * @return
		 */
		public JsonHelper addExcludes(String[] excludes) {
			this.excludes.addAll(Arrays.asList(excludes));
			return this;
		}

		/**
		 * 从“排除列表”删除多个 “属性”
		 * 
		 * @param excludes
		 * @return
		 */
		public JsonHelper removeExcludes(String[] excludes) {
			this.excludes.removeAll(Arrays.asList(excludes));
			return this;
		}

		/**
		 * 转换为JSONArray类型，即数组型JSON
		 * 
		 * @param object
		 * @return
		 */
		public JSONArray toJSONArray(Object object) {
			execlude(object);
			return JSONArray.fromObject(object, jsonConfig);
		}

		/**
		 * 转换为JSONObject类型，即对象型JSON
		 * 
		 * @param object
		 * @return
		 */
		public JSONObject toJSONObject(Object object) {
			execlude(object);
			return JSONObject.fromObject(object, jsonConfig);
		}

		/**
		 * 把JSON字符串中的值copy到指定对象上。
		 * 
		 * @param jsonString
		 * @param bean
		 */
		public void toBean(String jsonString, Object bean) {
			execlude(bean);
			JSONObject json = JSONObject
					.fromObject(jsonString, this.jsonConfig);
			JSONObject.toBean(json, bean, this.jsonConfig);
		}
	}

	public static class DateJsonValueProcessor implements JsonValueProcessor {
		private DateFormat dateFormat;

		/**
		 * 构造方法.
		 * 
		 * @param datePattern
		 *            日期格式
		 */
		public DateJsonValueProcessor(String datePattern) {
			dateFormat = new SimpleDateFormat(datePattern);
		}

		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
			return process(value);
		}

		public Object processObjectValue(String key, Object value,
				JsonConfig jsonConfig) {
			return process(value);
		}

		private Object process(Object value) {
			if (value == null)
				return null;
			return dateFormat.format((Date) value);
		}
	}

	public static class BooleanToStringJsonValueProcessor implements
			JsonValueProcessor {
		private String propertyName;

		public BooleanToStringJsonValueProcessor() {
		}

		public BooleanToStringJsonValueProcessor(String propertyName) {
			this.propertyName = propertyName;
		}

		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
			if (value != null)
				return String.valueOf(value);
			return value;
		}

		public Object processObjectValue(String key, Object value,
				JsonConfig jsonConfig) {
			if (value == null)
				return null;
			if (propertyName == null)
				return String.valueOf(value);
			return propertyName.equals(key) ? String.valueOf(value) : value;
		}

	}

	public static JsonConfig getJsonConfig() {
		JsonConfig jsonConfig = new JsonConfig();
		return jsonConfig;
	}

	public static JsonHelper getJsonHelper() {
		return new JsonHelper();
	}

	/**
	 * 可以将json对象转换为对应的bean 注意：不适合含有日期字段的bean转换
	 * 
	 * @param jsonString
	 * @param bean
	 */
	public static void toBean(String jsonString, Object bean) {
		JSONObject json = JSONObject.fromObject(jsonString);
		JSONObject.toBean(json, bean, new JsonConfig());
	}

	public static void main(String[] args) {
		Debug.println(getJsonHelper().setAllBooleanToString()
				.toJSONObject(new Test()));
		Debug.println(getJsonHelper().setBooleanToString("testBoolean")
				.toJSONObject(new Test()));
		Debug.println(getJsonHelper().toJSONObject(new Test()));
		Debug.println(ClassUtils.isPrimitiveOrWrapper(ClassUtils
				.resolvePrimitiveClassName("int")));
		Debug.println(ClassUtils.isPrimitiveOrWrapper(Integer.class));
		Debug.println(ClassUtils.isPrimitiveOrWrapper(String.class));
		Debug.println(Collection.class.getName());
		Method[] methods = Collection.class.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			String mName = methods[j].getName();
			Debug.println(mName);
			Debug.println(methods[j].getReturnType().getName());
		}
		// Debug.println(getJsonHelper().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeChildren().toJSONObject(new
		// Dept()));
	}

	public static class Test {
		private Boolean testBoolean = true;

		public Boolean getTestBoolean() {
			return testBoolean;
		}

		public void setTestBoolean(Boolean testBoolean) {
			this.testBoolean = testBoolean;
		}

	}
	
	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else if (obj instanceof Date){
			json.append("\"").append(obj).append("\"");
		}else {
			json.append(bean2json(obj));
		}
		return json.toString();
	}

	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());
					String value = object2json(props[i].getReadMethod().invoke(
							bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	public static JSONObject getJSONBean(HttpServletRequest request) {
		InputStream in = null;
		ByteArrayOutputStream read = null;
		try {
			in = request.getInputStream();
			read = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				read.write(buffer, 0, len);
			}
			byte[] data = read.toByteArray();
			JSONObject json = JSONObject.fromObject(new String(data, "UTF-8").intern());
			return json;
		} catch (Exception e) {
			logger.error("数据解析出错！",e);
			return new JSONObject();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (read != null) {
					read.close();
				}
			} catch (IOException e) {
				logger.error("关闭IO流出错！",e);
				return new JSONObject();
			}
		}
	}
	
	public static Map<String, Object> parseJSON2Map(JSONObject jsonObject) {
		String jsonStr = jsonObject.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}
}
