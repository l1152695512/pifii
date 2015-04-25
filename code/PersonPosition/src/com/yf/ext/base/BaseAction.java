package com.yf.ext.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.auth.SystemUser;
import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysUserService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;

public class BaseAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7014149503508930061L;
	protected String jsonString;
	protected String msg;
	private SysUserService sysUserService;
	protected SysUser loginUser;
	
	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public SysUser getLoginUser() {

		SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String uid = sysuser.getUserId();
		loginUser = sysUserService.findById(uid);
		return loginUser;
	}

	// 得到登陆用户所在用户组
	public List<SysUsergroup> getSysUsergroup() {
		return (List) this.sysUserService
				.executeTransactional(new TransactionalCallBack() {
					@Override
					public Object execute(IService arg0) {
						return getSysUsergroupDoing();
					}
				});

	}

	public List<SysUsergroup> getSysUsergroupDoing() {

		List<SysUsergroup> resultList = new ArrayList();
		List<SysUsergroupUser> temp = getLoginUser().getSysUsergroupUsers();
		for (SysUsergroupUser sysUsergroupUser : temp) {
			resultList.add(sysUsergroupUser.getSysUsergroup());
		}
		return resultList;

	}

	public List getSysUsergroupNames() {
		return (List) this.sysUserService
				.executeTransactional(new TransactionalCallBack() {
					@Override
					public Object execute(IService arg0) {
						return getSysUsergroupNamesDoing();

					}
				});

	}

	public List getSysUsergroupNamesDoing() {
		
		List list = new ArrayList();
		for (SysUsergroup sysUsergroup : getSysUsergroup()) {
			list.add(sysUsergroup.getName());
		}
		return list;

	}

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

}
