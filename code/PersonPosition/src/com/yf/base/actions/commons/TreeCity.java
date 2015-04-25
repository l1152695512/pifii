package com.yf.base.actions.commons;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.YwRegionService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;

public class TreeCity extends ActionSupport {

	private YwRegionService ywRegionService;
	private String jsonString;
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public String execute() throws Exception {
		TransactionalCallBack callBack = new TransactionalCallBack() {
			@Override
			public Object execute(IService service) {
				DetachedCriteria  dc=ywRegionService.createCriteria();
				dc.add(Restrictions.isNull("ywRegion"));
				dc.add(Restrictions.or(Restrictions.isNull("deleted"), Restrictions.eq("deleted", false)));	//软删除	
				List list =  ywRegionService.findByDetachedCriteria(dc);
				return buildTree(list);
			}
		};
		Object result = this.ywRegionService.executeTransactional(callBack);
		this.jsonString = result.toString();		
		return "data";
	}
	
	private JSONArray buildTree(List list){

		JSONArray array = new JSONArray();	
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			YwRegion ywRegion = (YwRegion) iterator.next();
			if(ywRegion.getDeleted()==null||ywRegion.getDeleted()==false){
				JSONObject object = new JSONObject();	
				object.put("text", ywRegion.getName());
				object.put("id", ywRegion.getRid());
				object.put("leaf", true);
				array.add(object);
			}
		}
		return array;
	}
	public YwRegionService getYwRegionService() {
		return ywRegionService;
	}
	public void setYwRegionService(YwRegionService ywRegionService) {
		this.ywRegionService = ywRegionService;
	}
	
}
