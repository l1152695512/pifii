package com.yf.base.actions.map.area;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GridData extends ActionSupport {

		private static final long serialVersionUID = 1L;
		
		private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
		private static Map<String,String> nameToIndex = new HashMap<String,String>();
		private String jsonString;
		private int start;
		private int limit;
		private String dir;
		private String sort;
		
		private String pid;
		private String name;
		private String uploadDate;
		
		static{
			nameToIndex.put("communityName", "c.name");
			nameToIndex.put("districtName", "r1.name");
			nameToIndex.put("cityName", "r2.name");
			nameToIndex.put("provinceName", "r3.name");
			nameToIndex.put("createDate", "c.createDate");
		}
		@SuppressWarnings("unchecked")
		@Override
		public String execute() throws Exception {
			StringBuffer dataSql = new StringBuffer();
			dataSql.append("select c.id,c.name as communityName,c.area_type,c.dependent_community,r1.name as districtName,r2.name as cityName,r3.name as provinceName,DATE_FORMAT(c.createDate,'%Y-%m-%d %H:%i:%s') as createDate ");
			StringBuffer commonSql = new StringBuffer();
			commonSql.append("from bp_community_tbl c join yw_region_tbl r1 on c.districtId = r1.rid ");
			commonSql.append("join yw_region_tbl r2 on r2.rid = r1.pid ");
			commonSql.append("join yw_region_tbl r3 on r3.rid = r2.pid ");
			commonSql.append("where 1=1 ");
			String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
			if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
				commonSql.append(" and c.user_id = '"+userId+"' ");
			}
			if(StringUtils.isNotBlank(name)){
				commonSql.append(" and c.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"' ");
			}
			if(StringUtils.isNotBlank(pid)&&!isRoot()){
				commonSql.append(" and c.districtId in ("+getAllCommunityId()+") ");
			}
			if(StringUtils.isNotBlank(uploadDate)){
				commonSql.append(" and LEFT(c.createDate,10) = '"+uploadDate+"' ");
			}
			dataSql.append(commonSql.toString());
			dataSql.append(" order by "+nameToIndex.get(sort)+" " +dir+" LIMIT "+start+","+limit);
			System.err.println(dataSql.toString());
			List<?> dataList = dbhelper.getMapListBySql(dataSql.toString());
			
			StringBuffer countSql = new StringBuffer();
			countSql.append("select count(*) count ");
			countSql.append(commonSql.toString());
			List<Map<String,Object>> countList = (List<Map<String, Object>>) dbhelper.getMapListBySql(countSql.toString());
			long rowCount = (Long)countList.get(0).get("count");
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("totalRecord", rowCount);
			resultMap.put("list", dataList.toArray());
			String result=JsonUtils.map2json(resultMap);
			this.jsonString = result;
			return "data";
		}

		@SuppressWarnings("unchecked")
		private boolean isRoot(){
			String sql = "select rid from yw_region_tbl where pid is null or pid = ''";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				String rid = null==data.get("rid")?"":data.get("rid").toString();
				if(pid.equals(rid)){
					return true;
				}
			}
			return false;
		}
		@SuppressWarnings("unchecked")
		private String getAllCommunityId(){
			StringBuffer strB = new StringBuffer();
			String sql = "select rid from yw_region_tbl where pid = '"+pid+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size() == 0){
				return "'"+pid+"'";
			}
			Iterator<Map<String,Object>> ite = dataList.iterator();
			while(ite.hasNext()){
				Map<String,Object> data = ite.next();
				String rid = null==data.get("rid")?"":("'"+data.get("rid").toString()+"',");
				strB.append(rid);
			}
			String strReturn = strB.toString(); 
			return strReturn.substring(0, strReturn.length()-1);
		}
		
		public String getJsonString() {
			return jsonString;
		}
		
		public void setJsonString(String jsonString) {
			this.jsonString = jsonString;
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
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUploadDate() {
			return uploadDate;
		}
		
		public void setUploadDate(String uploadDate) {
			this.uploadDate = uploadDate;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}
}
