package com.yf.base.actions.sys.user;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.providers.encoding.PasswordEncoder;

import com.yf.base.db.vo.SysUser;
import com.yf.base.service.SysUserService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class SaveForm extends ActionSupport implements ModelDriven<SysUser> {
	
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


	private String msg;
	

	@Override
	public String execute() throws Exception {
		try{
			if(StringUtils.isNotBlank(user.getUserId())){
		
				SysUser  tempUser=this.sysUserService.findById(user.getUserId());
				if(tempUser == null){
					this.msg="保存失败,ID为" + user.getUserId() + "用户不存在";
					return "failure";
				}
				if(StringUtils.isNotEmpty(user.getPassword())){//如果账户密码是空，则需要保留原密码
					user.setPassword( this.passwordEncoder.encodePassword(user.getPassword(), user.getAccountName()));
				}else{
					user.setPassword(tempUser.getPassword());
				}
//				BeanUtils.copyProperties(tempUser, this);
				//不在提交的form里面包含的参数，要自己设置回去。因为update的对象是新的对象，而不是从数据库里面加载出来的。
				user.setUpdateTime(new Date());
				user.setDisabled(false);
				user.setCreateTime(tempUser.getCreateTime());
				this.sysUserService.update(user);
			}else {

				if(this.sysUserService.findByProperty("accountName", user.getAccountName()).size() != 0){
					this.msg="保存失败,账号重复";
					return "failure";
				}
				user.setPassword(this.passwordEncoder.encodePassword(user.getPassword(), user.getAccountName()));
				//BeanUtils.copyProperties(tempUser, this);
				user.setCreateTime(new Date());
				user.setUpdateTime(new Date());
				user.setDisabled(false);
				this.sysUserService.add(user);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return super.execute();
	}
	@Override
	public SysUser getModel() {
		return user;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
