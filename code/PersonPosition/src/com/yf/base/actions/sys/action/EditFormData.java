package com.yf.base.actions.sys.action;

import net.sf.json.JSONObject;

import com.yf.base.db.vo.SysAction;
import com.yf.base.service.SysActionService;
import com.yf.constants.SystemConstants;
import com.yf.ext.base.BaseAction;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {

	public SysActionService getSysActionService() {
		return sysActionService;
	}
	public void setSysActionService(SysActionService sysActionService) {
		this.sysActionService = sysActionService;
	}



	public String getSaid() {
		return said;
	}
	public void setSaid(String said) {
		this.said = said;
	}



	private SysActionService sysActionService;
	private String said;



	@Override
	public String execute() throws Exception {
		SysAction sysAction = this.sysActionService.findById(this.said);
		if (sysAction == null){
			this.msg="该权限不存在!";
			return "formData";
		}
			
		JSONObject record = JsonUtils.getJsonHelper().setDateFormat(
				SystemConstants.STANDARD_DATE_TIME_FORMAT)
				.excludeForeignObject(SysAction.class).toJSONObject(sysAction);
		jsonString = record.toString();
		return "formData";
	}

}
