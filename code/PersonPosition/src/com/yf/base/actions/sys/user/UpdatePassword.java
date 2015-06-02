package com.yf.base.actions.sys.user;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.encoding.PasswordEncoder;

import com.opensymphony.xwork2.ModelDriven;
import com.yf.base.auth.SystemUser;
import com.yf.base.db.vo.SysUser;
import com.yf.base.service.SysUserService;
import com.yf.ext.base.UniqueIdActionSupport;
import com.yf.util.Debug;

public class UpdatePassword extends UniqueIdActionSupport implements ModelDriven<SysUser> {
	private String jsonString;
	private String msg;
	
	private SysUserService sysUserService;
	public SysUserService getSysUserService() {
		return sysUserService;
	}
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	private SysUser user = new SysUser();
	
	private PasswordEncoder passwordEncoder;
	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	private String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	@Override
	public String execute() throws Exception {
		try{
			SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String uid = sysuser.getUserId();
			SysUser tempUser = sysUserService.findById(uid);
			List authMenus = null;
			if(tempUser==null){
				return "failure";
			}else{
				tempUser.setUpdateTime(new Date());

				if(StringUtils.isNotEmpty(user.getPassword())){//如果账户密码是空，则需要保留原密码
					Debug.println("password=333=="+user.getPassword());
					tempUser.setPassword( this.passwordEncoder.encodePassword(user.getPassword(), tempUser.getAccountName()));
					this.sysUserService.update(tempUser);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
		return "success";
	}
	@Override
	public SysUser getModel() {
		return user;
	}
	public SysUser getUser() {
		return user;
	}
	public void setUser(SysUser user) {
		this.user = user;
	}
}
