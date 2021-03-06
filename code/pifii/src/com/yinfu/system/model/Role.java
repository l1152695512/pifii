package com.yinfu.system.model;

import java.util.ArrayList;
import java.util.List;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.jbase.jfinal.ext.ListUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.easyui.Tree;
import com.yinfu.shiro.ShiroCache;

@TableBind(tableName = "system_role")
public class Role extends Model<Role>
{
	private static final long serialVersionUID = -5747359745192545106L;
	public static Role dao = new Role();

	public List<String> getResUrl(String name)
	{
		return Res.dao.getAttr(sql("system.res.getResUrl"), "url", name);

	}

	public List<Role> getRole(int uid)
	{
		return Role.dao.find(sql("system.role.getRole"), uid);
	}

	@Override
	public List<Role> list()
	{
		List<Role> list = Role.dao.find(sql("system.role.list"));

		for (Role r : list)
		{
			List<Res> res = Res.dao.getRes(r.getId());
			r.put("res_ids", ListUtil.listToString(res, "id"));
			r.put("res_names", ListUtil.listToString(res, "name"));
		}

		return list;
	}

	public List<Tree> getTree(Integer id, Integer passId)
	{
		// 根据用户角色来获取 列表
		List<Tree> trees = new ArrayList<Tree>();

		for (Role res : getChild(id))
		{
			if (res.getId().equals(passId)) continue;
			Tree tree = new Tree(res.getId(), res.getPid(), res.getName(), res.getIconCls(), res, false);

			tree.children = getTree(res.getId(), passId);
			if (tree.children.size() > 0) tree.changeState();

			trees.add(tree);
		}
		return trees;
	}

	public List<Role> getChild(Integer id)
	{
		if (id == null) return dao.list(" where pid is null order by seq");
		return dao.list(" where pid = ? order by seq ", id);

	}

	public boolean grant(int roleId, String resIds)
	{
		boolean result=false;
		if (!Validate.isEmpty(resIds)) result= Res.dao.batchAdd(roleId, resIds);
		ShiroCache.clearAuthorizationInfoAll();
		return result;
	}

	/***
	 * 批量授权 会先删除所有权限再授权
	 * 
	 * @param roleId
	 * @param resIds
	 * @return
	 */
	public boolean batchGrant(int roleId, String resIds)
	{
		boolean result = Db.deleteById("system_role_res", "role_id", roleId);
		if (!Validate.isEmpty(resIds)) result = Res.dao.batchAdd(roleId, resIds);

		ShiroCache.clearAuthorizationInfoAll();

		return result;
	}

	//@formatter:off 
	/**
	 * Title: childNodeData
	 * Description:节点数据加载
	 * Created On: 2014年8月4日 下午5:36:17
	 * @author JiaYongChao
	 * <p>
	 * @param id
	 * @return 
	 */
	//@formatter:on
	public String childNodeData(String id) {
		String sql = null;
		List<Role> list = null;
		if(null != id){
			sql = " select ids, names, iconCls from system_role where pid = ? order by seq asc ";
			list = Role.dao.find(sql, id);
			
		}else{
			sql = " select ids, names, iconCls from system_role where pid is null order by seq asc ";
			list = Role.dao.find(sql);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size() - 1;
		for (Role role : list) {
			sb.append(" { ");
			sb.append(" id : '").append(role.getStr("id")).append("', ");
			sb.append(" name : '").append(role.getStr("name")).append("', ");
			sb.append(" isParent : true, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append("/jsFile/zTree/css/zTreeStyle/img/diy/").append(role.getStr("iconCls")).append("' ");
			sb.append(" }");
			if(list.indexOf(role) < size){
				sb.append(", ");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}

}
