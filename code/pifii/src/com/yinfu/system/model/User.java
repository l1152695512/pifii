package com.yinfu.system.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.Consts;
import com.yinfu.jbase.jfinal.ext.ListUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.model.easyui.DataGrid;
import com.yinfu.model.easyui.Form;
import com.yinfu.shiro.ShiroCache;

@TableBind(tableName = "system_user")
public class User extends Model<User>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static User dao = new User();
	
	
	/***
	 * 隐藏 id 1
	 */
	public DataGrid<User> listByDataGrid(DataGrid<User> dg, Form f)
	{
		dg = super.listByDataGrid(sql("system.user.list"), dg, f);
		Iterator<User> list = dg.rows.iterator();
		User now = ShiroExt.getSessionAttr(Consts.SESSION_USER);

		while (list.hasNext())
		{
			User u = list.next();
			List<Role> role = Role.dao.getRole(u.getId());
			u.put("role_ids", ListUtil.listToString(role, "id"));
			u.put("role_names", ListUtil.listToString(role, "name"));
			if (u.getId() == 1 && now.getId() != 1) list.remove();
		}

		return dg;
	}

	public List<String> getRolesName(String loginName)
	{
		return getAttr(sql("system.role.getRolesName"), "name", loginName);
	}
	
	public boolean grant(Integer[] role_ids, Integer userId)
	{
		boolean result = Db.deleteById("system_user_role", "user_id", userId);

		if (role_ids == null) return result;

		Object[][] params = ListUtil.ArrayToArray(userId, role_ids);
		result = Db.batch("insert into system_user_role(user_id,role_id)  values(?,?)", params, role_ids.length).length > 0;

		ShiroCache.clearAuthorizationInfoAll();

		return result;
	}

	public User encrypt()
	{
		String pwd = this.getPwd();
		if(Validate.isEmpty(pwd))pwd="123456";
		
		this.set("pwd", Sec.md5(pwd));
		return this;
	}

	public boolean batchGrant(Integer[] role_ids, String uids)
	{
		boolean result = Db.update("delete from system_user_role where user_id in (" + uids + ")") > 0;

		if (role_ids == null) return result;

		Object[][] params = ListUtil.ArrayToArray(uids, role_ids);

		result = Db.batch("insert into system_user_role(user_id,role_id)  values(?,?)", params, params.length).length > 0;

		ShiroCache.clearAuthorizationInfoAll();

		return result;
	}

	public boolean changeStaus(Integer id, Integer status)
	{
		if(status==null)return false;
		if(status.equals(1)) status=2;
		else status=1;
		return dao.update("status",status,id) ;

	}

	//@formatter:off 
	/**
	 * Title: getUserPic
	 * Description:获得用户图片
	 * Created On: 2014年7月29日 下午3:38:09
	 * @author JiaYongChao
	 * <p>
	 * @param valueOf 
	 */
	//@formatter:on
	public void getUserPic(Long id,HttpServletResponse response) {
		
	}

	//@formatter:off 
	/**
	 * Title: findList
	 * Description:分页显示数据
	 * Created On: 2014年8月4日 上午9:56:22
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage 
	 */
	//@formatter:on
	public SplitPage findList(SplitPage splitPage) {
		String sql = "select system_user.*, role.name as roleName ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from system_user left join system_user_role as user_role on user_role.user_id =system_user.id left join system_role as role on role.id = user_role.role_id   ");
		formSqlSb.append(" where 1=1 ");
		if(null == queryParam){
			return;
		}
	}
	
}
