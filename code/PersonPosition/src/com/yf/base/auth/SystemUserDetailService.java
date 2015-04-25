package com.yf.base.auth;

import java.util.Date;
import java.util.List;

//import com.westwind.hr.db.vo.SystemUsertbl;
//import com.westwind.service.DaoHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.yf.base.db.handler.DaoHandler;
import com.yf.base.db.vo.SysUser;


public class SystemUserDetailService implements UserDetailsService,
		InitializingBean {

	private DaoHandler daoHandler;
	
	public void setDaoHandler(DaoHandler daoHandler) {
		this.daoHandler = daoHandler;
	}

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		//List userList = this.daoHandler.getUserDAO().findByAccountName(userName);
		
		List userList = this.daoHandler.getSysUserDAO().findByProperty("accountName", userName);
		
		if(userList.isEmpty())throw new UsernameNotFoundException("user not found");
		SysUser userVo = (SysUser) userList.get(0);
		userVo.setLastLogin(new Date());
		daoHandler.getSysUserDAO().update(userVo);
		return new SystemUser(userVo);
	}

	public void afterPropertiesSet() throws Exception {
		 Assert.notNull(this.daoHandler);
	}

}
