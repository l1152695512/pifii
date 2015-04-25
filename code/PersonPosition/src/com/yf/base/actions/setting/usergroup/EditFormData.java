package com.yf.base.actions.setting.usergroup;

import net.sf.json.JSONObject;

import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.service.SysUsergroupService;
import com.yf.constants.SystemConstants;
import com.yf.ext.base.BaseAction;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {


	private SysUsergroupService sysUsergroupService;
	private String sugid;
	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}
	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}

	public String getSugid() {
		return sugid;
	}
	public void setSugid(String sugid) {
		this.sugid = sugid;
	}
	@Override
	public String execute() throws Exception {
		SysUsergroup sysUsergroup = this.sysUsergroupService.findById(this.sugid);
		if (sysUsergroup == null){
			this.msg="该用户组不存在!";
			return "formData";
		}
			
		JSONObject record = JsonUtils.getJsonHelper().setDateFormat(
				SystemConstants.STANDARD_DATE_TIME_FORMAT)
				.excludeForeignObject(SysUsergroup.class).toJSONObject(sysUsergroup);
		record.put("cid", sysUsergroup.getYwCompany()==null?"":sysUsergroup.getYwCompany().getCid());
		
		jsonString = record.toString();
		return "formData";
	}

}
