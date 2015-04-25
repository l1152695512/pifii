package com.yf.base.actions.setting.company;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.YwCompany;
import com.yf.base.service.YwCompanyService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<YwCompany, String> {

	private YwCompanyService ywCompanyService;

	private String cname;
	
	@Override
	protected IService<YwCompany, String> _getService() {
		return this.ywCompanyService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(YwCompany.class);
	}
	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
		YwCompany  b=(YwCompany)rowObject;
		json.put("ywRegion",b.getYwRegion()==null?"": b.getYwRegion().getName());
		json.put("typeName", b.getCategory().equals("1")==true?"分公司":"二级单位");
	}

	@Override
	public  PageList getPageList(IService<YwCompany, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(!StringUtils.isBlank(this.cname)){
			dc.add(Restrictions.like("cname", this.cname,MatchMode.ANYWHERE));	
		}
		dc.add(Restrictions.or(Restrictions.isNull("deleted"), Restrictions.eq("deleted", false)));	//软删除
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}

	public YwCompanyService getYwCompanyService() {
		return ywCompanyService;
	}

	public void setYwCompanyService(YwCompanyService ywCompanyService) {
		this.ywCompanyService = ywCompanyService;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}


}
