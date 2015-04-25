package com.yf.interfaces.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.yf.interfaces.phone.device.CustomerDevice;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;
import com.yf.util.dbhelper.PrivateUtil;

public class Customer {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(CustomerDevice.class);
	
	public JSONObject addCustomer(JSONObject json){
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			String sql="select CODE from YW_REGION_TBL where RID='"+packParam.get("AREA").toString()+"'";
			RowSet rs=dbhelper.select(sql);
			String areaNo= null;
			if(rs.next()){
				areaNo=rs.getString("CODE");
			}else{
				resultObject.put("error", "不存在该区域编码");
				logger.error("区域：["+packParam.get("AREA").toString()+"]不存在对应编码");
				return resultObject;
			}
			String account=PrivateUtil.getCustomerNo(areaNo, packParam.get("C_TYPE").toString());
			packParam.put("ACCOUNT", account);
			packParam.put("CREATE_TIME",PrivateUtil.str2sqlTimestamp(""));
			String inserSql = PrivateUtil.ctIstSl("BP_CUSTOMER_TBL", packParam);
			bool = dbhelper.insert(inserSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("SQL语法错误：INSERT BP_CUSTOMER_TBL", e);
		}
		if(bool){
			resultObject.put("returnCode", 200);
			JSONObject obj=new JSONObject();
			obj.put("ACCOUNT", packParam.get("ACCOUNT"));
			resultObject.put("data", obj);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 402);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端创建数据失败");
		}
		return resultObject;
	}
	
	public JSONObject updateCustomer(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			final String updateSql = PrivateUtil.ctUpdateSl("BP_CUSTOMER_TBL", packParam,"where ACCOUNT='"+packParam.get("ACCOUNT").toString()+"'");
			bool = dbhelper.update(updateSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("SQL语法错误：UPDATE BP_CUSTOMER_TBL", e);
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
	
	public JSONObject deleteCustomer(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=json.getJSONArray("data");
		List<String> sqlList=new ArrayList<String>();
		for (int i=0;i<array.size();i++) {
			String id=array.getJSONObject(i).getString("id");
			String sql="delete from BP_CUSTOMER_TBL where ACCOUNT='"+id+"'";
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
	
	public JSONObject getCustomerJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONObject object=json.getJSONObject("data");
		int start=object.getInt("START");
		int limit=object.getInt("LIMIT");
		String cName=object.getString("CNAME");
		int rowCount = 0;
        int totalpage = 0; 
		int currentPage = start / limit + 1;
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from (select m.*,to_char(m.CREATE_TIME,'yyyy-mm-dd HH24:MI:SS') as createTime,rownum rn from(select decode(C_TYPE,'1','企业','2','家庭') C_TYPE,C_NAME,ID_NUMBER,");
		getListSql.append("RP.NAME PROVINCE,RC.NAME CITY,RA.NAME AREA,PHONE,CREATE_TIME,REMARK,PASSWORD,ACCOUNT,");
		getListSql.append("USER_IMG,FAMILY_IMG,USER_GRADE,FAMILY_ADDRESS,USER_SIGN from BP_CUSTOMER_TBL t left join ");
		getListSql.append("YW_REGION_TBL RP on t.PROVINCE=RP.RID left join YW_REGION_TBL RC on t.CITY=RC.RID left join YW_REGION_TBL RA on ");		
		getListSql.append("t.AREA=RA.RID order by ACCOUNT) m where rownum <= "+limit+"*"+currentPage+") where rn > "+limit+"*("+currentPage+"-1)");
		if(!StringUtils.isBlank(cName)){
			getListSql.append(" and C_NAME like '%"+cName+"%'");
		}
		List dataList = dbhelper.getMapListBySql(getListSql.toString());
		String getTotSql = "select count(*) as rowCount from BP_CUSTOMER_TBL";
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
	
	public JSONObject getCustomerInfo(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		String account=json.getJSONObject("data").getString("ACCOUNT");
		if(!StringUtils.isBlank(account)){
			String sql="select ACCOUNT,C_NAME,ID_NUMBER,PHONE,REMARK,PASSWORD,FAMILY_ADDRESS from BP_CUSTOMER_TBL where ACCOUNT='"+account+"'";
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

}
