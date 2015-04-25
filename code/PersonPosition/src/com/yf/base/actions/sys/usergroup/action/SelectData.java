package com.yf.base.actions.sys.usergroup.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysAction;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupAction;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysActionService;
import com.yf.base.service.SysUsergroupService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class SelectData extends BaseGridDataResultAction<SysAction, String> {

	private SysActionService sysActionService;
	private SysUsergroupService sysUsergroupService;
	private String sugid;
	
	private String name;


	public String getSugid() {
		return sugid;
	}

	public void setSugid(String sugid) {
		this.sugid = sugid;
	}

	@Override
	protected IService<SysAction, String> _getService() {
		return this.sysActionService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysAction.class);
	}

	@Override
	public  PageList getPageList(IService<SysAction, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}		
		SysUsergroup sysUsergroup=this.sysUsergroupService.findById(sugid);
		List <SysUsergroupAction> tempList=sysUsergroup.getSysUsergroupActions();
		if(tempList!=null&&tempList.size()>0){
			List sysActionList=new ArrayList();
			for (SysUsergroupAction temp : tempList) {
				sysActionList.add(temp.getSysAction().getSaid());	
			}
			dc.add(Restrictions.not(Restrictions.in("said", sysActionList)));
		}
		if(!StringUtils.isBlank(this.name)){
			dc.add(Restrictions.like("name", this.name,MatchMode.ANYWHERE));	
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}
	
	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
  
	    
	}


	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}

	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}

	public SysActionService getSysActionService() {
		return sysActionService;
	}

	public void setSysActionService(SysActionService sysActionService) {
		this.sysActionService = sysActionService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
