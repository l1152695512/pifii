package com.yf.base.actions.sys.user.usergroup;

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

public class SelectData extends BaseGridDataResultAction<SysUsergroup, String> {

	
	private SysUsergroupService sysUsergroupService;

	private SysUserService sysUserService;
	private String userId;


	@Override
	protected IService<SysUsergroup, String> _getService() {
		return this.sysUsergroupService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysUsergroup.class);
	}

	@Override
	public  PageList getPageList(IService<SysUsergroup, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		SysUser sysUser=this.sysUserService.findById(userId);
		List <SysUsergroupUser> tempList=sysUser.getSysUsergroupUsers();
		if(tempList!=null&&tempList.size()>0){
			List sysUserList=new ArrayList();
			for (SysUsergroupUser temp : tempList) {
				sysUserList.add(temp.getSysUsergroup().getSugid());	
			}
			dc.add(Restrictions.not(Restrictions.in("sugid", sysUserList)));
		}
	

		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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



}
