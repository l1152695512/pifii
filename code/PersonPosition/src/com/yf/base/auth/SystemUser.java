package com.yf.base.auth;

//import com.westwind.hr.db.vo.SystemUsertbl;
import java.util.Date;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

import com.yf.constants.RoleConstants;
import com.yf.base.db.vo.SysUser;


public class SystemUser implements UserDetails {

	private GrantedAuthority[] authorities ={new GrantedAuthorityImpl (RoleConstants.ROLE_SYS_ADMIN),
			new GrantedAuthorityImpl(RoleConstants.ROLE_USER)};
	
	private SysUser sysUser;

	public SystemUser(SysUser vo) {
		super();
		this.sysUser = vo;
	}

	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}

	public String getPassword() {
		return this.sysUser.getPassword();
	}

	public String getUsername() {
		return this.sysUser.getAccountName();
	}

	public String getUserId() {
		
		return this.sysUser.getUserId();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return sysUser.getDisabled() == null || !sysUser.getDisabled(); // 为了向后兼容，为Null也表示true
	}
	
	public Date getLastLoginTime() {
		return new Date();
	}
	
	public String getRealUserName(){
		return this.sysUser.getUserName();
	}

}
