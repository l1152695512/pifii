package com.yf.base.actions.sys.dictionary;

import java.util.Dictionary;

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysDictionary;
import com.yf.base.service.SysDictionaryService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysDictionary, String> {
	
	private SysDictionaryService sysDictionaryService;
	
	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}

	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}
	private String typeName;
	private String keyName;
	

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}


	@Override
	protected IService<SysDictionary, String> _getService() {
		return this.sysDictionaryService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(dateFormat).excludeForeignObject(Dictionary.class);
	}
	@Override
	public  PageList getPageList(IService<SysDictionary, String> service){
		int currentPage = start/limit + 1;
		
		DetachedCriteria  dc=service.createCriteria();
		if(!StringUtils.isBlank(this.typeName)){
			dc.add(Restrictions.like("typeName", this.typeName,MatchMode.ANYWHERE));	
		}
		if(!StringUtils.isBlank(this.keyName)){
			dc.add(Restrictions.like("keyName", this.keyName,MatchMode.ANYWHERE));
			
		}
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));	
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);		
	}


}
