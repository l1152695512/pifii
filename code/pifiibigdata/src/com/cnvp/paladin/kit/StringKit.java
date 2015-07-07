package com.cnvp.paladin.kit;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.StrKit;

public class StringKit extends StrKit{
	
	public static String urlDecode(String value){
		String result = null;
		try {
			result = URLDecoder.decode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	/** 
	 * 表名转模型名
	 */
	public static String tableName_2_modelName(String tableName) {
		String[] tableNameArr = tableName.replace(ConfigKit.get("jdbc.dbPrefix"), "").split("_");
		StringBuilder modelName = new StringBuilder();
		for (int j = 0; j < tableNameArr.length; j++) 
			modelName.append(firstCharToUpperCase(tableNameArr[j]));
		return modelName.toString();
	}
	/** 
	 * 模型名转表名
	 */
	public static String modelName_2_tableName(String modelName) {
		String tableName = modelName.replaceAll("[A-Z]", "_$0");
		tableName = tableName.substring(1,tableName.length()).toLowerCase();
		return ConfigKit.get("jdbc.dbPrefix") + tableName;
	}
	
	/**
	 * 从数据库字段类型，提取类型和长度
	 * 例如 varchar(20)
	 * @param typeFromDb
	 * @return varchar 20
	 */
	public static String[] getFieldTypeLength(String typeFromDb){
		Pattern p = Pattern.compile("(\\w+)\\((.+)\\)");
		Matcher m = p.matcher(typeFromDb);
		ArrayList<String> strArr = new ArrayList<String>();
		while (m.find()) {			
			for (int i = 1; i < m.groupCount()+1; i++) {
				strArr.add(m.group(i));
			}
		}
		if (strArr.size()==0)
			return new String[]{typeFromDb,""};
		else{
			String[] result = new String[strArr.size()];
			result = (String[]) strArr.toArray(result);
			System.out.println(result);
			return result;
		}
		
	}
	public static String implode(ArrayList<String> strArr, String separator){
		String[] result = new String[strArr.size()];
		result = (String[]) strArr.toArray(result);
		return implode(result,separator);
	}
	public static String implode(String[] strArr, String separator){
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < strArr.length; i++) {
			if(i!=0)
				result.append(separator);
			result.append(strArr[i]);
		}
		return result.toString();
	}
	/** 
     * 使用StringTokenizer类将字符串按分隔符转换成字符数组 
     * @param string 字符串  
     * @param separator 分隔符 
     * @return 字符串数组 
     * @see [类、类#方法、类#成员] 
     */  
    public static String[] split(String string, String separator)  
    {  
        int i = 0;  
        StringTokenizer tokenizer = new StringTokenizer(string, separator);  
          
        String[] str = new String[tokenizer.countTokens()];  
          
        while (tokenizer.hasMoreTokens())  
        {  
            str[i] = new String();  
            str[i] = tokenizer.nextToken();  
            i++;  
        }  
        return str;  
    }  
      
    /** 
     * 字符串解析，不使用StringTokenizer类和java.lang.String的split()方法 
     * 将字符串根据分割符转换成字符串数组 
     * @param string 字符串 
     * @param c 分隔符 
     * @return 解析后的字符串数组 
     */  
    public static String[] split(String string, char separator)  
    {  
        //字符串中分隔符的个数  
        int count = 0;  
          
        //如果不含分割符则返回字符本身  
        if (string.indexOf(separator) == -1)  
        {  
            return new String[]{string};  
        }  
          
        char[] cs = string.toCharArray();  
          
        //过滤掉第一个和最后一个是分隔符的情况  
        for (int i = 1; i < cs.length -1; i++)  
        {  
            if (cs[i] == separator)  
            {  
                count++; //得到分隔符的个数  
            }  
        }  
          
        String[] strArray = new String[count + 1];  
        int k = 0, j = 0;  
        String str = string;  
          
        //去掉第一个字符是分隔符的情况  
        if ((k = str.indexOf(separator)) == 0)  
        {  
            str = string.substring(k + 1);  
        }  
          
        //检测是否包含分割符，如果不含则返回字符串  
        if (str.indexOf(separator) == -1)  
        {  
            return new String[]{str};  
        }  
          
        while ((k = str.indexOf(separator)) != -1)  
        {  
            strArray[j++] = str.substring(0, k);  
            str = str.substring(k + 1);  
            if ((k = str.indexOf(separator)) == -1 && str.length() > 0)  
            {  
                strArray[j++] = str.substring(0);  
            }  
        }  
          
        return strArray;  
    }  
}
