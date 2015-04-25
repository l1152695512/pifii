package com.yf.base.actions.location;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.server.LocationAlgorithm;
import com.yf.base.actions.server.ReadDataFromTxt;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PersonLocation_2014_01_03 extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
//	private static ServiceApiStub serviceApiStub;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static LocationAlgorithm lAlgorithm;
	private String jsonString;
	private String communityId;
	
	static{
		ReadDataFromTxt rDataFromTxt=new ReadDataFromTxt();
		double[][] locat_wifi_data=rDataFromTxt.ReadData(GlobalVar.WORKPATH+File.separator+"config"+File.separator+"wifi.txt", 33, 3);
		double[][] locat_data=rDataFromTxt.ReadData(GlobalVar.WORKPATH+File.separator+"config"+File.separator+"pos.txt", 33, 3);
		lAlgorithm=	new LocationAlgorithm(locat_wifi_data, locat_data);
//		try {
//			serviceApiStub = new ServiceApiStub();
//		} catch (AxisFault e) {
//			e.printStackTrace();
//		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
//		String groupId = ActionContext.getContext().getSession().get("userGroupId").toString();
		StringBuffer getListSql = new StringBuffer();
//		getListSql.append("select p.id,DATE_FORMAT(l.date,'%Y-%m-%d %H:%i:%s') date,l.locationX,l.locationY ");
//		getListSql.append("from location_current l ");
//		getListSql.append("join bp_person_tbl p on (p.communityId='"+communityId+"' and l.userId = p.id) ");
		
		getListSql.append("select p.id,DATE_FORMAT(l.date,'%Y-%m-%d %H:%i:%s') date,l.locationX,l.locationY,if(h.id is null,'0','1') forHelp ");
		getListSql.append("from bp_location_current_tbl l ");
		getListSql.append("join bp_person_tbl p on (p.communityId='"+communityId+"' and l.personId = p.id) ");
		getListSql.append("left join test_help_event h on (p.id = h.person_id) ");
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString());
		
		Iterator<?> ite = dataList.iterator();
//		while(ite.hasNext()){
//			Map<String,Object> rowObject = (Map<String,Object>)ite.next();
//			if("0e7e280fd71c4296baec7df52b64dc7d".equals(rowObject.get("id"))){
////				double[] location = getLocation();
////				double[] posi_rssi={location[0],location[1]};
//				double[] lbs=lAlgorithm.location(getLocation(), 3, 3,0);
////				System.out.println("位置坐标为：x="+lbs[1]+";y="+lbs[2]);
//				rowObject.put("locationX", 260+52.5*lbs[2]);
//				rowObject.put("locationY", 100+77*lbs[1]);
//			}
//			if("B08E1A31033C".equals(rowObject.get("id"))){
//				//获取B08E1A31033C X坐标和Y坐标
//				double[] location = getLocation("B0:8E:1A:31:03:3C");
//				rowObject.put("locationX", location[0]);
//				rowObject.put("locationY", location[1]);
//			}
//			if("B08E1A300F4B".equals(rowObject.get("id"))){
//				//获取B08E1A300F4B X坐标和Y坐标
//				double[] location = getLocation("B0:8E:1A:30:0F:4B");
//				rowObject.put("locationX", location[0]);
//				rowObject.put("locationY", location[1]);
//			}
//		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	@SuppressWarnings("unchecked")
	private double[] getLocation(){
		double[] location = new double[2];
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select id,user_id,phone_id,wifi1,wifi2 ");
		getListSql.append("from bp_person_location_tbl limit 1 ");
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString());
		if(dataList.size() > 0){
			Map<String,Object> rowData = (Map<String,Object>)dataList.get(0);
			location[0] = Double.parseDouble(rowData.get("wifi1").toString());
			location[1] = Double.parseDouble(rowData.get("wifi2").toString());
		}
		return location;
	}
	
	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
//	private double[] getLocation(String personId){
//		double[] location = new double[]{0,0};
//		try{
//			GetTagStatus object = new GetTagStatus();
//			object.setTagMac(personId);
//			GetTagStatusResponse response = serviceApiStub.GetTagStatus(object);
//			GetTagStatusResult result = response.getGetTagStatusResult();
//			if(result.getX()>55){
//				location[0] = Math.abs(result.getX()*15);
//			}else{
//				location[0] = Math.abs(result.getX()*17);
//			}
//			location[1] = Math.abs(result.getY()*8);
//			System.err.println(result.getX()+"<<<<<<<<<<<<<<<<<--------->>>>>>>>>>>>>>>>>>>>>>"+result.getY());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return location;
//	}
}
