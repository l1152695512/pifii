package com.yf.base.actions.map.area;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DeleteData extends ActionSupport {
	private static final long serialVersionUID = 1L;
//	private Logger logger = Logger.getLogger(this.getClass().getName());
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String[] ids;

	@Override
	public String execute() throws Exception {
		List<String> sqlList = new ArrayList<String>();
		for(int i=0;i<ids.length;i++){
			if(!"".equals(ids[i])){
				if(deleteMapAndPreview(ids[i])){
					sqlList.add("delete from bp_community_tbl where id='"+ids[i]+"'");
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
	private boolean deleteMapAndPreview(String mapId){
		boolean isOK = true;
		String sql = "select map,preview_image from bp_community_tbl where id='"+mapId+"' ";
		List<Map<String,String>> dataList = (List<Map<String, String>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,String> data = dataList.get(0);
			String mapPath = null==data.get("map")?"":data.get("map").toString();
			String previewImage = null==data.get("preview_image")?"":data.get("preview_image").toString();
//			String str = DeleteData.class.getClassLoader().getResource("").getPath();
//			String localPath = str.substring(1, str.indexOf("WEB-INF"));
			String localPath = GlobalVar.TOOLSPATH+"/";
			if(StringUtils.isNotBlank(mapPath)){
				String mapAbsolutePath = localPath+mapPath;
				File mapFile = new File(mapAbsolutePath);
				if(mapFile.exists()){
					isOK = mapFile.delete();
				}
			}
			if(StringUtils.isNotBlank(previewImage)){
				String previewImageAbsolutePath = localPath+previewImage;
				File file = new File(previewImageAbsolutePath);
				if(file.exists()){
					isOK =  file.delete();
				}
			}
		}
		return isOK;
	}

	public String[] getIds() {
		return ids;
	}
	
	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
