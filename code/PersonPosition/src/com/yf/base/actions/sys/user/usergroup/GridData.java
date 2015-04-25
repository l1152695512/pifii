package com.yf.base.actions.sys.user.usergroup;

import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
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
	private String userId;


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
		dc.add(Restrictions.eq("sysUser.userId", this.userId));	
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}
	
	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
		SysUsergroupUser c = (SysUsergroupUser) rowObject;
	    json.put("name",c.getSysUsergroup()==null?"":c.getSysUsergroup().getName());	    
	
	    
	}

	public SysUsergroupUserService getSysUsergroupUserService() {
		return sysUsergroupUserService;
	}

	public void setSysUsergroupUserService(
			SysUsergroupUserService sysUsergroupUserService) {
		this.sysUsergroupUserService = sysUsergroupUserService;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}




}
