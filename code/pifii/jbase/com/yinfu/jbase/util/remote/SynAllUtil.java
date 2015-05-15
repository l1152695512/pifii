package com.yinfu.jbase.util.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.util.PropertyUtils;


public class SynAllUtil {

    private static SynAllUtil m_instance = null;
    private List<String> sqlList = null;
    private String shopId = "0";
    private String isPublish = "0";
    private String sn = "0";
    private String cmdSql = "";
    
    
	public SynAllUtil() {
		
	}
	
    public synchronized static SynAllUtil getInstance() {
		if (m_instance == null) {
			m_instance = new SynAllUtil();
		}
		return m_instance;
	}
    
    /**
     * 根据sn同步全部最新数据
     * @param sn
     * @return
     */
    public boolean synAllData(String sn){
    	boolean bool = false;
		try {
			String sql = "select a.shop_id,ifnull(b.is_publish,'0') is_publish from bp_device a left join bp_shop_page b on a.shop_id=b.shop_id where a.router_sn=?";
			Record rd = Db.findFirst(sql, new Object[]{sn});
			if(rd != null) {
				this.sn = sn;
				this.shopId = rd.getInt("shop_id")+"";
				this.isPublish = rd.getStr("is_publish");
				this.sqlList = new ArrayList<String>();
				String uid = UUID.randomUUID().toString();
				this.cmdSql = "insert into bp_cmd(uid,type,url,dir) values('"+uid;
				sqlList.add("insert into bp_task(router_sn,type,key_id,create_date,uid,is_publish) values('"+sn+"','synAll','"+sn+"',now(),'"+uid+"',"+isPublish+")");
			
				StringBuffer fSql = new StringBuffer();
				fSql.append("select a.app_id,c.name,ifnull(c.marker,'no') marker ");
				fSql.append("from bp_shop_page_app a ");
				fSql.append("left join bp_shop_page b ");
				fSql.append("on a.page_id=b.id ");
				fSql.append("left join bp_app c ");
				fSql.append("on a.app_id=c.id ");
				fSql.append("where b.shop_id = "+this.shopId);
				List<Record> list = Db.find(fSql.toString());
				
				if(list.size()>0){
					synAdv();
					synShop();
					synApp();
					String link = "file/freemarker/html/"+shopId+"/mb/index.html";
					sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
				}
				
				synVideo();
				synAudio();
//				synGames();
//				synBook();
				synApk();
				
				for(Record appRd : list){
//					if("video".equals(appRd.getStr("marker"))){
						
//					}else if("audio".equals(appRd.getStr("marker"))){
//					}else if("game".equals(appRd.getStr("marker"))){
//					}else if("book".equals(appRd.getStr("marker"))){
//					}else if("apk".equals(appRd.getStr("marker"))){
//						
//					}else 
						
					if("preferential".equals(appRd.getStr("marker"))){
						synPreferential();
					}else if("tide".equals(appRd.getStr("marker"))){
						synTide();
					}else if("flowpack".equals(appRd.getStr("marker"))){
						synFlowPack();
					}else if("funny".equals(appRd.getStr("marker"))){
						synFunny();
					}else if("introduce".equals(appRd.getStr("marker"))){
						synIntroduce();
					}
				}
				
				if(Db.batch(sqlList, sqlList.size()).length>0){
					bool = true;
					PageUtil.changPageLog(this.shopId, "synAll", this.sn, "2");
				}
			}
		} catch (Exception e) {
			System.out.println("一键同步出错："+e);
		}
		return bool;
    }
    
    private void synAdv(){
    	StringBuffer sql = new StringBuffer();
		sql.append("select distinct bac.img image ");
		sql.append("from bp_adv_shop bas ");
		sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
		sql.append("join bp_adv_type bat on (bat.template_id=? and bas.adv_type_id=bat.id) ");
		sql.append("join bp_adv_spaces basp on (basp.adv_type='adv' and basp.id=bat.adv_spaces) ");
		sql.append("where bas.shop_id =? and bac.img is not null ");
		List<Record> list = Db.find(sql.toString(), new Object[]{PageUtil.getTemplateId(shopId),shopId});
//    	String sql = "select image from bp_adv where shop_id=? and template_id=? and delete_date is null";
//    	List<Record> list = Db.find(sql, new Object[]{this.shopId,PageUtil.getTemplateId(shopId)});
		for(Record rd : list) {
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
		}
    }
    
    private void synShop(){
    	String sql = "select icon from bp_shop where id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			if(!"".equals(rd.getStr("icon")) && rd.getStr("icon") != null){
				sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexShop","/storageroot/Data/mb/logo")+"')");//图片
			}
		}
    }
    
    private void synApp(){
    	String sql = "select c.icon from bp_shop_page_app a left join bp_shop_page b on a.page_id=b.id left join bp_app c on a.app_id=c.id where b.shop_id=?";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexApp","/storageroot/Data/mb/logo")+"')");//图片
		}
    }
    
    private void synPreferential(){
    	boolean flag = false;
    	String sql = "select img from bp_preferential where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+shopId+"/mb/mall/preferential.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
    }
    
    private void synFlowPack(){
    	boolean flag = false;
    	String sql = "select pic from bp_flow_pack where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("pic")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+shopId+"/mb/mall/flowpack.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
    }
    
    private void synTide(){
    	boolean flag = false;
    	String sql = "select img from bp_tide where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+shopId+"/mb/mall/tide.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
	}
    
    private void synFunny(){
    	boolean flag = false;
    	String sql = "select img from bp_funny where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+this.shopId+"/mb/mall/funny.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
	}
    
    private void synIntroduce(){
    	boolean flag = false;
    	String sql = "select file_path from bp_introduce where shop_id=?";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("file_path")+"','"+PropertyUtils.getProperty("downdir.introduceFile","/storageroot/Data/mb/introduce/f")+"')");//图片
		}
		if(flag){
			String link = "file/freemarker/html/"+this.shopId+"/mb/introduce/introduce.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.introduce","/storageroot/Data/mb/introduce")+"')");//html
		}
    }
    
    
    private void synVideo(){
    	boolean flag = false;
    	String sql = "select icon,link from bp_video where status=1";
    	List<Record> list = Db.find(sql);
		for(Record rd : list) {
			flag = true;
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.videoImg","/storageroot/Data/mb/video/logo")+"')");//图片
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.videoFile","/storageroot/Data/mb/video/v")+"')");//文件
		}
		if(flag){
			String link = "file/freemarker/html/0/mb/video/video.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.video","/storageroot/Data/mb/video")+"')");//html
		}
    }
    
    private void synAudio(){
    	boolean flag = false;
    	String sql = "select icon,link from bp_audio where status=1";
    	List<Record> list = Db.find(sql);
		for(Record rd : list) {
			flag = true;
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.audioImg","/storageroot/Data/mb/audio/logo")+"')");//图片
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.audioFile","/storageroot/Data/mb/audio/m")+"')");//文件
		}
		
		if(flag){
			String link = "file/freemarker/html/0/mb/audio/audio.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.audio","/storageroot/Data/mb/audio")+"')");//html
		}
    }
    
    private void synGames(){
    	boolean flag = false;
    	String sql = "select icon,link from bp_game where status=1";
    	List<Record> list = Db.find(sql);
		for(Record rd : list) {
			flag = true;
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.gamesImg","/storageroot/Data/mb/games/logo")+"')");//图片
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.gamesFile","/storageroot/Data/mb/games/f")+"')");//文件
		}
		if(flag){
			String link = "file/freemarker/html/0/mb/games/games.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.games","/storageroot/Data/mb/games")+"')");//html
		}
    }
    
    private void synApk(){
    	boolean flag = false;
    	String sql = "select icon,link from bp_apk where status=1";
    	List<Record> list = Db.find(sql);
		for(Record rd : list) {
			flag = true;
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.appImg","/storageroot/Data/mb/app")+"')");//图片
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.appFile","/storageroot/Data/mb/app/f")+"')");//文件
		}
		if(flag){
			String link = "file/freemarker/html/0/mb/app/app.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.app","/storageroot/Data/mb/app")+"')");//html
		}
    }
    
    private void synBook(){
    	boolean flag = false;
    	String sql = "select img,link from bp_book where status=1";
    	List<Record> list = Db.find(sql);
		for(Record rd : list) {
			flag = true;
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.bookImg","/storageroot/Data/mb/book/img")+"')");//图片
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.bookFile","/storageroot/Data/mb/book/file")+"')");//文件
		}
		if(flag){
			String link = "file/freemarker/html/0/mb/book/book.html";
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.book","/storageroot/Data/mb/book")+"')");//html
		}
		
    }
    
   
    public static void main(String[] args)throws Exception{
    	
    }
}
