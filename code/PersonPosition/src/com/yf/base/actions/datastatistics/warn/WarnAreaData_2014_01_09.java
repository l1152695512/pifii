package com.yf.base.actions.datastatistics.warn;

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

public class WarnAreaData_2014_01_09 extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
//	private int start;
//	private int limit;
//	private String dir;
//	private String sort;
	
	private String name;
	private String communityId;
	private String warnAreaId;
	private String startDate;
	private String endDate;
	private String isLegal;//前台搜索违规记录或者正常记录
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		List<Map<String,Object>> returnData = new ArrayList<Map<String,Object>>();
		if(CommonSearch.initSearchTable(dbhelper, startDate, endDate)){
			StringBuffer dataSql = new StringBuffer();
			dataSql.append("select c.name community_name,wa.name warn_area_name,p.name person_name,sd.date,DATE_FORMAT(date(lh.date),'%Y-%m-%d') position_date,lh.locationX,lh.locationY,wa.type,wa.id warn_area_id,p.id person_id ");
			dataSql.append("from bp_warn_area_person_assign_tbl pa ");
			dataSql.append("join bp_fine_area_tbl wa on (");
			if(StringUtils.isNotBlank(warnAreaId)){
				dataSql.append("wa.id = '"+warnAreaId+"' and ");
			}
			dataSql.append("wa.is_used and wa.id = pa.warn_area_id) ");
			dataSql.append("join bp_community_tbl c on (");
			if(StringUtils.isNotBlank(communityId)){
				dataSql.append("c.id = '"+communityId+"' and ");
			}
			dataSql.append("c.id = wa.community_id) ");
			dataSql.append("join bp_person_tbl p on (");
			if(StringUtils.isNotBlank(name)){
				dataSql.append("p.name like '"+DBHelper.wrapFuzzyQuery(name)+"' and ");
			}
			dataSql.append("p.id = pa.person_id) ");
			dataSql.append("join bp_search_date_tbl sd ");
			dataSql.append("left join bp_location_history_tbl lh on (");
			if(StringUtils.isNotBlank(startDate)){
				dataSql.append("date(lh.date) >= '"+startDate+"' and ");
			}
			if(StringUtils.isNotBlank(endDate)){
				dataSql.append("date(lh.date) < '"+endDate+"' and ");
			}
			dataSql.append("date(lh.date) = sd.date and TIME(lh.date) >= wa.effective_start_time and TIME(lh.date) <= wa.effective_end_time and lh.personId = p.id) ");
			dataSql.append("order by c.id,wa.id,p.id,lh.date desc ");
			System.err.println(dataSql.toString());
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(dataSql.toString());
			if(dataList.size() > 0){
				Map<String,Object> firstRowData = dataList.get(0);
				String currentWarnAreaId = firstRowData.get("warn_area_id").toString();
				String currentPersonId = firstRowData.get("person_id").toString();
				String currentDate = firstRowData.get("date").toString();
				Map<String,Object> previousRowData = dataList.remove(0);
				boolean isSkip = false;
				Map<String,List<Map<String,Object>>> warnAreas = Commons.getWarnAreas(dbhelper);
				if(null != warnAreas){
					Iterator<Map<String,Object>> ite = dataList.iterator();
					while(ite.hasNext()){
						Map<String,Object> rowData = ite.next();
//						if(null == rowData.get("position_date")){
//							currentDate = "";
//							rowData.put("is_legal", "-1");//记录合法
//							returnData.add(rowData);
//							isSkip = true;
//							continue;
//						}
						if(!currentWarnAreaId.equals(rowData.get("warn_area_id").toString())
								|| !currentPersonId.equals(rowData.get("person_id").toString())
								|| !currentDate.equals(rowData.get("date").toString())){
							currentWarnAreaId = rowData.get("warn_area_id").toString();
							currentPersonId = rowData.get("person_id").toString();
							currentDate = rowData.get("date").toString();
							if(!isSkip && !"0".equals(isLegal)){
								previousRowData.put("is_legal", "1");//记录合法
								returnData.add(previousRowData);
							}
							isSkip = false;
							previousRowData = rowData;
						}
						if(!isSkip && null != rowData.get("position_date")){
							int pointPosition = Commons.checkPointInWarnArea(warnAreas.get(rowData.get("warn_area_id").toString()),(Double)rowData.get("locationX"),(Double)rowData.get("locationY"));
							if(("9".equals(rowData.get("type").toString()) && 1 == pointPosition) 
									|| ("10".equals(rowData.get("type").toString()) && -1 == pointPosition)){
								if(!"1".equals(isLegal)){
									rowData.put("is_legal", "0");//违规标志
									returnData.add(rowData);
								}
								isSkip = true;
								continue;
							}
						}
					}
					if(!isSkip && !"0".equals(isLegal)){//最后一条记录在while中没有放进去
						previousRowData.put("is_legal", "1");//记录合法
						returnData.add(previousRowData);
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
	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

//	public int getStart() {
//		return start;
//	}
//
//	public void setStart(int start) {
//		this.start = start;
//	}
//
//	public int getLimit() {
//		return limit;
//	}
//
//	public void setLimit(int limit) {
//		this.limit = limit;
//	}
//
//	public String getSort() {
//		return sort;
//	}
//	
//	public void setSort(String sort) {
//		this.sort = sort;
//	}
//	
//	public String getDir() {
//		return dir;
//	}
//	
//	public void setDir(String dir) {
//		this.dir = dir;
//	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
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
