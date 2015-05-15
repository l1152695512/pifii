package com.yinfu.business.util;

import java.util.Iterator;
import java.util.List;

public class ChartUtil {
	public static String generCategories(List<String> labels){
		StringBuffer categories = new StringBuffer("<categories>");
		Iterator<String> ite = labels.iterator();
		while(ite.hasNext()){
			String label = ite.next();
			categories.append("<category label='" + label + "' />");
		}
		categories.append("</categories>");
		return categories.toString();
	}
	
	
}
