package com.yf.base.actions.mapposition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.Main;
import com.yf.tradecontrol.GlobalVar;
import com.yf.tradecontrol.JDomHandler;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PersonLocation extends ActionSupport {
	private static final long serialVersionUID = 1L;
//	private static final long phoneUploadInterval = 10*24*60*60*1000;//毫秒
	protected static String minSearchDateStr = "";
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String[] personIds;
	public static JDomHandler domHandler = new JDomHandler();
	
	static{
		domHandler.loadXmlByPath(Main.xmlpath);
		long phoneUploadInterval = 0;
		try {
			phoneUploadInterval = Long.parseLong(PersonLocation.domHandler.getNodeValue("/ds-config/location/uploadInterval"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(phoneUploadInterval > 0){
			Date minDate = new Date(new Date().getTime()-phoneUploadInterval);
			minSearchDateStr = format.format(minDate);
		}
	}
	
	@Override
	public String execute() throws Exception {
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from ");
		getListSql.append("(select p.phone,p.name,l.location_x,l.location_y,l.upload_date,if(l.phone_imsi is null,0,1) info ");
		getListSql.append("from bp_person_tbl p left join bp_phone_location_tbl l on (");
		if(!"".equals(minSearchDateStr)){
			getListSql.append("l.upload_date > '"+minSearchDateStr+"' and ");
		}
		getListSql.append("p.phone = l.phone_imsi) ");
		getListSql.append("where p.phone in ("+arrayToSQLIn(personIds)+") ");
		getListSql.append("order by l.upload_date desc) a ");
		getListSql.append("group by phone ");//去重
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString());
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	private String arrayToSQLIn(String[] params){
		if(null == params){
			return "''";
		}
		StringBuilder strB = new StringBuilder();
		for(int i=0;i<params.length;i++){
			strB.append("'"+params[i]+"',");
		}
		String sqlIn = strB.toString();
		if(sqlIn.length()>0){
			return sqlIn.substring(0, sqlIn.length()-1);
		}
		return "''";
	}
	
	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String[] getPersonIds() {
		return personIds;
	}
	
	public void setPersonIds(String[] personIds) {
		this.personIds = personIds;
	}
	
}
