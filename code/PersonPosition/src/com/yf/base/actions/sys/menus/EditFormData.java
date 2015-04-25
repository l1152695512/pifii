package com.yf.base.actions.sys.menus;

import net.sf.json.JSONObject;

import com.yf.base.db.vo.SysMenu;
import com.yf.base.service.SysMenuService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {


	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}



	private SysMenuService sysMenuService;
	
	private String beanId;


	
	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}



	private String jsonString;

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public String execute() throws Exception {
		this.sysMenuService.executeTransactional(new TransactionalCallBack() {
			
			@Override
			public Object execute(IService arg0) {
				//Debug.println("beanId"+beanId);
				SysMenu menu = sysMenuService.findById(beanId);
				if (menu == null){
						msg="该数据字典不存在!";
						return null;
				
				}
		
				JSONObject record = JsonUtils.getJsonHelper().setDateFormat(
						SystemConstants.STANDARD_DATE_TIME_FORMAT).setAllBooleanToString()
						.excludeForeignObject(SysMenu.class).toJSONObject(menu);
				SysMenu parent = menu.getSysMenu();
				if(parent != null){
					record.put("parentId", parent.getSystemMenuId());
				}else{
					record.put("parentId", 0);
				}
				jsonString = record.toString();
				return null;
			}
		});
		
		return "formData";
	}

}
