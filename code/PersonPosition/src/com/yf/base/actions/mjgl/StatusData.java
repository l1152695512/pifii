package com.yf.base.actions.mjgl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;


public class StatusData extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> nameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int limit;
	private int start;	
	private String dir;
	private String sort;
	private String cardId;
	private String name;
	private String communityId;

	static{
		nameToIndex.put("cardID", "prl.card_id");
		nameToIndex.put("comname", "com.name");
		nameToIndex.put("uploadtime", "prl.upload_time");
	}
	@Override
	public String execute() throws Exception {
	//String sql="select prl.phone,prl.card_id,prl.upload_time,c.`name` from bp_card_tbl c join bp_phone_rfid_location_tbl prl on (c.card_mark = prl.card_id) and to_days(prl.upload_time) = to_days(now());";
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("SELECT prl.id,prl.phone,prl.card_id cardID,com.name comname,c.name,TIMEDIFF(NOW(),prl.upload_time) uploadtime");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append(" from bp_phone_rfid_location_tbl prl");
		commonSql.append(" JOIN bp_card_tbl c on (prl.card_id=c.card_mark)");
		commonSql.append(" JOIN bp_community_tbl com on (c.communityId=com.id)");
		commonSql.append(" JOIN bp_person_tbl p on (p.communityId=com.id)");
		commonSql.append(" LEFT JOIN sys_dictionary_tbl d on (d.DIC_ID=p.dictionaryId)");
		commonSql.append(" WHERE 1=1");
		//sql.append(" WHERE to_days(prl.upload_time) = to_days(now())");
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			commonSql.append(" and com.user_id = '"+userId+"' ");
		}
		if(!StringUtils.isBlank(cardId)){
			commonSql.append(" and prl.card_id LIKE '"+DBHelper.wrapFuzzyQuery(cardId)+"'");
		}
		if(!StringUtils.isBlank(name)){
			commonSql.append(" and c.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"'");
		}
		if(!StringUtils.isBlank(communityId)){
			commonSql.append(" and c.communityId = '"+communityId+"'");
		}
		commonSql.append(" GROUP BY prl.card_id");
		dataSql.append(commonSql.toString());
		dataSql.append(" ORDER BY prl.upload_time,prl."+sort+" " +dir+" LIMIT "+start+","+limit);
		System.err.println(dataSql.toString());
		//System.out.println(sql.toString());
		List<?> dataList = dbhelper.getMapListBySql(dataSql.toString());
		
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(*) count from (SELECT prl.id,prl.phone,prl.card_id cardID,com.name comname,TIMEDIFF(NOW(),prl.upload_time) uploadtime,c.name");
		countSql.append(commonSql.toString());
		countSql.append(") b");
		System.out.println(countSql);
		List<Map<String,Object>> countList = (List<Map<String, Object>>) dbhelper.getMapListBySql(countSql.toString());
		long rowCount = (Long)countList.get(0).get("count");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("totalRecord", rowCount);
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

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

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

}