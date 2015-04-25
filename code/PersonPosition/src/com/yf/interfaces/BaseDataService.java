package com.yf.interfaces;

import java.text.ParseException;
import java.util.Map;

import javax.sql.RowSet;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;
import com.yf.util.dbhelper.PrivateUtil;
import com.yf.util.dbhelper.PublicUtil;

public class BaseDataService {

	private static Logger logger = Logger.getLogger(BaseDataService.class);
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);

	public JSONObject saveBaseData(JSONObject json) {
		JSONObject resultObject = new JSONObject();
		try {
			Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
			String areaNo = packParam.get("C_ID").toString().trim().substring(0, 6);
			String d_No = packParam.get("D_NO").toString().trim();
			PublicUtil.createDatabaseTbl(areaNo);
			String getTime_sql = "select to_char(max(create_time),'yyyy-mm-dd HH24:MI:SS') as LAST_TIME from bp_database_"+ areaNo + "_tbl where d_no='"+d_No+"'";
			RowSet rs = dbhelper.select(getTime_sql);
			String lastTime = "";
			if(rs.next()){
				lastTime = rs.getString("LAST_TIME");
			}
			packParam.put("LAST_TIME", PrivateUtil.str2sqlTimestamp(lastTime));//上次记录时间
			packParam.put("CREATE_TIME", PrivateUtil.str2sqlTimestamp(""));//空为当前时间
			
			String inserSql = PrivateUtil.ctIstSl("BP_DATABASE_"+ areaNo + "_TBL", packParam);
			boolean bool = dbhelper.insert(inserSql, PrivateUtil.delNullArray(packParam.values().toArray()));
			if (bool) {
				resultObject.put("returnCode", 0);
				resultObject.put("data", new JSONObject());
				resultObject.put("desc", "添加基础数据成功");
			} else {
				resultObject.put("returnCode", -1);
				resultObject.put("data", new JSONObject());
				resultObject.put("desc", "添加基础数据失败");
			}
		} catch (Exception e) {
			logger.error("添加基础数据失败", e);
		}
		return resultObject;
	}

	public static void main(String[] args) throws ParseException {
		BaseDataService baseDataService = new BaseDataService();

		JSONObject json = new JSONObject();
		json.put("tradeCode", 231);
		JSONObject dataJson = new JSONObject();
		dataJson.put("D_NO", "333444");
		dataJson.put("D_TYPE", "dt001");
		dataJson.put("C_ID", "0101052000010");
		dataJson.put("C_TYPE", "2");
		dataJson.put("CREATE_TIME", "2012-12-12 11:11:11");
		dataJson.put("D_CURRENT", 12);
		dataJson.put("D_VOLTAGE", 21);
		dataJson.put("D_POWER", 22);
		dataJson.put("KWH", 6);
		dataJson.put("REMARK", "备注");
		json.put("data", dataJson);
		System.out.println(json);

		baseDataService.saveBaseData(json);
	}

}
