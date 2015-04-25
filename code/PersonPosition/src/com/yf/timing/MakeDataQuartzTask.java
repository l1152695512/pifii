package com.yf.timing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;


/***
 * 定时生成基础数据
 * @author Administrator
 *
 */
public class MakeDataQuartzTask {
	private static Logger logger = Logger.getLogger(MakeDataQuartzTask.class);
	public void makeData() {
		DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
		String sql = "SELECT ID,COMMUNITYID FROM bp_person_tbl";
		List<String> sqlList = new ArrayList<String>();
		try {
			RowSet rs = dbhelper.select(sql);
			while(rs.next()){
				String comm = rs.getString("COMMUNITYID");
				String user = rs.getString("ID");
				String getDataSql= "SELECT personId,COMMUNITYID,LOCATIONX,LOCATIONY FROM bp_location_current_tbl WHERE personId = '"+user+"'";
				RowSet data = dbhelper.select(getDataSql);
				String key = UUID.randomUUID().toString().replaceAll("-", "");
				if(data.next()){
					int type = (int)(Math.random()*2);
					int x = (int)(Math.random()*100);
					int y = (int)(Math.random()*100);
					double lx = data.getDouble("LOCATIONX");
					double ly = data.getDouble("LOCATIONY");
					double xx = 0;
					double yy = 0;
					if(type == 1){
						xx = x+lx;
						yy = y+ly;
						if(xx>1000){
							xx = lx-x;
						}
						if(yy>600){
							yy = ly-y;
						}
					}else{
						xx = lx-x;
						yy = ly-y;
						if(xx<10){
							xx = x+lx;
						}
						if(yy<10){
							yy = y+ly;
						}
					}
					String upSql = "UPDATE bp_location_current_tbl SET date=now(),locationX= "+xx+" ,locationY= "+yy+" WHERE personId='"+user+"'";
					String inSql = "INSERT INTO bp_location_history_tbl VALUES('"+key+"','"+user+"','"+comm+"',NOW(),"+xx+","+yy+")";
					sqlList.add(upSql);
					sqlList.add(inSql);
				}else{
					int xx = (int)(Math.random()*1000);
					int yy = (int)(Math.random()*500);
					String inSql2 = "INSERT INTO bp_location_current_tbl VALUES('"+key+"','"+user+"','"+comm+"',NOW(),"+xx+","+yy+")";
					String inSql = "INSERT INTO bp_location_history_tbl VALUES('"+key+"','"+user+"','"+comm+"',NOW(),"+xx+","+yy+")";
					sqlList.add(inSql2);
					sqlList.add(inSql);
				}
			}
			if(sqlList.size()>0){
				dbhelper.executeFor(sqlList);
			}
		} catch (Exception e) {
			logger.error("生成数据失败",e);
		}
		
	}
	@SuppressWarnings("unchecked")
	public void makeData2() {
		DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
		List<String> sqlList = new ArrayList<String>();
		String sql = "select phone from bp_person_tbl";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		Iterator<Map<String,Object>> ite = dataList.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			String phone = rowData.get("phone").toString();
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			double locationX = 113.3+Math.random()*0.1;
			double locationY = 23.1+Math.random()*0.1;
			sqlList.add("insert into bp_phone_location_tbl (id,phone_imsi,upload_date,location_x,location_y) values('"+id+"','"+phone+"',now(),"+locationX+","+locationY+")");
		}
		dbhelper.executeFor(sqlList);
	}
}
