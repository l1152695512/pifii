package com.yf.interfaces.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.yf.interfaces.phone.device.CustomerDevice;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.HibernateUUId;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;
import com.yf.util.dbhelper.PrivateUtil;

public class Device {

	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(CustomerDevice.class);
	
	public JSONObject getDeviceJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONObject object=json.getJSONObject("data");
		int start=object.getInt("START");
		int limit=object.getInt("LIMIT");
		String dName=object.getString("DNAME");
		String hId=object.getString("HID");
		int rowCount = 0;
        int totalpage = 0; 
		int currentPage = start / limit + 1;
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from (select m.*,to_char(m.CREATE_TIME,'yyyy-mm-dd HH24:MI:SS') as CREATETIME,rownum rn from(select PHOTO_URL,D_ID,H_ID,dt.NAME D_TYPE,D_NAME,decode(STATUS,'1','开通','2','未开通') STATUS,");
		getListSql.append("CREATE_TIME,decode(IS_OPEN,'1','开','2','关') IS_OPEN,t.REMARK from BP_DEVICE_TBL t left join ");
		getListSql.append("BP_DEVICE_TYPE_TBL dt on t.D_TYPE=dt.DT_ID where H_ID='"+ hId +"' order by D_ID) ");		
		getListSql.append("m where rownum <= "+limit+"*"+currentPage+") where rn > "+limit+"*("+currentPage+"-1)");
		if(!StringUtils.isBlank(dName)){
			getListSql.append(" and D_NAME like '%"+dName+"%'");
		}
		List dataList = dbhelper.getMapListBySql(getListSql.toString());
		String getTotSql = "select count(*) as rowCount from BP_DEVICE_TBL where H_ID='"+hId+"'";
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
	
	public JSONObject addDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			packParam.put("D_ID", new HibernateUUId().generate().toString());
			packParam.put("CREATE_TIME",PrivateUtil.str2sqlTimestamp(""));
			String inserSql = PrivateUtil.ctIstSl("BP_DEVICE_TBL", packParam);
			bool = dbhelper.insert(inserSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("SQL语法错误：INSERT BP_DEVICE_TBL", e);
		}
		if(bool){
			resultObject.put("returnCode", 200);
			JSONObject obj=new JSONObject();
			obj.put("D_ID", packParam.get("D_ID"));
			resultObject.put("data", obj);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 402);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端创建数据失败");
		}
		return resultObject;
	}
	
	public JSONObject getDeviceInfo(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		String dId=json.getJSONObject("data").getString("D_ID");
		if(!StringUtils.isBlank(dId)){
			String sql="select D_ID,D_NAME,STATUS,IS_OPEN,REMARK,PHOTO_URL from BP_DEVICE_TBL where D_ID='"+dId+"'";
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
	
	public JSONObject updateDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			final String updateSql = PrivateUtil.ctUpdateSl("BP_DEVICE_TBL", packParam,"where D_ID='"+packParam.get("D_ID").toString()+"'");
			bool = dbhelper.update(updateSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("SQL语法错误：UPDATE BP_DEVICE_TBL", e);
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
	
	public JSONObject deleteDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=json.getJSONArray("data");
		List<String> sqlList=new ArrayList<String>();
		for (int i=0;i<array.size();i++) {
			String id=array.getJSONObject(i).getString("id");
			String sql="delete from BP_DEVICE_TBL where D_ID='"+id+"'";
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
	
	public JSONObject getDeviceTypeJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=new JSONArray();
		Map map = new HashMap();
		String sql = "select distinct DT_ID,NAME from BP_DEVICE_TYPE_TBL";
		List list = dbhelper.getMapListBySql(sql);
		if(list.size() != 0){
			for (int i=0;i<list.size();i++) {
				JSONObject o=new JSONObject();
				map = (Map)list.get(i);
	    		o.put("DTID",map.get("DT_ID"));
	    		o.put("NAME",map.get("NAME"));
	    		array.add(o);	
			}
			resultObject.put("returnCode", 200);
			resultObject.put("data", array);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 401);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端请求数据失败");
		}
		return resultObject;
	}
}
