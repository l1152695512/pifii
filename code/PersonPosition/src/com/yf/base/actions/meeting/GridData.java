package com.yf.base.actions.meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GridData extends ActionSupport {

	private String jsonString;
	private String queryInfo;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	
	@Override
	public String execute() throws Exception {	
		List<Map<String, String>> dataList=new ArrayList<Map<String, String>>();
		String [] strDate=new String []{"8:30:00","9:00:00","9:30:00","10:00:00","10:30:00","11:00:00","11:30:00","12:00:00","12:30:00","13:00:00","13:30:00","14:00:00","14:30:00","15:00:00","15:30:00","16:00:00","16:30:00","17:00:00","17:30:00"};
		String [][] strArray=new String [strDate.length-1][9];
		if(StringUtils.isBlank(queryInfo)){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			queryInfo=df.format(new Date());
		}
		for(int i=0;i<strDate.length-1;i++){
			strArray[i][0]=strDate[i]+"-"+strDate[i+1];
			for(int j=1;j<9;j++){
				String sql="select M_NAME,CHARGE from BP_MEETING_TBL where START_TIME<=to_date('"+queryInfo.substring(0, 10)+" "+strDate[i]+"','yyyy-MM-dd HH24:mi:ss') and END_TIME>=to_date('"+queryInfo.substring(0, 10)+" "+strDate[i+1]+"','yyyy-MM-dd HH24:mi:ss') and M_ROOM='"+j+"'";
				RowSet rs=dbhelper.select(sql);
				if(rs.next()){
					strArray[i][j]=rs.getString("CHARGE")+":"+rs.getString("M_NAME");
				}else {
					strArray[i][j]="空闲";
				}
			}
		}
		for(int i=0;i<strDate.length-1;i++){
			Map<String, String> map=new HashMap<String, String>();
			map.put("DATE", strArray[i][0]);
			map.put("ONE", strArray[i][1]);
			map.put("TWO", strArray[i][2]);
			map.put("THREE", strArray[i][3]);
			map.put("FOUR", strArray[i][4]);
			map.put("FIVE", strArray[i][5]);
			map.put("SIX", strArray[i][6]);
			map.put("SEVEN", strArray[i][7]);
			map.put("EIGHT", strArray[i][8]);
			dataList.add(map);
		}
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

	public String getQueryInfo() {
		return queryInfo;
	}

	public void setQueryInfo(String queryInfo) {
		this.queryInfo = queryInfo;
	}

}
