package com.yf.base.actions.mapposition.datastatistics.warn;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class Commons {
	/**
	 * 检查方式：从检查点画一条平行的射线，如果与区域的交叉点个数为奇数，则检查点在区域内，为偶数则在区域外，
	 * @param warnAreaPoints
	 * @param locationX
	 * @param locationY
	 * @return -1代表在区域外，0代表在区域的线上，1代表在区域内
	 */
	public static int checkPointInWarnArea(List<Map<String,Object>> warnAreaPoints,double locationX,double locationY){
		int crossPointNum = 0;
		for(int i=0;i<warnAreaPoints.size();i++){
			Map<String,Object> startPoint = warnAreaPoints.get(i);
			Map<String,Object> endPoint = warnAreaPoints.get((i+1)%warnAreaPoints.size());
			double startPointX = (Double)startPoint.get("location_x");
			double startPointY = (Double)startPoint.get("location_y");
			double endPointX = (Double)endPoint.get("location_x");
			double endPointY = (Double)endPoint.get("location_y");
			double maxY = startPointY > endPointY?startPointY:endPointY;
			double minY = startPointY > endPointY?endPointY:startPointY;
			if(locationY<minY || locationY>maxY){//证明该点在区域当前相邻点组成的线无交叉点
				continue;
			}
			double crossPointX = startPointX - (startPointX-endPointX)*(startPointY-locationY)/(startPointY-endPointY);
			if(Double.parseDouble(new DecimalFormat("#0.0000000").format(crossPointX)) == locationX){//此处要使用约等于后的小数位数个数大于等于坐标的小数个数
				return 0;
			}
			if(crossPointX < locationX){
				crossPointNum++;
			}
		}
		if(crossPointNum%2==0){
			return -1;
		}else{
			return 1;
		}
	}
	@SuppressWarnings("unchecked")
	public static int checkPointInWarnArea(DBHelper dbhelper,String areaId,double locationX,double locationY){
		StringBuffer sql = new StringBuffer();
		sql.append("select id,name,previous_point_id,next_point_id,location_x,location_y,description,DATE_FORMAT(add_date,'%Y-%m-%d %H:%i:%s') add_date ");
		sql.append("from bp_coarse_area_point_tbl ");
		sql.append("where area_id = '");
		sql.append(areaId);
		sql.append("' ");
		sql.append("order by previous_point_id ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
		//排序
		List<Map<String,Object>> warnAreaPoints = new ArrayList<Map<String,Object>>();
		if(dataList.size()>0){
			warnAreaPoints.add(dataList.get(0));
			dataList.remove(0);
		}
		while(dataList.size()>0){
			String nextPointId = warnAreaPoints.get(warnAreaPoints.size()-1).get("next_point_id").toString();
			if(StringUtils.isBlank(nextPointId)){
				break;
			}
			Map<String,Object> nextPoint = findPoint(dataList,nextPointId);
			if(null == nextPoint){
				break;
			}
			warnAreaPoints.add(nextPoint);
		}
		return checkPointInWarnArea(warnAreaPoints,locationX,locationY);
	}
	@SuppressWarnings("unchecked")
	public static Map<String,List<Map<String,Object>>> getWarnAreas(DBHelper dbhelper){//Map<warnAreaId,List<warnAreaPointIdMap<warnAreaPointId,>>>
		StringBuffer sql = new StringBuffer();
		sql.append("select id,area_id,name,previous_point_id,next_point_id,location_x,location_y ");
		sql.append("from bp_coarse_area_point_tbl ");
		sql.append("order by area_id,previous_point_id ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
		//排序
		Map<String,List<Map<String,Object>>> warnAreas = new HashMap<String, List<Map<String,Object>>>();
		List<Map<String,Object>> currentWarnArea = null;
		while(dataList.size()>0){
			if(null != currentWarnArea && currentWarnArea.size() >0 
					&& null != currentWarnArea.get(currentWarnArea.size()-1).get("next_point_id") 
					&& !"".equals(currentWarnArea.get(currentWarnArea.size()-1).get("next_point_id").toString().trim())){
				String nextPointId = currentWarnArea.get(currentWarnArea.size()-1).get("next_point_id").toString();
				Map<String,Object> nextPoint = findPoint(dataList,nextPointId);
				if(null == nextPoint){
					return null;
				}
				currentWarnArea.add(nextPoint);
			}else{
				Map<String,Object> rowData = dataList.get(0);
				if(!"".equals(rowData.get("previous_point_id").toString().trim())){
					return null;
				}
				String warnAreaId = rowData.get("area_id").toString();
				warnAreas.put(warnAreaId, new ArrayList<Map<String,Object>>());
//				warnAreas.get(warnAreaId).add(rowData);
				currentWarnArea = warnAreas.get(warnAreaId);
				currentWarnArea.add(rowData);
				dataList.remove(0);
			}
		}
		return warnAreas;
	}
	
	private static Map<String,Object> findPoint(List<Map<String,Object>> dataList,String pointId){
		Map<String,Object> findData = null;
		for(int i=0;i<dataList.size();i++){
			if(dataList.get(i).get("id").equals(pointId)){
				findData = dataList.get(i);
				dataList.remove(i);
				return findData;
			}
		}
		return findData;
	}
}
