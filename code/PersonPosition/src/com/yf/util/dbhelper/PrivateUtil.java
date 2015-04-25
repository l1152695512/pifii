package com.yf.util.dbhelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.RowSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import com.yf.tradecontrol.GlobalVar;

@SuppressWarnings("unchecked")
public class PrivateUtil {
	private final static Logger logger = Logger.getLogger(PrivateUtil.class);

	/**
	 * 判断对象是否为空.如果为空则返回空字串
	 * 
	 * @param obj
	 * @return
	 */
	public static String isNullRetNS(Object obj) {
		String ret = "";
		if (obj != null) {
			ret = obj.toString();
		}
		return ret;
	}
	
	/**
	 * 返回java.sql.Date日期
	 * @param obj
	 * @return
	 * @throws ParseException 
	 */
	public static java.sql.Date str2sqlDate(String dateStr) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		if (!StringUtils.isBlank(dateStr)) {
			return new java.sql.Date(format.parse(dateStr).getTime());
		}else{
			return new java.sql.Date(format.parse(format.format(new java.util.Date())).getTime());
		}
	}
	
	/**
	 * 返回java.sql.Timestamp日期
	 * @param obj
	 * @return
	 */
	public static java.sql.Timestamp str2sqlTimestamp(String dateStr) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if (!StringUtils.isBlank(dateStr)) {
			return new java.sql.Timestamp(format.parse(dateStr).getTime());
		}else{
			return new java.sql.Timestamp(format.parse(format.format(new java.util.Date())).getTime());
		}
	}

	/**
	 * 格式化数，根据指定长度及指定的小数位格式 例，传入(1111.11,15,2) 测返回111111
	 * 
	 * @return
	 */
	public static String scaleByPowerOfTen(Double dvalue, int length,
			int divisor) {
		MathContext match = new MathContext(length / 2);
		BigDecimal big = new BigDecimal(dvalue, match);
		big = big.movePointRight(divisor);
		return String.valueOf(big.longValue());
	}

	/**
	 * 更新表语句 会删除为空的对象
	 * 
	 * @param tablename
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String ctutdSl(String tablename, Map map) throws Exception {
		Set<String> keyset = map.keySet();
		final String reg = ",";
		StringBuilder builder = new StringBuilder(" update " + tablename
				+ " set ");
		int i = 0;
		List<String> keymap = new ArrayList<String>();
		for (Iterator iter = keyset.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (map.get(element) == null) {
				keymap.add(element);
				continue;
			}
			builder.append(element + "=? " + reg);
			i++;
		}
		for (Iterator iter = keymap.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			map.remove(element);
		}
		String name = builder.toString();
		if (name.endsWith(reg)) {
			name = name.substring(0, name.length() - 1);
		}
		return name;
	}

	public static void mapToString(StringBuffer buffer, Map map) {
		if (map == null || buffer == null) {
			return;
		}

		Object[] objs = map.keySet().toArray();
		buffer.append("{");
		for (int i = 0; i < objs.length; i++) {
			String element = (String) objs[i];
			Object value = map.get(element);
			if (value == null) {
				continue;
			}
			if (Map.class.isAssignableFrom(value.getClass())) {
				buffer.append("'" + element + "':");
				mapToString(buffer, (Map) value);
			} else {
				buffer.append("'" + element + "':'" + value + "'");
			}
			if (i < objs.length - 1) {
				buffer.append(",");
			}
		}
		buffer.append("}");
	}

	public static String trim(String value) {
		int size = value.length();
		int i = 0, j = size;
		while (i < size && Character.isWhitespace(value.charAt(i))) {
			i++;
		}
		while (j > 0 && Character.isWhitespace(value.charAt(j - 1))) {
			j--;
		}
		if (i > j) {
			i = j = 0;
		}
		return value.substring(i, j);
	}

	public static String formatDate(Date date, String regex) {
		String ret = null;
		if (date == null) {
			return ret;
		}
		SimpleDateFormat format = new SimpleDateFormat(regex);
		ret = format.format(date);
		return ret;
	}

	public static Double formatNumber(BigDecimal big) {
		if (big == null) {
			return null;
		}
		return big.doubleValue();
	}

	/**
	 * 格式Double类型为字符串并扩大100倍返回并且只返回正数值 1111.11 返回 111111
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDouble(Double d) {
		String reg = "0";
		if (d == null) {
			return reg;
		}
		d *= 100;
		DecimalFormat format = new DecimalFormat("0.00");
		String ds = format.format(d);
		return ds.substring(0, ds.indexOf("."));
	}

	/**
	 * 生成集合sql 例如:('123','123','123','132','12','3')
	 * 
	 * @param tkids
	 * @return
	 */
	public static String createTaskIdSql(Object[] tkids, String _regex1) {
		StringBuilder builder = new StringBuilder();
		final String regex = "";
		Object tk = null;
		for (int i = 0; i < tkids.length; i++) {
			tk = tkids[i];
			if (tk != null && !tk.equals(regex)) {
				builder.append("'" + tk + "'" + _regex1);
			}
		}
		String retStr = builder.toString();
		if (retStr.endsWith(_regex1)) {
			retStr = retStr.substring(0, retStr.length() - 1);
		}
		return retStr;
	}

	private static boolean isExistByOrgan(List<Map<String, Object>> rlist,
			String organ) {
		boolean tBool = false;
		Map<String, Object> obj = null;
		for (int i = 0; i < rlist.size(); i++) {
			obj = rlist.get(i);
			String porgan = (String) obj.get("PARENT_ORGAN");
			if (porgan != null && porgan.equals(organ)) {
				tBool = true;
				break;
			}
		}
		return tBool;
	}

	/**
	 * 获取指定路径节点
	 * 
	 * @param document
	 * @param path
	 * @return
	 */
	public static Element findElementByPath(Document document, String path) {
		String[] paths = path.split("/");
		Document doc = document.getDocument();
		Element element = doc.getRootElement();
		int i = 1;
		for (; i < paths.length; i++) {
			if (element == null) {
				break;
			}
			element = element.getChild(paths[i]);
		}
		if (i != paths.length) {
			element = null;
		}
		return element;
	}

	/**
	 * 获取节点值
	 * 
	 * @param element
	 * @return
	 */
	public static String getElementValue(Element element) {
		String value = "";
		if (element == null) {
			return value;
		}

		String v = element.getText();
		if (v != null) {
			value = v;
		}
		return value;
	}

	/**
	 * 获取指定节点下所有子节点并包装到map对象中 如存在重复节点,自动转换为List对象进行包装
	 * 
	 * @param map
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public static void queryElement(Map map, Element element) {
		if (element == null) {
			return;
		}
		List elementlist = element.getChildren();
		Element child = null;
		for (int i = 0; i < elementlist.size(); i++) {
			child = (Element) elementlist.get(i);
			String childnodename = child.getName();
			List tlist = child.getChildren();
			if (tlist.isEmpty()) {
				map.put(childnodename, child.getText());
				continue;
			}
			Map tmap = new HashMap();
			queryElement(tmap, child);
			if (map.containsKey(childnodename)) {
				Object obj = map.get(childnodename);
				if (Collection.class.isAssignableFrom(obj.getClass())) {
					Collection coll = (Collection) obj;
					coll.add(tmap);
				} else {
					List list = new ArrayList();
					Object objtemp = map.get(childnodename);
					list.add(objtemp);
					list.add(tmap);
					map.put(childnodename, list);
				}
			} else {
				map.put(childnodename, tmap);
			}
		}
	}

	/**
	 * 获取rowSet包装list
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> queryRowSet(RowSet rs)
			throws Exception {
		List list = new ArrayList();
		try {
			ResultSetMetaData re = rs.getMetaData();
			Map map = null;
			while (rs.next()) {
				map = new HashMap();
				int columnCount = re.getColumnCount();
				for (int i = 0; i < columnCount; i++) {
					String columnname = re.getColumnName(i + 1);
					map.put(columnname, rs.getObject(columnname));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			throw e;
		}
		return list;
	}

	/**
	 * 通过map创建insert语句
	 * 
	 * @param tablename
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String ctIstSl(String tablename, Map map) throws Exception {
		Set<String> keyset = map.keySet();
		final String reg = ",";
		StringBuilder builder = new StringBuilder(" insert into ");
		builder.append(" " + tablename + " (");
		int i = 0;
		for (Iterator iter = keyset.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (map.get(element) == null) {
				continue;
			}
			builder.append(element + reg);
			i++;
		}
		String name = builder.toString();
		if (name.endsWith(reg)) {
			name = name.substring(0, name.length() - 1);
		}
		name += ")values(";
		for (; i > 0; i--) {
			name += "?";
			if (i != 1) {
				name += reg;
			}
		}
		name += ")";
		return name;
	}

	@SuppressWarnings("unchecked")
	public static Object[] delNullArray(Object[] obj) {
		List list = new ArrayList();
		Object obje = null;
		for (int i = 0; i < obj.length; i++) {
			obje = obj[i];
			if (obje != null) {
				list.add(obje);
			}
		}
		return list.toArray();
	}

	/**
	 * 更新私有部分节点
	 * 
	 * @param document
	 * @param map
	 */
	public static void updatePrivateElement(Document document, Map map) {
		String[] path = { "ROOT", "BODY", "PRIVATE" };
		Document doc = document.getDocument();
		Element element = doc.getRootElement();
		for (int i = 1; i < path.length; i++) {
			element = element.getChild(path[i]);
		}
		element.removeContent();
		createElementByMap(element, map);
	}

	/**
	 * 更新指定路径的节点内的值
	 * 
	 * @param document
	 * @param map
	 */
	public static void updateElementByPathsOrMap(Document document,
			String paths, Map map) {
		String[] path = paths.split("\\");
		Document doc = document.getDocument();
		Element element = doc.getRootElement();
		for (int i = 1; i < path.length; i++) {
			element = element.getChild(path[i]);
		}
		element.removeContent();
		createElementByMap(element, map);
	}

	public static void updateHeadElement(Document document, Map map) {
		String[] path = { "ROOT", "HEAD" };
		Document doc = document.getDocument();
		Element element = doc.getRootElement();
		for (int i = 1; i < path.length; i++) {
			element = element.getChild(path[i]);
		}
		element.removeContent();
		createElementByMap(element, map);
	}

	public static void updateRootElement(Document document, Map map) {
		String[] path = { "ROOT" };
		Document doc = document.getDocument();
		Element element = doc.getRootElement();
		for (int i = 1; i < path.length; i++) {
			element = element.getChild(path[i]);
		}
		element.removeContent();
		createElementByMap(element, map);
	}

	private static void createElementByMap(Element element, Map map) {
		Set set = map.keySet();
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Object obj = map.get(key);
			if (key == null || obj == null) {
				continue;
			}
			judgeType(element, key, obj);
		}
	}

	private static void createElementByList(Element element, String key,
			Collection list) {
		Collection coll = (Collection) list;
		for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
			Object obj = (Object) iterator.next();
			judgeType(element, key, obj);
		}
	}

	private static void judgeType(Element element, String key, Object obj) {
		if (Collection.class.isAssignableFrom(obj.getClass())) {
			createElementByList(element, key, (List) obj);
		} else if (Map.class.isAssignableFrom(obj.getClass())) {
			Element elenode = new Element(key);
			element.addContent(elenode);
			createElementByMap(elenode, (Map) obj);
		} else {
			Element elenode = new Element(key);
			element.addContent(elenode);
			elenode.setText(obj.toString());
		}
	}

	/**
	 * 格式数据类型
	 * 
	 * @param d
	 * @return
	 */
	public static String formatNumber(Double d) {
		String ret = "0.00";
		if (d == null || 0 == d) {
			return ret;
		}

		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(d);
	}

	/**
	 * 与createTaskIdSql 区别只在于，这个方法没有添加''引号
	 * 
	 * @param flowids
	 * @param regex
	 * @return
	 */
	public static String createFlowidString(Object[] flowids, String regex) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < flowids.length; i++) {
			buffer.append(flowids[i]);
			if (i < flowids.length - 1) {
				buffer.append(regex);
			}
		}
		return buffer.toString();
	}

	// 半角转全角
	public static String changeCode(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		String str = "";
		for (int j = 0; j < c.length; j++) {
			str += c[j];
		}
		return str;
	}

	public static String removeISOControlChar(String strline) {
		StringBuffer buffer = new StringBuffer();
		if (strline == null) {
			return strline;
		}
		for (int i = 0; i < strline.length(); i++) {
			char c = strline.charAt(i);
			if (!Character.isISOControl(c)) {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	// 全角转半角
	public static String changeCode2(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] > 65248) {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String str = "";
		for (int j = 0; j < c.length; j++) {
			str += c[j];
		}
		return str;
	}
	
	/**
	 * 得到一个13位客户ID
	 * 第1 —— 6位       区域号
     * 第7位      客户类型：1企业、2家庭
     * 第8 —— 13位          客户序号
     * @param areaNo  区域编号
	 * @param customerType  客户类型：1企业、2家庭
	 * @return
	 * @throws Exception
	 */
	public static String getCustomerNo(String areaNo, String customerType) throws Exception {
		int sequence = 0;
		if (areaNo == null || areaNo.equals("")){
			throw new IllegalArgumentException("无效参数[区域编号为空]!");
		}
		if (areaNo.length()!=6){
			throw new IllegalArgumentException("区域号[" +areaNo + "]错误，应该为 6位!");
		}
		if (customerType == null || customerType.equals("")){
			throw new IllegalArgumentException("无效参数[客户类型为空]!");
		}
		if (customerType.length()!=1){
			throw new IllegalArgumentException("客户类型[" +customerType + "]错误，应该为 1位!");
		}
		
		try {
			sequence = getSequenceByAreaNo(areaNo);
		} catch (Exception e) {
			createSequence(areaNo);
			sequence = getSequenceByAreaNo(areaNo);
			throw e;
		}
		DecimalFormat df = new DecimalFormat("000000");
		return new StringBuffer()
		   .append(areaNo)
	       .append(customerType)
	       .append(df.format(sequence))
        .toString();
	}
	
	private static int getSequenceByAreaNo(String areaNo) throws SQLException, Exception {
		String sql = "select area_"+areaNo+"_seq.nextval id from dual";
		DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
		ResultSet rs = dbhelper.select(sql);
		if (rs.next()) {
			return rs.getInt(1);
		} else {
			createSequence(areaNo);
			return getSequenceByAreaNo(areaNo);
		}
	}
	
	/**
	 * 创建sequence
	 * @param organNo
	 * @throws Exception
	 */
	private static void createSequence(String areaNo) throws Exception {
		StringBuffer sql = new StringBuffer("CREATE SEQUENCE ");
		sql.append("area_"+areaNo+"_seq ");
		sql.append("INCREMENT BY 1 ");
		sql.append("START WITH 1 ");
		sql.append("MAXVALUE 999999 ");
		sql.append("NOCYCLE ");
		sql.append("NOCACHE ");
		sql.append("ORDER ");
		DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
		dbhelper.executeFor(sql.toString());	
	}
	
	/**
	 * 通过map创建update语句
	 * 
	 * @param tablename
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String ctUpdateSl(String tablename, Map map,String where) throws Exception {
		Set<String> keyset = map.keySet();
		final String reg = " =? ";
		final String reg1 = ",";
		StringBuilder builder = new StringBuilder("update");
		builder.append(" " + tablename + " set ");
		int i = 0;
		for (Iterator iter = keyset.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (map.get(element) == null) {
				continue;
			}
			if (i != 0 ) {
				builder.append(reg1);
			}
			builder.append(element + reg);
			i++;
		}
		builder.append(where);
		String name = builder.toString();	
		return name;
	}

	public static void main(String[] args) throws Exception {
		DecimalFormat df = new DecimalFormat("000000");
		System.out.println(df.format(524));
		
		System.out.println(str2sqlDate("2012-12-12 11:11:11"));
	}

}
