package com.yf.base.actions.initialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yf.util.DBUtil;

public class CompanyAndName
{
	public static Map<String,String> getCompanyMap()
	{
		Map<String,String> map = new HashMap<String,String>();
		map.put("广州分公司", "gz_admin_gh");
		map.put("深圳分公司", "sz_admin_gh");
		map.put("佛山分公司", "fs_admin_gh");
		map.put("东莞分公司", "dg_admin_gh");
		map.put("中山分公司", "zs_admin_gh");
		map.put("江门分公司", "jm_admin_gh");
		map.put("惠州分公司", "hz_admin_gh");
		map.put("茂名分公司", "mm_admin_gh");
		map.put("珠海分公司", "zh_admin_gh");
		map.put("湛江分公司", "zj_admin_gh");
		map.put("汕头分公司", "st_admin_gh");
		map.put("肇庆分公司", "zq_admin_gh");
		map.put("清远分公司", "qy_admin_gh");
		map.put("揭阳分公司", "jy_admin_gh");
		map.put("韶关分公司", "sg_admin_gh");
		map.put("梅州分公司", "mz_admin_gh");
		map.put("阳江分公司", "yj_admin_gh");
		map.put("河源分公司", "hy_admin_gh");
		map.put("云浮分公司", "yf_admin_gh");
		map.put("汕尾分公司", "sw_admin_gh");
		map.put("潮州分公司", "cz_admin_gh");
		return map;
	}
	
	public static List<String> getPrfessional()
	{
		DBUtil db = new DBUtil();
		List<String> proList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer("");
		sb.append("select key_name kname from sys_dictionary_tbl where type_name = 'professional'");
		
		List list = db.getMapListBySql(sb.toString());
		if(!list.isEmpty())
		{
			for(int i = 0;i < list.size(); i++)
			{
				Map map = (Map)list.get(i);
				proList.add(map.get("kname") == null ? "" : map.get("kname").toString());
			}
		}
		
		return proList;
	}
	
	public static List<String> getRoom()
	{
		DBUtil db = new DBUtil();
		List<String> roomList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer("");
		sb.append("select key_name kname from sys_dictionary_tbl where type_name = 'room'");
		
		List list = db.getMapListBySql(sb.toString());
		if(!list.isEmpty())
		{
			for(int i = 0;i < list.size(); i++)
			{
				Map map = (Map)list.get(i);
				roomList.add(map.get("kname") == null ? "" : map.get("kname").toString());
			}
		}
		
		return roomList;
	}
	
	public static List<String> getPowerPlanType()
	{
		DBUtil db = new DBUtil();
		List<String> plist = new ArrayList<String>();
		StringBuffer sb = new StringBuffer("");
		sb.append("select key_name kname from sys_dictionary_tbl where type_name = 'powerplantype'");
		
		List list = db.getMapListBySql(sb.toString());
		if(!list.isEmpty())
		{
			for(int i = 0;i < list.size(); i++)
			{
				Map map = (Map)list.get(i);
				plist.add(map.get("kname") == null ? "" : map.get("kname").toString());
			}
		}
		
		return plist;
	}
}
