package com.yinfu.cloudportal.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Survey {
	private String routersn;
	private String clientMac;
	
	public Survey(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public List<Record> index(){
		StringBuffer sql = new StringBuffer();
		sql.append("select s.id survey_id,s.question,s.type,so.id option_id,so.option ");
		sql.append("from bp_device d join bp_survey s on (d.router_sn=? and d.shop_id=s.shop_id) ");
		sql.append("join bp_survey_option so on (s.id=so.survey_id) ");
		return Db.find(sql.toString(), new Object[]{routersn});
	}
	
	public void save(String options){
		try{
			List<String> surveyed = hasSurveyed();
			List<String> insertData = new ArrayList<String>();
			String[] optionArr = options.split("&amp;");
			for(int i=0;i<optionArr.length;i++){
				String survey = optionArr[i].split("=")[0];
				if(!surveyed.contains(survey)){
					insertData.add(optionArr[i]);
				}
			}
			if(insertData.size() > 0){
				Object[][] params = new Object[insertData.size()][4];
				for(int i=0;i<insertData.size();i++){
					String[] optionInfo = insertData.get(i).split("=");
					params[i] = new Object[]{routersn,clientMac,optionInfo[0],optionInfo[1]};
				}
				Db.batch("insert into bp_survey_result(router_sn,mac,survey_id,option_id,create_date) values(?,?,?,?,now()) ", params, params.length);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private List<String> hasSurveyed(){
		List<Record> list = Db.find("select distinct survey_id from bp_survey_result where mac=? ", new Object[]{clientMac});
		List<String> returnData = new ArrayList<String>();
		Iterator<Record> ite = list.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			returnData.add(rowData.getInt("survey_id")+"");
		}
		return returnData;
	}
	
}
