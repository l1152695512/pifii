package com.yf.base.actions.sys.action.item;

import net.sf.json.JSONObject;

import com.yf.base.db.vo.SysActionItem;
import com.yf.base.service.SysActionItemService;
import com.yf.constants.SystemConstants;
import com.yf.ext.base.BaseAction;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {

	private SysActionItemService sysActionItemService;
	private String saiid;
	@Override
	public String execute() throws Exception {
		SysActionItem sysActionItem = this.sysActionItemService.findById(this.saiid);
		if (sysActionItem == null){
			this.msg="该权限Action不存在!";
			return "formData";
		}
			
		JSONObject record = JsonUtils.getJsonHelper().setDateFormat(
				SystemConstants.STANDARD_DATE_TIME_FORMAT)
				.excludeForeignObject(SysActionItem.class).toJSONObject(sysActionItem);
		jsonString = record.toString();
		return "formData";
	}
	public SysActionItemService getSysActionItemService() {
		return sysActionItemService;
	}
	public void setSysActionItemService(SysActionItemService sysActionItemService) {
		this.sysActionItemService = sysActionItemService;
	}
	public String getSaiid() {
		return saiid;
	}
	public void setSaiid(String saiid) {
		this.saiid = saiid;
	}


}
