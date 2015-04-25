package com.yf.ext.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.auth.SystemUser;
import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysUserService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.data.base.SortBy;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;
import com.yf.util.JsonUtils.JsonHelper;

public abstract class BaseGridDataResultAction<T, PK extends Serializable>
		extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static Log logger = LogFactory
			.getLog(BaseGridDataResultAction.class);

	protected String msg;

	protected int start;

	protected int limit = 20;

	protected String sort;

	protected String dir = "DESC";

	protected String jsonString;
	
	protected SysUser loginUser;

	protected String dateFormat = SystemConstants.STANDARD_DATE_TIME_FORMAT;
	
	private SysUserService  sysUserService;

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
		// 默认显示20条数据，防止未提交limit，出现by zero异常！
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
	
		return "data";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
    
	//得到登陆用户
	public SysUser getLoginUser() {	
		SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String uid = sysuser.getUserId();
		loginUser= sysUserService.findById(uid);
		return loginUser;
	}
	//得到登陆用户所在用户组
	public List<SysUsergroup> getSysUsergroup(){
		return (List)this.sysUserService.executeTransactional(new TransactionalCallBack() {	
			@Override
			public Object execute(IService arg0) {
				List <SysUsergroup>resultList=new ArrayList();
				List <SysUsergroupUser> temp=getLoginUser().getSysUsergroupUsers();
				for (SysUsergroupUser sysUsergroupUser : temp) {
					resultList.add(sysUsergroupUser.getSysUsergroup());
				}
				return resultList;
				
			}
		});	

	}
	
	public List getSysUsergroupNames(){
		return (List)this.sysUserService.executeTransactional(new TransactionalCallBack() {	
			@Override
			public Object execute(IService arg0) {		
				List list=new ArrayList();
			for (SysUsergroup sysUsergroup : getSysUsergroup()) {
				list.add(sysUsergroup.getName());
				
			}
			return list;}
		});	
		
	}

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
}
