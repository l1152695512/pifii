package com.yf.servlet.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;

import com.yf.base.auth.SystemUser;
import com.yf.base.db.vo.SysActionItem;
import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupAction;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysActionItemService;
import com.yf.base.service.SysUserService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;
import com.yf.util.Debug;

public class CheckFilter implements Filter {
	private SysUserService sysUserService;
	private SysActionItemService sysActionItemService;
	private static Map powerActionMap;
	private static Map allPowerActionMap;
	

	public void destroy() {
		
	}
	
	//获取所有需求验证的action
	private void initAllAction(){
		this.allPowerActionMap = new HashMap();
		List<SysActionItem> actionItemList = sysActionItemService.getAll();
		for (SysActionItem sysActionItem : actionItemList) {
			 if(StringUtils.isNotEmpty(sysActionItem.getPath())){
				 this.allPowerActionMap.put(sysActionItem.getPath().trim(), "0");		
			 }
		}
	}
	//获取当前用户组有权限的action
	private void initAction(){
		this.powerActionMap=(Map)this.sysUserService.executeTransactional(new TransactionalCallBack(){
			@Override
			public Object execute(IService iservice) {
				SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String uid = sysuser.getUserId();
				SysUser user = sysUserService.findById(uid);
				List<SysUsergroupUser> sysUsergroupUserList = user.getSysUsergroupUsers();
				List<SysUsergroupAction> sysUsergroupActionList;
				List<SysActionItem> sysActionItemList;
				SysUsergroup sysUsergroup;// 用户组
				Map powerActionMap = new HashMap();
				for (SysUsergroupUser sysUsergroupUser : sysUsergroupUserList) {
					sysUsergroup = sysUsergroupUser.getSysUsergroup();
					sysUsergroupActionList = sysUsergroup.getSysUsergroupActions();
					for (SysUsergroupAction sysUsergroupAction : sysUsergroupActionList) {
						sysActionItemList = sysUsergroupAction.getSysAction().getSysActionItems();
						for (SysActionItem sysActionItem : sysActionItemList) {
							 if(StringUtils.isNotEmpty(sysActionItem.getPath())){
								 powerActionMap.put(sysActionItem.getPath().trim(), "1");		
							 }
						}
					}
				}
				return powerActionMap;
			}
		});
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		final String url = request.getServletPath();
		String[] commStr = { ".js", ".css", ".ico", ".jpg", ".gif", ".png" };
		for (int i = 0; i < commStr.length; i++) {
			if (url.endsWith(commStr[i])) {
				arg2.doFilter(request, response);//公共权限
				return;
			}
		}
		Debug.println("请求权限:----------" + url);
		String accountName = (String)request.getSession().getAttribute("accountName");
		if (!StringUtils.isBlank(accountName)) {
			if ("admin".equals(accountName)) {
				arg2.doFilter(request, response); // 管理员具有所有的权限
				return;
			}else{
				//Map allPowerActionMap = (HashMap) request.getSession().getAttribute(SystemConstants.SESSION_ALL_POWER_MAP);
				initAllAction();//获取所有需求验证的action
				if (this.allPowerActionMap != null) {
					if (this.allPowerActionMap.containsKey(url)) {
						//Map powerActionMap = (HashMap) request.getSession().getAttribute(SystemConstants.SESSION_POWER_MAP);
						initAction();//获取当前用户组的action
						if(this.powerActionMap != null){
							if (this.powerActionMap.containsKey(url)) {
								arg2.doFilter(request, response);//通过验证
								return;
							}else{//用户组配置了权限，但没该权限
								Debug.println("你没有此权限,请与管理员联系!" + url);
								response.getWriter().write("<script>alert('你没有此权限,请与管理员联系!');</script>");
								return;
							}
						}else{//用户组没配置权限
							Debug.println("你没有此权限,请与管理员联系!" + url);
							response.getWriter().write("<script>alert('你没有此权限,请与管理员联系!');</script>");
							return;
						}
					}else{// 公共action
						arg2.doFilter(request, response);
						return;
					}
				}else{//没有需要验证的action
					arg2.doFilter(request, response);
					return;
				}
			}
		}else{// 公共action
			arg2.doFilter(request, response); 
			return;
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}
	public SysUserService getSysUserService() {
		return sysUserService;
	}
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
	public SysActionItemService getSysActionItemService() {
		return sysActionItemService;
	}
	public void setSysActionItemService(SysActionItemService sysActionItemService) {
		this.sysActionItemService = sysActionItemService;
	}
}
