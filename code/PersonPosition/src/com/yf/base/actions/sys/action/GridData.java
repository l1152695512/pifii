package com.yf.base.actions.sys.action;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysAction;
import com.yf.base.service.SysActionService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysAction, String> {

	private SysActionService sysActionService;
	private String name;



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
		if(!StringUtils.isBlank(this.name)){
			dc.add(Restrictions.like("name", this.name,MatchMode.ANYWHERE));	
		}
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}

}
