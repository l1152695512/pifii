package com.yf.base.actions.commons;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.YwCompany;
import com.yf.base.service.YwCompanyService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;

public class ComboCompany extends BaseAction implements ServletRequestAware  {
	

	private YwCompanyService  ywCompanyService;
	
	private String jsonString;
	private boolean onlySun;
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	@Override
	public String execute() throws Exception {
		TransactionalCallBack callBack;
		callBack=new TransactionalCallBack(){
			@Override
			public Object execute(IService arg0) {
				// TODO Auto-generated method stub
				executeDoing();
				return null;
			}
			
		};
		
		this.ywCompanyService.executeTransactional(callBack);

		return "comboData";

	}
	public void executeDoing(){
		DetachedCriteria dc;
		List<YwCompany> list= new ArrayList();
		JSONArray  arry=new JSONArray();
		JSONObject o=null;
		
		dc = this.ywCompanyService.createCriteria();
		dc.add(Restrictions.isNotNull("deptNo"));
		list = this.ywCompanyService.findByDetachedCriteria(dc);
		
		if(!list.isEmpty())
		{
			for(YwCompany yc:list)
			{
				o = new JSONObject();
				o.put("name", yc.getCname());
				o.put("cid", yc.getCid());
				arry.add(o);
			}
		}
		
//	    String companyCid=this.queryUserLevelAndCompanyCId();
//	    if(companyCid.equals("null")){
//		dc=this.ywCompanyService.createCriteria();
//		list=this.ywCompanyService.findByDetachedCriteria(dc);
//	    }else if(!companyCid.equals("")){
//	    	
//	    	dc=this.ywCompanyService.createCriteria();
//			dc.add(Restrictions.eq("cid", companyCid));
//			list=this.ywCompanyService.findByDetachedCriteria(dc);
//	    }
//		//只求分公司不包括二级单位
//		//Debug.println("isOnlySun===="+onlySun);
//		if(this.onlySun){
//			for (YwCompany ywCompany : list) {
//				if(ywCompany.getCname().indexOf("分公司")>0){
//					o=new JSONObject();
//		    		o.put("name" ,ywCompany.getCname());
//		    		o.put("cid", ywCompany.getCid());
//		    		arry.add(o);
//				}
//			}
//			
//		}else{
//
//			o=new JSONObject();
//			o.put("name" ,"省公司");
//			o.put("cid", "");
//			arry.add(o);	
//			for (YwCompany ywCompany : list) {
//				o=new JSONObject();
//	    		o.put("name" ,ywCompany.getCname());
//	    		o.put("cid", ywCompany.getCid());
//	    		arry.add(o);	
//			}
//		}
		this.jsonString = arry.toString();
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}
	public YwCompanyService getYwCompanyService() {
		return ywCompanyService;
	}
	public void setYwCompanyService(YwCompanyService ywCompanyService) {
		this.ywCompanyService = ywCompanyService;
	}
	public boolean isOnlySun() {
		return onlySun;
	}
	public void setOnlySun(boolean onlySun) {
		this.onlySun = onlySun;
	}


}
