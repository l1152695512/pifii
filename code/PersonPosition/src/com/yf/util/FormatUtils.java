package com.yf.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yf.remote.service.DataBaseDTO;

public class FormatUtils {

	public static final DecimalFormat DECIMAL_TWO = new DecimalFormat("#0.00");
	public static final DecimalFormat DECIMAL_THREE = new DecimalFormat(
			"#0.000");

	/**
	 * 把长整型数字补齐指定字符串,形成定长的字符串
	 * 
	 * @param source
	 *            源长整型数字
	 * @param iLength
	 *            目标字符串的长度
	 * @param ch
	 *            填充字符串
	 * @return 异常返回 null或 " " ,成功返回定长的字符串
	 */
	public static String toDefiniteLengthString(long source, int iLength,
			char ch) {
		StringBuffer sb = new StringBuffer();
		String strValue = String.valueOf(source);
		if (iLength < strValue.length()) {
			return null;
		}
		for (int i = 0; i < iLength - strValue.length(); i++) {
			sb.append(ch);
		}
		sb.append(source);
		return new String(sb);
	}

	/**
	 * 把double变成2位小数
	 * 
	 * @param total
	 * @return
	 */
	public static Double formatTwoDouble(Double total) {

		return Double.parseDouble(formatDouble(total, 2));
	}

	public static Double formatThreeDouble(Double total) {

		return Double.parseDouble(formatDouble(total, 3));
	}

	public static String formatDouble(double value, int num) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bd2 = bd.setScale(num, BigDecimal.ROUND_HALF_UP);
		return bd2.toString();
	}

	public static void main1(String[] args) {
		// Debug.println(formatTwoDouble(2000d));
		// Debug.println(formatTwoDouble(2000.123d));
		// Debug.println(formatTwoDouble(2000.125d));
		// Debug.println(formatTwoDouble(2000.124999d));

		double value = 134.1556235d;

		Debug.println(formatTwoDouble(value));
		Debug.println(formatThreeDouble(value));

		// 1. 先乘后四舍五入, 再除;
		double d = 123.14499;

		double d2 = Math.round(d * 100) / 100.0;
		Debug.println("通过Math取整后做除法: " + d2);

		// 2. 通过BigDecimal的setScale()实现四舍五入与小数点位数确定, 将转换为一个BigDecimal对象.
		BigDecimal bd = new BigDecimal(d);
		BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		Debug.println("通过BigDecimal.setScale获得: " + bd2);

		// 3. 通过DecimalFormat.format返回String的
		DecimalFormat df = new DecimalFormat("#.##");
		Debug.println("通过DecimalFormat.format获得: " + df.format(d));

		// 4. 通过String.format
		Debug.println("通过StringFormat: " + String.format("%.2f", d));
		
		
		double formatString=0;
		Debug.println("通过DecimalFormat: " + DECIMAL_TWO.format(formatString));
	}
	
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean) throws Exception {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
    
    
    public static void main(String[] src) throws Exception{
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ms");
    	DataBaseDTO dataBaseDTO = new DataBaseDTO();
    	dataBaseDTO.setDb_id(new HibernateUUId().generate().toString());
    	dataBaseDTO.setCreate_time(new java.sql.Timestamp(new Date().getTime()));
		dataBaseDTO.setArea("1111");
		Map dataBaseMap = FormatUtils.convertBean(dataBaseDTO);
		final String inserSql = DBUtil.ctIstSl("BP_DATA_BASE_TBL", dataBaseMap);
		System.out.println(DBUtil.delNullArray(dataBaseMap.values().toArray()).toString());
    }
    
}
