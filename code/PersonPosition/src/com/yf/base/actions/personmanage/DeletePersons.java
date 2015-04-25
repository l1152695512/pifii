package com.yf.base.actions.personmanage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DeletePersons extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String[] ids;

	@Override
	public String execute() throws Exception {
		List<String> sqlList = new ArrayList<String>();
		for(int i=0;i<ids.length;i++){
			if(!"".equals(ids[i])){
				if(deletePhoto(ids[i])){
					sqlList.add("delete from bp_person_tbl where id='"+ids[i]+"'");
				}
			}
		}
		boolean isSuccess = dbhelper.executeFor(sqlList);
		if(isSuccess){
			return super.execute();
		}
		return "failure";
	}

	@SuppressWarnings("unchecked")
	private boolean deletePhoto(String personId){
		String sql = "select photo from bp_person_tbl where id='"+personId+"' ";
		List<Map<String,String>> dataList = (List<Map<String, String>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,String> data = dataList.get(0);
			String photoPath = null==data.get("photo")?"":data.get("photo").toString();
			if(StringUtils.isNotBlank(photoPath)){
//				String str = DeletePersons.class.getClassLoader().getResource("").getPath();
//				String localPath = str.substring(1, str.indexOf("WEB-INF"));
				String localPath = GlobalVar.TOOLSPATH+"/";
				String photoAbsolutePath = localPath+photoPath;
				File photoFile = new File(photoAbsolutePath);
				if(photoFile.exists()){
					return photoFile.delete();
				}
			}
		}
		return true;
	}
	
	public String[] getIds() {
		return ids;
	}
	
	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
