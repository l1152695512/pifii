package com.yf.base.actions.sys.usergroup.user;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysUsergroupUserService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysUsergroupUser, String> {

	private SysUsergroupUserService sysUsergroupUserService;
	private String sugid;
	private String accountName;
	private String userName;
	


	@Override
	protected IService<SysUsergroupUser, String> _getService() {
		return this.sysUsergroupUserService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysUsergroupUser.class);
	}

	@Override
	public  PageList getPageList(IService<SysUsergroupUser, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		dc.createAlias("sysUser", "sysUser");
		if(!StringUtils.isBlank(this.accountName)){
			dc.add(Restrictions.like("sysUser.accountName", this.accountName,MatchMode.ANYWHERE));	
		}
		if(!StringUtils.isBlank(this.userName)){
			dc.add(Restrictions.like("sysUser.userName", this.userName,MatchMode.ANYWHERE));	
		}

		
		dc.createAlias("sysUsergroup", "sysUsergroup");
		dc.add(Restrictions.eq("sysUsergroup.sugid", this.sugid));	
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
		SysUsergroupUser c = (SysUsergroupUser) rowObject;
	    json.put("accountName",c.getSysUser()==null?"":c.getSysUser().getAccountName());	    
	    json.put("userName",c.getSysUser()==null?"":c.getSysUser().getUserName());	  
	    
	}

	public SysUsergroupUserService getSysUsergroupUserService() {
		return sysUsergroupUserService;
	}

	public void setSysUsergroupUserService(
			SysUsergroupUserService sysUsergroupUserService) {
		this.sysUsergroupUserService = sysUsergroupUserService;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


}
