package com.yf.base.actions.commons;

import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class CommunityUtils {
	private static DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	
	@SuppressWarnings("unchecked")
	public static String getParentCommunity(String id){
		List<Map<String,Object>> community = (List<Map<String, Object>>) dbhelper.getMapListBySql(
				"select dependent_community from bp_community_tbl where id=? ",new Object[]{id});
		if(community.size()>0){
			Map<String,Object> firstRow = community.get(0);
			Object dependent_community = firstRow.get("dependent_community");
			if(null != dependent_community && StringUtils.isNotBlank(dependent_community.toString())){
				id = dependent_community.toString();
			}
		}
		return id;
	}
	
	public static List<?> getCommunityComboWidthoutFloor(String columns){
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		StringBuffer sql = new StringBuffer();
		sql.append("select "+columns+" from bp_community_tbl where 1=1 ");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			sql.append("and user_id = '"+userId+"' ");
		}
		sql.append("and area_type!='2' order by name ");
		return dbhelper.getMapListBySql(sql.toString());
	}
	
	public static List<?> getCommunityComboWidthFloor(String columns){
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		StringBuffer sql = new StringBuffer();
		sql.append("select "+columns+" from bp_community_tbl where 1=1 ");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			sql.append("and user_id = '"+userId+"' ");
		}
		sql.append("and (dependent_community is null or dependent_community='') order by name ");
		return dbhelper.getMapListBySql(sql.toString());
	}
}
