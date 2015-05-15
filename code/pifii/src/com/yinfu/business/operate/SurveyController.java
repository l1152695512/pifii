
package com.yinfu.business.operate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/survey", viewPath = "/page/business/")
public class SurveyController extends Controller<Device> {
	public void index(){
		render("operate/survey.jsp");
	}
	
	public void list(){
		StringBuffer sql = new StringBuffer();
		sql.append("select s.id survey_id,s.question,so.id option_id,so.option,count(distinct sr.mac) option_count ");
		sql.append("from bp_survey s join bp_survey_option so on (s.shop_id=? and s.delete_date is null and s.id=so.survey_id) ");
		sql.append("left join bp_survey_result sr on (sr.survey_id=s.id and sr.option_id=so.id) ");
		sql.append("group by s.id,so.id ");
		List<Record> options = Db.find(sql.toString(), new Object[]{getPara("shopId")});
		
		StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("select survey_id,sum(option_count) count ");
		sqlCount.append("from ( ");
		sqlCount.append("select s.id survey_id,so.id option_id,count(distinct sr.mac) option_count ");
		sqlCount.append("from bp_survey s join bp_survey_option so on (s.shop_id=? and s.delete_date is null and s.id=so.survey_id) ");
		sqlCount.append("left join bp_survey_result sr on (sr.survey_id=s.id and sr.option_id=so.id) ");
		sqlCount.append("group by s.id,so.id ");
		sqlCount.append(") a ");
		sqlCount.append("group by survey_id ");
		List<Record> surveyOptionsCount = Db.find(sqlCount.toString(), new Object[]{getPara("shopId")});
		Map<Integer,Long> countMap = new HashMap<Integer,Long>();
		Iterator<Record> iteCount = surveyOptionsCount.iterator();
		while(iteCount.hasNext()){
			Record rowData = iteCount.next();
			int surveyId = rowData.getInt("survey_id");
			long count = rowData.getBigDecimal("count").longValue();
			countMap.put(surveyId, count);
		}
		
		Iterator<Record> iteOptions = options.iterator();
		while(iteOptions.hasNext()){
			Record rowData = iteOptions.next();
			int surveyId = rowData.getInt("survey_id");
			long optionCount = rowData.getLong("option_count");
			long count = countMap.get(surveyId);
			double precent = optionCount*1.0/count;
			int temp = (int)Math.round(precent * 10000);
			precent = (double)temp / 100.00;
			rowData.set("precent", precent+"%");
		}
		renderJson(options);
	}
	
	public void settingIndex(){
		render("application/survey/index.jsp");
	}
	
	public void listQuestions(){
		List<Record> questions = Db.find("select id,question from bp_survey where shop_id=? and delete_date is null ", new Object[]{getPara("shopId")});
		renderJson(questions);
	}
	public void delete(){
		Db.update("update bp_survey set delete_date=now() where id=? ", new Object[]{getPara("id")});
		renderNull();
	}
	public void update(){
		if(StringUtils.isNotBlank(getPara("id"))){
			Record question = Db.findFirst("select id,question,type from bp_survey where id=? ", new Object[]{getPara("id")});
			if(null != question){
				setAttr("id", question.get("id"));
				setAttr("question", question.get("question"));
				setAttr("type", question.get("type"));
				
				List<Record> options = Db.find("select id,`option` from bp_survey_option where survey_id=? ", new Object[]{getPara("id")});
				setAttr("options", options);
			}
		}
		render("application/survey/edit.jsp");
	}
	public void save(){
		renderJsonResult(Db.tx(new IAtom(){public boolean run() throws SQLException {
			String thisId = getPara("id");
			int changeRow = 0;
			if(StringUtils.isBlank(getPara("id"))){
				changeRow = Db.update("insert into bp_survey(shop_id,question,type,create_date) values(?,?,?,now())", new Object[]{getPara("shopId"),getPara("question"),getPara("type")});
				if(changeRow>0){
					Record rec = Db.findFirst("select max(id) id from bp_survey where shop_id=? ",new Object[]{getPara("shopId")});
					System.err.println(rec.getInt("id"));
					thisId = rec.getInt("id")+"";
				}
			}else{
				changeRow = Db.update("update bp_survey set question=?,type=? where id=?", new Object[]{getPara("question"),getPara("type"),getPara("id")});
			}
			if(changeRow ==0){
				return false;
			}
			
			String[] addOptions = getParaValues("addOptions");
			if(null != addOptions && addOptions.length>0){
				String[][] addOptionsParams = new String[addOptions.length][2];
				for(int i=0;i<addOptions.length;i++){
					addOptionsParams[i][0] = thisId;
					addOptionsParams[i][1] = addOptions[i];
				}
				int[] rows1 = DbExt.batch("insert into bp_survey_option(survey_id,`option`,create_date) values(?,?,now())", addOptionsParams);
				for(int i=0;i<rows1.length;i++){
					if(rows1[i] < 1){
						return false;
					}
				}
			}
			
			String[] deleteOptions = getParaValues("deleteOptions");
			if(null != deleteOptions && deleteOptions.length > 0){
				String[][] deleteOptionsParams = new String[deleteOptions.length][1];
				for(int i=0;i<deleteOptions.length;i++){
					deleteOptionsParams[i][0] = deleteOptions[i];
				}
				int[] rows2 = DbExt.batch("delete from bp_survey_option where id=? ", deleteOptionsParams);
				for(int i=0;i<rows2.length;i++){
					if(rows2[i] < 1){
						return false;
					}
				}
			}
			
			String[] editOptions = getParaValues("editOptions");
			if(null != editOptions && editOptions.length > 0){
				String[][] editOptionsParams = new String[editOptions.length][2];
				for(int i=0;i<editOptions.length;i++){
					String[] info = editOptions[i].split(":");
					editOptionsParams[i][0] = info[1];
					editOptionsParams[i][1] = info[0];
				}
				int[] rows3 = DbExt.batch("update bp_survey_option set `option`=? where id=? ", editOptionsParams);
				for(int i=0;i<rows3.length;i++){
					if(rows3[i] < 1){
						return false;
					}
				}
			}
			return true;
		}}));
	}
}
