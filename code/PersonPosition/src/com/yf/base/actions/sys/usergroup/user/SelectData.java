package com.yf.base.actions.sys.usergroup.user;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysUserService;
import com.yf.base.service.SysUsergroupService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class SelectData extends BaseGridDataResultAction<SysUser, String> {

	private SysUserService sysUserService;
	private SysUsergroupService sysUsergroupService;
	private String accountName;
	private String userName;
	
	private String sugid;



	public String getSugid() {
		return sugid;
	}

	public void setSugid(String sugid) {
		this.sugid = sugid;
	}

	@Override
	protected IService<SysUser, String> _getService() {
		return this.sysUserService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysUser.class);
	}

	@Override
	public  PageList getPageList(IService<SysUser, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		SysUsergroup sysUsergroup=this.sysUsergroupService.findById(sugid);
		List <SysUsergroupUser> tempList=sysUsergroup.getSysUsergroupUsers();
		if(tempList!=null&&tempList.size()>0){
			List sysUserList=new ArrayList();
			for (SysUsergroupUser temp : tempList) {
				sysUserList.add(temp.getSysUser().getUserId());	
			}
			dc.add(Restrictions.not(Restrictions.in("userId", sysUserList)));
		}
		if(!StringUtils.isBlank(this.accountName)){
			dc.add(Restrictions.like("accountName", this.accountName,MatchMode.ANYWHERE));	
		}
		if(!StringUtils.isBlank(this.userName)){
			dc.add(Restrictions.like("userName", this.userName,MatchMode.ANYWHERE));	
		}

		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}
	
	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
		
	    
	}

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}

	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
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
