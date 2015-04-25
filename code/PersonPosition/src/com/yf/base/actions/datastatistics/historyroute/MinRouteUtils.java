package com.yf.base.actions.datastatistics.historyroute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

import edu.emory.mathcs.backport.java.util.Arrays;

public class MinRouteUtils {
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private Map<String,JSONObject> points = new HashMap<String,JSONObject>();
	private List<List<String>> routesAll = new ArrayList<List<String>>();
	
	private String communityId;
	
	public MinRouteUtils(String communityId) {
		this.communityId = communityId;
		initPoints();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,JSONObject> getAdjacentPoints(String card_mark_start,String card_mark_end){
		Map<String,JSONObject> adjacentPoints = new LinkedHashMap<String,JSONObject>();
		if(null != points.get(card_mark_start)){//所有点中包含起始点
			List<String> thisPointLinkPoints = (List<String>)(points.get(card_mark_start)).get("linkPoints");
			if(null != thisPointLinkPoints && !thisPointLinkPoints.contains(card_mark_end)){//该两点不为相邻点,添加相邻点到历史记录里]
				List<String> routes = new ArrayList<String>();
				routes.add(card_mark_start);
				routesAll.clear();
				findRoutes(card_mark_start,card_mark_end,routes);
				int index = -1;
				double minLength = -1;
				for(int j=0;j<routesAll.size();j++){
					List<String> route = routesAll.get(j);
					double thisLength = 0;
					for(int i=1;i<route.size();i++){
						JSONObject previousPoint = points.get(route.get(i-1));
						JSONObject thisPoint = points.get(route.get(i-1));
						double pointLength = Math.pow(Math.pow(previousPoint.getDouble("locationX")-thisPoint.getDouble("locationX"), 2)+
								Math.pow(previousPoint.getDouble("locationY")-thisPoint.getDouble("locationY"), 2), 1/2);
						thisLength += pointLength;
					}
					if(minLength == -1 || minLength>thisLength){
						minLength = thisLength;
						index = j;
					}
				}
				if(index != -1){
					List<String> thisPoints = routesAll.get(index);
					for(int i=1;i<thisPoints.size()-1;i++){
						String cardMark = thisPoints.get(i);
						adjacentPoints.put(cardMark, points.get(cardMark));
					}
				}
			}
		}
		return adjacentPoints;
	}
	//获取两点之间所有符合的路线
	@SuppressWarnings("unchecked")
	private void findRoutes(String thisPointMark,String endPointMark,List<String> routes){
		List<String> linkPoints = (null == points.get(thisPointMark))?null:(List<String>)points.get(thisPointMark).get("linkPoints");
		if(null != linkPoints){
			Iterator<String> ite = linkPoints.iterator();
			while(ite.hasNext()){
				String linkPoint = ite.next();
				if(!routes.contains(linkPoint)){
					List<String> newList = new ArrayList<String>();
					newList.addAll(routes);
					newList.add(linkPoint);
					if(endPointMark.equals(linkPoint)){
						routesAll.add(newList);
//						break;
					}else{
						findRoutes(linkPoint,endPointMark,newList);
					}
				}
			}
		}
	}
	//查询出所有的rfid卡的点以及其相邻的rfid点
	@SuppressWarnings("unchecked")
	private void initPoints(){
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select ca.card_mark,ca.locationX,ca.locationY,GROUP_CONCAT(link_card_mark) linkPoints ");
		getListSql.append("from bp_card_tbl ca join bp_link_card_tbl l on (ca.card_mark = l.card_mark) ");
		getListSql.append("join bp_community_tbl c on ((c.id=? or c.dependent_community=?) and ca.communityId = c.id) "); 
		getListSql.append("group by ca.card_mark ");
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString(),new Object[]{communityId,communityId});
		Iterator<Map<String,Object>> ite = (Iterator<Map<String, Object>>) dataList.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			JSONObject point = new JSONObject();
			point.put("locationX", rowData.get("locationX"));
			point.put("locationY", rowData.get("locationY"));
			point.put("linkPoints", Arrays.asList(rowData.get("linkPoints").toString().split(",")));
			
			points.put(rowData.get("card_mark").toString(), point);
		}
	}
}
