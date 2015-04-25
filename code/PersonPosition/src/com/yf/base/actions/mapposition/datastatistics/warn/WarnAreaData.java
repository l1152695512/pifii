package com.yf.base.actions.mapposition.datastatistics.warn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.datastatistics.CommonSearch;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class WarnAreaData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	
	private String name;
	private String warnAreaId;
	private String startDate;
	private String endDate;
	private String isLegal;//页面搜索违规记录或者正常记录的标识
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		List<Map<String,Object>> returnData = new ArrayList<Map<String,Object>>();
		if(CommonSearch.initSearchTable(dbhelper, startDate, endDate)){
			StringBuffer dataSql = new StringBuffer();
			dataSql.append("select wa.name area_name,p.name person_name,p.phone,sd.date,");
			dataSql.append("DATE_FORMAT(pl.upload_date,'%Y-%m-%d %H:%i:%s') position_date,pl.location_x,pl.location_y,t.start_time,t.end_time,");
			dataSql.append("t.area_type,wa.id area_id,p.id person_id,t.id area_time_id ");
			dataSql.append("from bp_coarse_area_time_assign_tbl ta ");
			dataSql.append("join bp_coarse_area_time_tbl t on (");
			if(StringUtils.isNotBlank(warnAreaId)){
				dataSql.append("t.area_id='"+warnAreaId+"' and ");
			}
			dataSql.append("t.id = ta.area_time_id) ");
			dataSql.append("join bp_coarse_area_tbl wa on (wa.id = t.area_id) ");
			
			dataSql.append("join bp_person_tbl p on (");
			if(StringUtils.isNotBlank(name)){
				dataSql.append("p.name like '"+DBHelper.wrapFuzzyQuery(name)+"' and ");
			}
			dataSql.append("p.id = ta.person_id) ");
			dataSql.append("join bp_search_date_tbl sd ");
			dataSql.append("left join bp_phone_location_tbl pl on (");
			if(StringUtils.isNotBlank(startDate)){
				dataSql.append("date(pl.upload_date) >= '"+startDate+"' and ");
			}
			if(StringUtils.isNotBlank(endDate)){
				dataSql.append("date(pl.upload_date) < '"+endDate+"' and ");
			}
			dataSql.append("date(pl.upload_date) = sd.date and TIME(pl.upload_date) >= t.start_time and TIME(pl.upload_date) <= t.end_time and pl.phone_imsi = p.phone) ");
			dataSql.append("order by wa.id,p.id,t.id,pl.upload_date desc ");
			System.err.println(dataSql.toString());
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(dataSql.toString());
			Map<String,List<Map<String,Object>>> warnAreas = Commons.getWarnAreas(dbhelper);
			if(null != warnAreas){
				Iterator<Map<String,Object>> iteDataList = dataList.iterator();
				String currentWarnAreaId = "";
				String currentPersonId = "";
				String currentAreaTimeId = "";
				String currentDate = "";
				while(iteDataList.hasNext()){
					Map<String,Object> rowData = iteDataList.next();
					//检验该条定位数据是否跳过检测
					if(!currentWarnAreaId.equals(rowData.get("area_id").toString())
							|| !currentPersonId.equals(rowData.get("person_id").toString())
							|| !currentAreaTimeId.equals(rowData.get("area_time_id").toString())
							|| !currentDate.equals(rowData.get("date").toString())){//加入一条新的记录
						
						currentWarnAreaId = rowData.get("area_id").toString();
						currentPersonId = rowData.get("person_id").toString();
						currentAreaTimeId = rowData.get("area_time_id").toString();
						currentDate = rowData.get("date").toString();
						
						boolean isLegal = checkPositionIsLegal(warnAreas,currentWarnAreaId,rowData.get("location_x"),rowData.get("location_y"),rowData.get("area_type").toString());
						
						rowData.remove("location_x");//去除不需要传到前台的数据
						rowData.remove("location_y");
						rowData.put("is_legal", isLegal?"1":"0");//加入记录是否合法的标识
						returnData.add(rowData);
						continue;
					}
					if("0".equals(returnData.get(returnData.size() - 1).get("is_legal").toString())){//该分组的记录已经不合法了，不需要再验证该分组的其他记录
						continue;
					}
					boolean isLegal = checkPositionIsLegal(warnAreas,currentWarnAreaId,rowData.get("location_x"),rowData.get("location_y"),rowData.get("area_type").toString());
					if(!isLegal){//如果该条记录违法，则更改该分组的合法标识
						returnData.get(returnData.size() - 1).put("is_legal", "0");
					}
				}
			}
		}
		System.err.println("returnData.size()============================="+returnData.size());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("totalRecord", returnData.size());
		resultMap.put("list", returnData.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}
	
	private boolean checkPositionIsLegal(Map<String,List<Map<String,Object>>> warnAreas,String areaId,Object locationX,Object locationY,String areaType){
		if(null == locationX || null == locationY){
			return true;
		}
		int pointPosition = Commons.checkPointInWarnArea(warnAreas.get(areaId),(Double)locationX,(Double)locationY);//为0的情况都算是合法的，即在线上
		if(("9".equals(areaType) && 1 == pointPosition) //进入了禁止进入的区域
				|| ("10".equals(areaType) && -1 == pointPosition)){//离开了规定的区域
			return false;
		}
		return true;
	}
	
	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWarnAreaId() {
		return warnAreaId;
	}
	
	public void setWarnAreaId(String warnAreaId) {
		this.warnAreaId = warnAreaId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getIsLegal() {
		return isLegal;
	}
	
	public void setIsLegal(String isLegal) {
		this.isLegal = isLegal;
	}

}
