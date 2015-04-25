package com.yf.base.actions.setting.usergroup;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.service.SysUsergroupService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysUsergroup, String> {

	private SysUsergroupService sysUsergroupService;
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}

	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}

	@Override
	protected IService<SysUsergroup, String> _getService() {
		return this.sysUsergroupService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysUsergroup.class);
	}
	

	protected void processRowJson(JSONObject json, Object rowObject) {
		SysUsergroup  sysUsergroup=(SysUsergroup)rowObject;
		json.put("companyName", sysUsergroup.getYwCompany()==null?"":sysUsergroup.getYwCompany().getCname());

	}


	@Override
	public  PageList getPageList(IService<SysUsergroup, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(!StringUtils.isBlank(this.name)){
			dc.add(Restrictions.like("name", this.name,MatchMode.ANYWHERE));	
		}

		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}

		dc.add(Restrictions.or(Restrictions.isNull("deleted"), Restrictions.eq("deleted", false)));	//软删除
		
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}

}
