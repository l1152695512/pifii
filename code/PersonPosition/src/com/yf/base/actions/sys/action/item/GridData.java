package com.yf.base.actions.sys.action.item;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysActionItem;
import com.yf.base.service.SysActionItemService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.Debug;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysActionItem, String> {

	private SysActionItemService sysActionItemService;
	private String said;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected IService<SysActionItem, String> _getService() {
		return this.sysActionItemService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysActionItem.class);
	}

	@Override
	public  PageList getPageList(IService<SysActionItem, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		Debug.println("said-------------"+said);
		dc.createAlias("sysAction", "sysAction");
		dc.add(Restrictions.eq("sysAction.said", this.said));	
		if(!StringUtils.isBlank(this.name)){
			dc.add(Restrictions.like("name", this.name,MatchMode.ANYWHERE));	
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}

	public String getSaid() {
		return said;
	}
	public void setSaid(String said) {
		this.said = said;
	}
	public SysActionItemService getSysActionItemService() {
		return sysActionItemService;
	}

	public void setSysActionItemService(SysActionItemService sysActionItemService) {
		this.sysActionItemService = sysActionItemService;
	}

}
