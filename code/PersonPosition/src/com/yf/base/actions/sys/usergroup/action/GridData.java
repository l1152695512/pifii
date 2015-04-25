package com.yf.base.actions.sys.usergroup.action;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysUsergroupAction;
import com.yf.base.service.SysUsergroupActionService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysUsergroupAction, String> {

	private SysUsergroupActionService sysUsergroupActionService;
	private String sugid;
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SysUsergroupActionService getSysUsergroupActionService() {
		return sysUsergroupActionService;
	}

	public void setSysUsergroupActionService(
			SysUsergroupActionService sysUsergroupActionService) {
		this.sysUsergroupActionService = sysUsergroupActionService;
	}


	@Override
	protected IService<SysUsergroupAction, String> _getService() {
		return this.sysUsergroupActionService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysUsergroupAction.class);
	}

	@Override
	public  PageList getPageList(IService<SysUsergroupAction, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		dc.createAlias("sysUsergroup", "sysUsergroup");
		dc.add(Restrictions.eq("sysUsergroup.sugid", this.sugid));	
		if(!StringUtils.isBlank(this.name)){
			dc.createAlias("sysAction", "sysAction");
			dc.add(Restrictions.like("sysAction.name", this.name,MatchMode.ANYWHERE));	
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}
	
	public String getSugid() {
		return sugid;
	}

	public void setSugid(String sugid) {
		this.sugid = sugid;
	}

	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
		SysUsergroupAction c = (SysUsergroupAction) rowObject;
	    json.put("sysActionName",c.getSysAction()==null?"":c.getSysAction().getName());	    
	    
	}


}
