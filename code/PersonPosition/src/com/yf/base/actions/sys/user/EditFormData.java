package com.yf.base.actions.sys.user;

import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysUser;
import com.yf.base.service.SysUserService;
import com.yf.constants.SystemConstants;
import com.yf.ext.base.BaseAction;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {

	private SysUserService sysUserService;
	private String userId;
	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String execute() throws Exception {
		SysUser user = this.sysUserService.findById(this.userId);
		DetachedCriteria dc = this.sysUserService.createCriteria().add(Restrictions.eq("accountName", "40")).add(Restrictions.isNull("email"));
		DetachedCriteria dc2 = this.sysUserService.createCriteria().add(Restrictions.eq("accountName", "40")).add(Restrictions.isNull("email"));
//		Debug.println(dc.equals(dc2));
//		Debug.println(dc.toString().equals(dc2.toString()));
//		Debug.println(dc);
//		Debug.println(dc2);
		this.sysUserService.findByDetachedCriteria(dc2);
		this.sysUserService.findByDetachedCriteria(dc);
		if (user == null){
			this.msg="该用户不存在!";
			return "formData";
			
		}
			
		user.setPassword("");
		JSONObject record = JsonUtils.getJsonHelper().setDateFormat(
				SystemConstants.STANDARD_DATE_TIME_FORMAT)
				.excludeForeignObject(SysUser.class).toJSONObject(user);
		jsonString = record.toString();
		return "formData";
	}

}
