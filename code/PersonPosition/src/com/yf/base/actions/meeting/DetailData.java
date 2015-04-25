package com.yf.base.actions.meeting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class DetailData extends ActionSupport {

	private String jsonString;
	private String userName;
	private String date1;
	private String date2;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);

	@Override
	public String execute() throws Exception {
		String user=userName.substring(0,userName.indexOf(":"));
		String start=date1.substring(0,10)+" "+date2.substring(0,date2.indexOf("-"));
		String end=date1.substring(0,10)+" "+date2.substring(date2.indexOf("-")+1);
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select M_ID,CHARGE,decode(M_TYPE,'1','招投标会议','2','其他') M_TYPE,M_NAME,");
		getListSql.append("decode(M_ROOM,'1','1号会议室','2','2号会议室','3','3号会议室','4','4号会议室','5','5号会议室','6','6号会议室') M_ROOM,");
		getListSql.append("to_char(ORDER_TIME,'yyyy-mm-dd HH24:MI:SS') ORDER_TIME,to_char(START_TIME,'yyyy-mm-dd HH24:MI:SS') START_TIME,to_char(END_TIME,'yyyy-mm-dd HH24:MI:SS') END_TIME,");
		getListSql.append("PROJECT,COMPANY,JOIN,W_JOIN,MEMO from BP_MEETING_TBL where CHARGE='"+user+"' and START_TIME<=to_date('"+start+"','yyyy-mm-dd HH24:MI:SS') and END_TIME>=to_date('"+end+"','yyyy-mm-dd HH24:MI:SS')");		
		List dataList = dbhelper.getMapListBySql(getListSql.toString());
		Map resultMap = new HashMap();
		resultMap.put("list", dataList.toArray());
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

}
