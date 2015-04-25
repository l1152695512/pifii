package com.yf.ext.base;

import java.io.Serializable;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.data.base.PageList;
import com.yf.data.base.TransactionalCallBack;
import com.yf.constants.SystemConstants;
import com.yf.data.base.SortBy;
import com.yf.service.IService;
import com.yf.util.JsonUtils.JsonHelper;

public abstract class BaseGridDataAction<T, PK extends Serializable> extends
		ActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected static Log logger = LogFactory.getLog(BaseGridDataAction.class);

	protected int start;

	protected int limit;

	protected String sort;

	protected String dir="DESC";

	protected String jsonString;

	protected String dateFormat = SystemConstants.STANDARD_DATE_TIME_FORMAT;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String listJson) {
		this.jsonString = listJson;
	}

	protected PageList getPageList(IService<T, PK> service) {
		//默认显示20条数据，防止未提交limit，出现by zero异常！钟委2010-10-18添加
		if (limit <= 0) {
			limit = 20;
		}

		int currentPage = start / limit + 1;
		if (sort != null && dir != null) {
			return service.getByPageWithOrderBy(limit, currentPage,
					SystemConstants.SORT_ASC.equals(dir) ? SortBy.asc(sort)
							: SortBy.desc(sort));
		} else {
			return service.getByPage(limit, currentPage);
		}

	}

	/**
	 * 请传入注入的Service对象
	 *
	 * @return
	 */
	protected abstract IService<T, PK> _getService();

	protected abstract JsonHelper _getJsonHelper();

	protected JSONObject processPageListJson(JSONObject json, PageList list) {
		JSONArray array = (JSONArray) json.get("list");
		int index = 0;
		for (Iterator iter = list.getList().iterator(); iter.hasNext(); index++) {
			processRowJson(array.getJSONObject(index), iter.next());
		}
		return json;
	}

	protected void processRowJson(JSONObject json, Object rowObject) {

	}

	@Override
	public String execute() throws Exception {
		try{
			TransactionalCallBack callBack = new TransactionalCallBack() {

				@Override
				public Object execute(IService service) {
					PageList list = getPageList(service);
					JSONObject json = null;
					try {
						json = processPageListJson(_getJsonHelper().toJSONObject(
								list), list);
					} catch (Exception e) {
						if (logger.isWarnEnabled()) {
							logger.warn("Json Exception--"
									+ this.getClass().getName(), e);
						}
						e.printStackTrace();
					}
					return json.toString();

				}
			};
			this.jsonString = (String) _getService().executeTransactional(callBack);
			if (logger.isDebugEnabled()) {
				logger.debug("Json String : " + jsonString);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return SUCCESS;
	}
}
