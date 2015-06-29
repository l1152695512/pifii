package com.yf.base.actions.sys.user;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysUser;
import com.yf.base.service.SysUserService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysUser, String> {

	private SysUserService sysUserService;
	private String accountName;
	private String userName;


	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@Override
	protected IService<SysUser, String> _getService() {
		return this.sysUserService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(dateFormat)
				.excludeForeignObject(SysUser.class);
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
	@Override
	public  PageList getPageList(IService<SysUser, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(!StringUtils.isBlank(this.accountName)){
			dc.add(Restrictions.like("accountName", this.accountName,MatchMode.ANYWHERE));	
		}
		if(!StringUtils.isBlank(this.userName)){
			dc.add(Restrictions.like("userName", this.userName,MatchMode.ANYWHERE));
			
		}
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}

}
