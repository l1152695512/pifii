package com.yf.base.actions.mapfloor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class Floor {
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	public static final String ADD_STR = "_copy";
	
	public void generFloorData(List<Map<String,Object>> dataList,String communityId){
		Map<String,Map<String,Object>> communityCards = getCommunityCards(communityId);
		
		List<Map<String,Object>> addData = new ArrayList<Map<String,Object>>();
		Iterator<Map<String,Object>> ite = dataList.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			Map<String,Object> personCopy = new HashMap<String, Object>();
			personCopy.putAll(rowData);
			personCopy.put("communityId", communityId);
			personCopy.put("id", rowData.get("id").toString()+ADD_STR);
			personCopy.put("card_mark", rowData.get("card_mark").toString()+ADD_STR);
			if(communityCards.containsKey(rowData.get("card_mark").toString()+ADD_STR)){//如果在楼层的地图中配置的楼层平面的对应点
				Map<String,Object> thisCard = communityCards.get(rowData.get("card_mark").toString()+ADD_STR);
				personCopy.put("locationX", thisCard.get("locationX"));
				personCopy.put("locationY", thisCard.get("locationY"));
				addData.add(personCopy);
			}
		}
		dataList.addAll(addData);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Map<String,Object>> getCommunityCards(String communityId){
		Map<String,Map<String,Object>> cards = new HashMap<String, Map<String,Object>>();
//		List<String> cards = new ArrayList<String>();
		List<Map<String,Object>> listData = (List<Map<String, Object>>) dbhelper.getMapListBySql("SELECT card_mark,locationX,locationY FROM bp_card_tbl WHERE communityId=? ", new Object[]{communityId});
		Iterator<Map<String,Object>> ite = listData.iterator();
		while(ite.hasNext()){
			Map<String,Object> row = ite.next();
			String card_mark = row.get("card_mark").toString();
			cards.put(card_mark, row);
		}
		return cards;
	}
	
	public void addFloorwareEvents(List<Map<String,Object>> dataList,String personId,JSONArray events){
		Iterator<Map<String,Object>> ite = dataList.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			if(null != rowData.get("id") && rowData.get("id").equals(personId+ADD_STR)){
				rowData.put("wareEvents", events.toString());
				break;
			}
		}
	}
}
