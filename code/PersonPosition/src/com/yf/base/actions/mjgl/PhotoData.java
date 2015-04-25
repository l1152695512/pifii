package com.yf.base.actions.mjgl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.xwork.StringUtils;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PhotoData extends ActionSupport{
private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String id;
	private String startDate;
	private String endDate;
	
	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		//getListSql.append("SELECT smallImageSrc from photos ");
		//getListSql.append("where id='"+id+"' ");
		sql.append("select bpt.smallImageSrc,bpt.bigImageSrc,date(bpt.phototime) photoDate,time(bpt.phototime) photoTime ");
		sql.append("from bp_photos_tbl bpt join bp_person_tbl p ");
		sql.append("on(bpt.phone=p.phone) ");
		sql.append("where p.id= '"+id+"' ");
		
		//sql.append("and phototime between '"+startDate+"' and '"+endDate+"'" );
		//and bpt.phototime>='2014-07-09 09:26:46' and bpt.phototime<'2014-07-11 11:37:38'
		if(StringUtils.isNotBlank(startDate)){
			sql.append("and phototime between '"+startDate+"' " );
		}
		if(StringUtils.isNotBlank(endDate)){
			sql.append("and '"+endDate+"' ");
		}
		sql.append("ORDER BY bpt.phototime desc ");
		System.out.println(sql);
		
		List<?> dataList = dbhelper.getMapListBySql(sql.toString());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	


}
