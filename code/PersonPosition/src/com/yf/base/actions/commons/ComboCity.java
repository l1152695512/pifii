package com.yf.base.actions.commons;


import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionContext;
import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.db.vo.YwCompany;
import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.SysUserService;
import com.yf.base.service.YwRegionService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;

public class ComboCity extends BaseAction  {
	
	private YwRegionService ywRegionService;
	private SysUserService sysUserService;
	
	public SysUserService getSysUserService() {
		return sysUserService;
	}
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
	
	private String jsonString;
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	@Override
	public String execute() throws Exception {
		try{
			TransactionalCallBack callBack=new TransactionalCallBack(){
				@Override
				public Object execute(IService arg0) {
					// TODO Auto-generated method stub
					executeDoing();
					return null;
				}
				
			};
			this.ywRegionService.executeTransactional(callBack);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		

		return "comboData";
	}
	public void executeDoing(){
	    String loginUserId=(String)ActionContext.getContext().getSession().get("loginUserId");
	   
	    SysUser sysUser=this.sysUserService.findById(loginUserId);
	   
	    
	    List <SysUsergroupUser>temp=sysUser.getSysUsergroupUsers();
	    SysUsergroup  sysUsergroup=null;
	    if(temp.size()>0){
	    	   sysUsergroup=temp.get(0).getSysUsergroup();
	    }
	    List<String>usergroupString=new ArrayList();
	    for (SysUsergroupUser sysUsergroupUser : temp) {
	    	usergroupString.add(sysUsergroupUser.getSysUsergroup().getName());
		}
	    
		JSONArray  arry=new JSONArray();
		JSONObject o=null;
		DetachedCriteria dc=this.ywRegionService.createCriteria();
		List<YwRegion> list= new ArrayList();
		if(usergroupString.contains(("省公司用户组"))||usergroupString.contains(("管理员用户组"))||usergroupString.contains(("研究院用户组"))){
			//dc.add(Restrictions.isNull("ywRegion"));
			
			dc.createAlias("ywRegion", "ywRegion");
			dc.add(Restrictions.eq("ywRegion.rid", sysUsergroup.getYwCompany().getYwRegion().getRid()));
			dc.add(Restrictions.or(Restrictions.isNull("deleted"), Restrictions.eq("deleted", false)));	//软删除	
			list=this.ywRegionService.findByDetachedCriteria(dc);
			//Debug.println("list==="+list.size());
			for (YwRegion ywRegion : list) {
				o=new JSONObject();
	    		o.put("name" ,ywRegion.getName());
	    		o.put("cityId", ywRegion.getRid());
	    		arry.add(o);	
			}
		}
		else{
			
			YwCompany yw=sysUsergroup.getYwCompany();
			if(yw!=null&&yw.getYwRegion()!=null){
				YwRegion ywRegion=yw.getYwRegion();
				o=new JSONObject();
	    		o.put("name" ,ywRegion.getName());
	    		o.put("cityId", ywRegion.getRid());
	    		arry.add(o);		
			}
		}
		this.jsonString = arry.toString();
		
	}
	
	
	public YwRegionService getYwRegionService() {
		return ywRegionService;
	}
	public void setYwRegionService(YwRegionService ywRegionService) {
		this.ywRegionService = ywRegionService;
	}
	

	

}
