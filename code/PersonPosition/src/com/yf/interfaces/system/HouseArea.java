package com.yf.interfaces.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.yf.interfaces.phone.device.CustomerDevice;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.HibernateUUId;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;
import com.yf.util.dbhelper.PrivateUtil;

public class HouseArea {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(CustomerDevice.class);
	
	public JSONObject getHouseAreaJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONObject object=json.getJSONObject("data");
		int start=object.getInt("START");
		int limit=object.getInt("LIMIT");
		String hName=object.getString("HNAME");
		String account=object.getString("ACCOUNT");
		int rowCount = 0;
        int totalpage = 0; 
		int currentPage = start / limit + 1;
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from (select m.*,rownum rn from(select H_ID,ACCOUNT,H_NAME,REMARK,PHOTO_URL from BP_HOUSE_TBL ");
		getListSql.append("where ACCOUNT='"+account+"' order by H_ID) m where rownum <= "+limit+"*"+currentPage+") where rn > "+limit+"*("+currentPage+"-1)");
		if(!StringUtils.isBlank(hName)){
			getListSql.append(" and H_NAME like '%"+hName+"%'");
		}
		List dataList = dbhelper.getMapListBySql(getListSql.toString());
		String getTotSql = "select count(*) as rowCount from BP_HOUSE_TBL where ACCOUNT='"+account+"'";
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
		if(!dataList.isEmpty()){
			resultObject.put("returnCode", 200);
			resultObject.put("data", result);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 401);
			resultObject.put("data", result);
			resultObject.put("desc", "客户端请求数据失败");
		}
		return resultObject;
	}
	
	public JSONObject addHouseArea(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			packParam.put("H_ID", new HibernateUUId().generate().toString());
			String inserSql = PrivateUtil.ctIstSl("BP_HOUSE_TBL", packParam);
			bool = dbhelper.insert(inserSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("SQL语法错误：INSERT BP_HOUSE_TBL", e);
		}
		if(bool){
			resultObject.put("returnCode", 200);
			JSONObject obj=new JSONObject();
			obj.put("H_ID", packParam.get("H_ID"));
			resultObject.put("data", obj);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 402);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端创建数据失败");
		}
		return resultObject;
	}
	
	public JSONObject getHouseAreaInfo(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		String hId=json.getJSONObject("data").getString("H_ID");
		if(!StringUtils.isBlank(hId)){
			String sql="select H_ID,ACCOUNT,H_NAME,REMARK,PHOTO_URL from BP_HOUSE_TBL where H_ID='"+hId+"'";
			List list = dbhelper.getMapListBySql(sql);
			String str=JsonUtils.list2json(list);
			String result=str.substring(1, str.length()-1);
			if (list.size() != 0){
				resultObject.put("returnCode", 200);
				resultObject.put("data", result);
				resultObject.put("desc", "客户端操作成功");
			}else{
				resultObject.put("returnCode", 401);
				resultObject.put("data", new JSONObject());
				resultObject.put("desc", "客户端请求数据失败");
			}
		}
		return resultObject;
	}
	
	public JSONObject updateHouseArea(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			final String updateSql = PrivateUtil.ctUpdateSl("BP_HOUSE_TBL", packParam,"where H_ID='"+packParam.get("H_ID").toString()+"'");
			bool = dbhelper.update(updateSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("SQL语法错误：UPDATE BP_HOUSE_TBL", e);
		}
		if(bool){
			resultObject.put("returnCode", 200);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 403);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端修改数据失败");
		}
		return resultObject;
	}
	
	public JSONObject deleteHouseArea(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=json.getJSONArray("data");
		List<String> sqlList=new ArrayList<String>();
		for (int i=0;i<array.size();i++) {
			String id=array.getJSONObject(i).getString("id");
			String sql="delete from BP_HOUSE_TBL where H_ID='"+id+"'";
			sqlList.add(sql);
		}
		boolean bool=dbhelper.executeFor(sqlList);
		if(bool){
			resultObject.put("returnCode", 200);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 404);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端删除数据失败");
		}
		return resultObject;
	}

}
