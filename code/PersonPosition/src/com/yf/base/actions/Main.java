package com.yf.base.actions;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.sql.RowSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.security.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.auth.SystemUser;
import com.yf.base.db.vo.SysUser;
import com.yf.base.service.SysMenuService;
import com.yf.base.service.SysUserService;
import com.yf.tradecontrol.GlobalVar;
import com.yf.tradecontrol.JDomHandler;
import com.yf.util.CfgTools;
import com.yf.util.dbhelper.DBHelper;

public class Main extends ActionSupport {
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private JDomHandler domHandler = new JDomHandler();
	public static final String xmlpath = GlobalVar.WORKPATH + File.separator
			+ "config" + File.separator + "dsSystemConfig.xml";
	
	private String theme="20";
	//private String[] themes=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};
	private String[] themes=new String[]{"1","2","3","4"};
	
	private SysUserService  sysUserService;
	private SysMenuService sysMenuService;
	
	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	private String loginTime = null;
	
	private String userName = "";
	
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String[] getThemes() {
		return themes;
	}

	@Override
	public String execute() throws Exception {
		try{
			SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			
			loginTime = sdf.format(sysuser.getLastLoginTime());
			userName = sysuser.getRealUserName();
			SysUser sy= sysUserService.findById(sysuser.getUserId());
			sy.setLoginCount(sy.getLoginCount()==null?1:sy.getLoginCount()+1);
			this.sysUserService.update(sy);
	        ActionContext.getContext().getSession().put("loginUserId",sysuser==null?"":sysuser.getUserId());
	        ActionContext.getContext().getSession().put("accountName",sy==null?"":sy.getAccountName());
	        
	        domHandler.loadXmlByPath(xmlpath);
	        String ccflowIp = domHandler.getNodeValue("/ds-config/ccflow/enginehostname");//ccflow工作流服务器地址
	        String tcpIp = domHandler.getNodeValue("/ds-config/tcp/enginehostname");//tcp服务器地址
	        ActionContext.getContext().getSession().put("ccflowip",tcpIp);
	        ActionContext.getContext().getSession().put("tcp",tcpIp);
	        
	        StringBuffer sql = new StringBuffer("SELECT a.NAME,a.SUGID FROM sys_usergroup_tbl a,sys_usergroup_user_tbl b ");
	        sql.append("WHERE a.sugid = b.sugid AND (a.deleted<> 1 OR a.deleted IS NULL ) ");
	        sql.append("AND b.user_id='"+sy.getUserId()+"'");
	        RowSet rs = dbhelper.select(sql.toString());
	        String userGroupName = "";
	        String sugid = "";
	        boolean isAdmin = false;
	        while(rs.next()){
	        	if(!StringUtils.isBlank(userGroupName)){
	        		userGroupName += ",";
	        	}
	        	if(!StringUtils.isBlank(sugid)){
	        		sugid += ",";
	        	}
	        	userGroupName += rs.getString("NAME");
	        	sugid += rs.getString("SUGID");
	        }
	        String src [] = userGroupName.split(",");
	        if(ArrayUtils.contains(src, "管理员用户组")){
	        	isAdmin = true;
	        }
	        ActionContext.getContext().getSession().put("isAdmin",isAdmin);
	        ActionContext.getContext().getSession().put("userGroupName",userGroupName);
        	ActionContext.getContext().getSession().put("userGroupId",sugid);
	        
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
		return SUCCESS;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public String getUserName() {
		return userName;
	}

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
}
