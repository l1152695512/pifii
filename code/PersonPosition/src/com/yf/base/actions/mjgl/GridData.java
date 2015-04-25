package com.yf.base.actions.mjgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.util.DBUtil;
import com.yf.util.JsonUtils;

public class GridData extends ActionSupport {

	private String msg;
	private int start;
	private int limit = 20;
	private String tableName;
	private String sort;
	private String dir = "DESC";
	private String jsonString;
	

	@Override
	public String execute() throws Exception {
		try{
			String sql = "select * FROM aaa WHERE to_number(tid)=(SELECT max(to_number(tid)) FROM aaa where tstats='1')";
			List list = new DBUtil().getMapListBySql(sql);
			String json = "";
			try {
				json = JsonUtils.object2json(list.toArray());
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.jsonString = json.substring(1, json.length()-1);
		}catch(Exception e){
			e.printStackTrace();
		}
	
		return "data";
	}

	protected List getPageList() {
		List<Map> pageList = new ArrayList<Map>();
		List list = new ArrayList();
		List<Map> totList = new ArrayList<Map>();
		Map bkMap = new HashMap();
		Map map = new HashMap();
		//总记录数
		int rowCount = 0;
		//计算总页数
        int totalpage = 0; 
		// 默认显示20条数据，防止未提交limit，出现by zero异常！
		if (limit <= 0) {
			limit = 20;
		}
		int currentPage = start / limit + 1;
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from (select m.*,rownum rn from(select * from "+this.tableName+" order by "+sort+" "+dir);
		getListSql.append(") m where rownum <= "+limit+"*"+currentPage+") where rn > "+limit+"*("+currentPage+"-1)");
		if (sort != null && dir != null) {
			list = new DBUtil().getMapListBySql(getListSql.toString());
		} else {
			list = new DBUtil().getMapListBySql(getListSql.toString());
		}
		StringBuffer getTotSql = new StringBuffer();
		getTotSql.append("select count(*) as rowCount from "+this.tableName);
		totList = (List<Map>) new DBUtil().getMapListBySql(getTotSql.toString());
		if(!totList.isEmpty()){
			map = totList.get(0);
			rowCount = Integer.parseInt(map.get("rowCount").toString());
		}
		totalpage = rowCount%limit ==0 ? rowCount/limit : rowCount/limit + 1 ;
		
		bkMap.put("currentPage", currentPage);
		bkMap.put("firstPage", totalpage != 1 && currentPage != 1 ? false : true);
		bkMap.put("lastPage", totalpage>currentPage ? false : true);
		bkMap.put("pageSize", limit);
		bkMap.put("totalPage", totalpage);
		bkMap.put("listSize", list.size());
		bkMap.put("totalRecord", rowCount);
		bkMap.put("list", list.toArray());
		
		pageList.add(bkMap);
		return pageList;
	}
	

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
