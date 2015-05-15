package com.yinfu.servlet.route.fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Survey {
	private String routersn;
	private String clientMac;
	
	public Survey(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String rid = request.getParameter("rid");
		if(StringUtils.isNotBlank(rid)){
			int index = rid.lastIndexOf("_");
			Commons.addAccessData(routersn,clientMac,rid.substring(index+1),rid.substring(0, index));
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select s.id survey_id,s.question,s.type,so.id option_id,so.option ");
		sql.append("from bp_device d join bp_survey s on (d.router_sn=? and d.shop_id=s.shop_id) ");
		sql.append("join bp_survey_option so on (s.id=so.survey_id) ");
		List<Record> questions = Db.find(sql.toString(), new Object[]{routersn});
		request.setAttribute("questions", JsonKit.toJson(questions));
		request.getRequestDispatcher("/page/routerapp/survey/index.jsp").forward(request,response);
	}
	
	public void save(String options){
		try{
			List<String> surveyed = hasSurveyed();
			List<String> insertData = new ArrayList<String>();
			String[] optionArr = options.split("&");
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
					Object surveyId = "";
					Object optionId = "";
					if(optionInfo.length > 0){
						surveyId = optionInfo[0];
					}
					if(optionInfo.length > 1){
						optionId = optionInfo[1];
					}
					params[i] = new Object[]{routersn,clientMac,surveyId,optionId};
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
