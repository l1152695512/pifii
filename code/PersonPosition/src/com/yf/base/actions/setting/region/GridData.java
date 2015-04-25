package com.yf.base.actions.setting.region;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.YwRegionService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<YwRegion, String> {

	private YwRegionService ywRegionService;
	private String name;
	private String pid;
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Override
	protected IService<YwRegion, String> _getService() {
		return this.ywRegionService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(YwRegion.class);
	}
	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
		YwRegion  b=(YwRegion)rowObject;
		json.put("parent",b.getYwRegion()==null?"无父区域":b.getYwRegion().getName());
		
	}

	@Override
	public  PageList getPageList(IService<YwRegion, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(!StringUtils.isBlank(this.name)){
			dc.add(Restrictions.like("name", this.name,MatchMode.ANYWHERE));	
		}
		if(!StringUtils.isBlank(this.pid)&&!this.pid.equals("rootId")){
			dc.createAlias("ywRegion", "ywRegion");
			dc.add(Restrictions.eq("ywRegion.rid", pid));
		}
		
		dc.add(Restrictions.or(Restrictions.isNull("deleted"), Restrictions.eq("deleted", false)));	//软删除
		
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}

	

	public YwRegionService getYwRegionService() {
		return ywRegionService;
	}

	public void setYwRegionService(YwRegionService ywRegionService) {
		this.ywRegionService = ywRegionService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
