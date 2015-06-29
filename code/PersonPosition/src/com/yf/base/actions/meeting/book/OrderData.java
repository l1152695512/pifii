package com.yf.base.actions.meeting.book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class OrderData extends ActionSupport {

	private int start;
	private int limit = 20;
	private String jsonString;
	private String userName;
	private String queryInfo;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);

	@Override
	public String execute() throws Exception {
		int rowCount = 0;
        int totalpage = 0; 
		int currentPage = start / limit + 1;
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from (select m.*,rownum rn from(select M_ID,decode(M_TYPE,'1','招投标会议','2','其他') M_TYPE,M_NAME,");
		getListSql.append("decode(M_ROOM,'1','1号会议室','2','2号会议室','3','3号会议室','4','4号会议室','5','5号会议室','6','6号会议室') M_ROOM,");
		getListSql.append("to_char(ORDER_TIME,'yyyy-mm-dd HH24:MI:SS') ORDER_TIME,to_char(START_TIME,'yyyy-mm-dd HH24:MI:SS') START_TIME,to_char(END_TIME,'yyyy-mm-dd HH24:MI:SS') END_TIME,");
		getListSql.append("PROJECT,COMPANY,JOIN,W_JOIN,MEMO from BP_MEETING_TBL where CHARGE='"+userName+"' order by ORDER_TIME desc) m ");		
		getListSql.append("where rownum <= "+limit+"*"+currentPage+") where rn > "+limit+"*("+currentPage+"-1)");
		if(!StringUtils.isBlank(queryInfo)){
			getListSql.append(" and M_NAME like '%"+queryInfo+"%'");
		}
		List dataList = dbhelper.getMapListBySql(getListSql.toString());
		String getTotSql = "select count(*) as rowCount from BP_MEETING_TBL where CHARGE='"+userName+"'";
		List<Map> totList = (List<Map>) dbhelper.getMapListBySql(getTotSql);
		if(!totList.isEmpty()){
			Map map = totList.get(0);
			rowCount = Integer.parseInt(map.get("rowCount").toString());
		}
		totalpage = rowCount%limit ==0 ? rowCount/limit : rowCount/limit + 1 ;
		Map resultMap = new HashMap();
		resultMap.put("currentPage", currentPage);
		resultMap.put("firstPage", totalpage != 1 && currentPage != 1 ? false : true);
		resultMap.put("lastPage", totalpage>currentPage ? false : true);
		resultMap.put("pageSize", limit);
		resultMap.put("totalPage", totalpage);
		resultMap.put("listSize", dataList.size());
		resultMap.put("totalRecord", rowCount);
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getQueryInfo() {
		return queryInfo;
	}

	public void setQueryInfo(String queryInfo) {
		this.queryInfo = queryInfo;
	}
	
}
