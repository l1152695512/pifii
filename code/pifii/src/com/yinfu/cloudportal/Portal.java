package com.yinfu.cloudportal;


import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.model.Feedback;
import com.yinfu.cloudportal.model.Food;
import com.yinfu.cloudportal.model.Queue;
import com.yinfu.cloudportal.model.Survey;
import com.yinfu.cloudportal.utils.Comms;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;

@ControllerBind(controllerKey = "/portal", viewPath = "/portal")
@Before(RouterInterceptor. class)
public class Portal extends Controller<Object>{
//	private static Logger logger = Logger.getLogger(PageController.class);
	public static String serverpath = PropertyUtils.getProperty("business.server.url");//商户平台的地址，如：http://www.pifii.com:8088/pifii
	public static final String REQUEST_MAC = "mac";
	public static final String REQUEST_SN = "routersn";
//	private static final String PHONE_ADV_ERROR_PAGE = "http://www.pifii.com/";
	
	
//	@ClearInterceptor
//	@ActionKey("portal")
	public void index() {
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record client = getSessionAttr(SessionParams.CLIENT_INFO);
		if(Comms.clientIsPC(getRequest())){
			redirect("/portal/pc/auth?routersn="+device.getStr("router_sn")+"&mac="+client.getStr("client_mac"));
		}else{
			redirect("/portal/mb?routersn="+device.getStr("router_sn")+"&mac="+client.getStr("client_mac"));
		}
	}
	
	@ActionKey("portal/mb")
	public void indexMb() {
		String type = getPara(0);
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		if("index".equals(type)){
			String portalUrl = "";
			if(null != getPara("shopId")){//用于商户平台的预览
				setAttr("isShow", "1");
				device.set("shop_id", getPara("shopId"));
			}
			portalUrl = Comms.getTemplateUrl(device.get("shop_id"),device.get("router_sn"));
			if(StringUtils.isNotBlank(portalUrl) && !portalUrl.startsWith("http://")){
				portalUrl = "/"+portalUrl;
			}
			forwardAction(portalUrl);
		}else{
			setAttr("start_adv", CommsRoute.getStartAdv(device.get("shop_id")));
			render("mb/goto.jsp");
		}
	}
	
	@ActionKey("portal/mb/adv")
	public void indexAdv(){
		String advId = getPara("id");
		Record rec =Db.findFirst("select ifnull(link,'') link from bp_adv_content where id=? ", new Object[]{advId});
		if(null != rec && StringUtils.isNotBlank(rec.getStr("link"))){
			String link = rec.getStr("link");
			if(!rec.getStr("link").startsWith("http://")){
				link = "http://"+link;
			}
			redirect(link);
		}else{
			setAttr("errorMsg", "广告没有配置链接地址，请在商户平台上配置！");
			render("/portal/mb/error/alert.jsp");
//			redirect(PHONE_ADV_ERROR_PAGE);
		}
	}
	@ActionKey("portal/mb/customapp")
	public void customapp(){
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record rec =Db.findFirst("select ifnull(url,'') url from bp_custom_app where shop_id=? ", new Object[]{device.get("shop_id")});
		if(null != rec && StringUtils.isNotBlank(rec.getStr("url"))){
			String link = rec.getStr("url");
			if(!rec.getStr("url").startsWith("http://")){
				link = "http://"+link;
			}
			redirect(link);
		}else{
			setAttr("errorMsg", "该应用没有配置链接地址，请在商户平台上配置！");
			render("/portal/mb/error/alert.jsp");
		}
	}
	
	
	@ActionKey("portal/mb/app")
	public void app() {
		setAttr("themes", Db.find("select id,name from bp_apk_theme"));
		setAttr("apks", Db.find("select name,score,down_num,theme,concat('"+Portal.serverpath+"','/',icon) icon,"
				+ "concat('"+Portal.serverpath+"','/',link) link from bp_apk where status=1 and delete_date is null"));
		render("mb/app/app.jsp");
	}
	
	@ActionKey("portal/mb/audio")
	public void audio() {
		String type = getPara(0);
		if("play".equals(type)){
			String audioId = getPara(1);
			setAttr("audio", Db.findFirst("select name,concat('"+Portal.serverpath+"','/',link) link from bp_audio where id=? ", new Object[]{audioId}));
			render("mb/audio/detail.jsp");
		}else{
			String context = getRequest().getContextPath();
			setAttr("audios", Db.find("select name,concat('"+context+"','/portal/mb/audio/play-',id) link,"
					+ "concat('"+Portal.serverpath+"','/',icon) icon from bp_audio where status=1 and delete_date is null "));
			render("mb/audio/audio.jsp");
		}
	}
	
	@ActionKey("portal/mb/book")
	public void book() {
		setAttr("themes", Db.find("select id,name from bp_book_theme"));
		StringBuffer sql = new StringBuffer();
		sql.append("select bth.name,bth.author,bth.words,bth.des,bth.theme,bty.name typeName,");
		sql.append("concat('"+Portal.serverpath+"','/',bth.img) img,concat('"+Portal.serverpath+"','/',bth.link) link ");
		sql.append("from bp_book bth join bp_book_type bty on (bth.type=bty.id) ");
		sql.append("where bth.status=1 and bth.delete_date is null ");
		setAttr("books", Db.find(sql.toString()));
		render("mb/book/book.jsp");
	}
	
	@ActionKey("portal/mb/games")
	public void games() {//上传的游戏压缩包需解压到对应的文件夹下，这里才能访问
		StringBuffer sql = new StringBuffer();
    	sql.append("select name,times,concat('"+Portal.serverpath+"','/',icon) icon,");
    	sql.append("concat('"+Portal.serverpath+"','/',substring_index(link,'.',1),'/index.htm') link ");
    	sql.append("from bp_game where status=1 and delete_date is null ");
    	setAttr("games", Db.find(sql.toString()));
		render("mb/games/game.jsp");
	}
	
	@ActionKey("portal/mb/video")
	public void video() {
		String type = getPara(0);
		if("play".equals(type)){
			String id = getPara(1);
			setAttr("video", Db.findFirst("select name,concat('"+Portal.serverpath+"','/',link) link from bp_video where id=? ", new Object[]{id}));
			render("mb/video/detail.jsp");
		}else{
			String context = getRequest().getContextPath();
			StringBuffer sql = new StringBuffer();
			sql.append("select a.name,concat('"+Portal.serverpath+"','/',icon) icon,");
			sql.append("concat('"+context+"','/portal/mb/video/play-',a.id) link,replace(group_concat(b.name),',','/') type ");
			sql.append("from bp_video a, bp_video_type b ");
			sql.append("where find_in_set(b.id, a.type) and a.status=1 and a.delete_date is null ");
			sql.append("group by a.id");
			setAttr("videos", Db.find(sql.toString()));
			render("mb/video/video.jsp");
		}
	}
	
	@ActionKey("portal/mb/introduce")
	public void introduce() {
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		StringBuffer sql = new StringBuffer();
    	sql.append("select a.des,b.name,concat('"+Portal.serverpath+"','/',a.file_path) link ");
    	sql.append("from bp_introduce a left join bp_shop b on a.shop_id=b.id ");
    	sql.append("where a.shop_id=? ");
    	setAttr("shop", Db.findFirst(sql.toString(), new Object[]{device.get("shop_id")}));
		render("mb/introduce/introduce.jsp");
	}
	
	@ActionKey("portal/mb/flowpack")
	public void flowpack() {
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		StringBuffer sql = new StringBuffer();
    	sql.append("select title,des,concat('"+Portal.serverpath+"','/',pic) pic ");
    	sql.append("from bp_flow_pack ");
    	sql.append("where shop_id=? and delete_date is null");
    	setAttr("rows", Db.find(sql.toString(),new Object[]{device.get("shop_id")}));
		render("mb/mall/flowpack.jsp");
	}
	@ActionKey("portal/mb/funny")
	public void funny() {
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		StringBuffer sql = new StringBuffer();
    	sql.append("select title,txt,date_format(create_date,'%Y年%m月%d日') create_date,concat('"+Portal.serverpath+"','/',img) img ");
    	sql.append("from bp_funny where shop_id=? and delete_date is null");
    	setAttr("rows", Db.find(sql.toString(),new Object[]{device.get("shop_id")}));
		render("mb/mall/funny.jsp");
	}

	@ActionKey("portal/mb/preferential")
	public void preferential() {
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		StringBuffer sql = new StringBuffer();
		sql.append("select title,txt,concat('"+Portal.serverpath+"','/',img) img ");
		sql.append("from bp_preferential where shop_id=? and delete_date is null ");
		setAttr("rows", Db.find(sql.toString(),new Object[]{device.get("shop_id")}));
		render("mb/mall/preferential.jsp");
	}
	
	@ActionKey("portal/mb/tide")
	public void tide() {
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		StringBuffer sql = new StringBuffer();
    	sql.append("select name,des,price,preprice,picdes,concat('"+Portal.serverpath+"','/',img) img ");
    	sql.append("from bp_tide where shop_id=? and delete_date is null");
    	setAttr("rows", Db.find(sql.toString(),new Object[]{device.get("shop_id")}));
		render("mb/mall/tide.jsp");
	}

	@ActionKey("portal/mb/queue")
	public void onlinequeing() {
		String cmd = getPara(0);
		if("get".equals(cmd) || "cancel".equals(cmd) || "history".equals(cmd)){
			Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
			Record client = getSessionAttr(SessionParams.CLIENT_INFO);
			Queue queue = new Queue(device.getStr("router_sn"),client.getStr("client_mac"));
			JSONObject json = null;
			if(cmd.indexOf("get") != -1){
				json = queue.getNum(getPara("personNum"));
			}else if(cmd.indexOf("cancel") != -1){
				json = queue.cancelNum();
			}else if(cmd.indexOf("history") != -1){
				json = queue.historyNum();
			}
			renderJson(json);
		}else{
			render("mb/onlinequeing/queue.jsp");
		}
	}

	@ActionKey("portal/mb/restaurant")
	public void restaurant() {
		String cmd = getPara(0);
		if("get".equals(cmd) || "save".equals(cmd)){
			Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
			Record client = getSessionAttr(SessionParams.CLIENT_INFO);
			Food food = new Food(device.getStr("router_sn"),client.getStr("client_mac"));
			JSONObject json = null;
			if(cmd.indexOf("save") != -1){
				json = food.saveFoodOrder(getPara("orderId"),getParaValues("food"));
			}else if(cmd.indexOf("get") != -1){
				json = food.getOrder(getPara("orderId"));
			}
			renderJson(json);
		}else{
			Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
	    	setAttr("types", Db.find("select id,name from bp_menu_type"));
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("select id,name,type,old_price,new_price,times,concat('"+Portal.serverpath+"','/',icon) icon ");
	    	sql.append("from bp_menu where shopId=? and delete_date is null");
	    	setAttr("menus", Db.find(sql.toString(), new Object[]{device.get("shop_id")}));
			render("mb/restaurant/index.jsp");
		}
	}

	@ActionKey("portal/mb/survey")
	public void survey() {
		String cmd = getPara(0);
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record client = getSessionAttr(SessionParams.CLIENT_INFO);
		Survey survey = new Survey(device.getStr("router_sn"),client.getStr("client_mac"));
		if("save".equals(cmd)){
			JSONObject json = new JSONObject();
			survey.save(getPara("options"));
			json.put("main_page", getMainPageWidthParams(device,client.getStr("client_mac")));
			renderJson(json);
		}else{
			setAttr("questions", JsonKit.toJson(survey.index()));
			render("mb/survey/index.jsp");
		}
	}
	
	@ActionKey("portal/mb/feedback")
	public void feedback() {
		String cmd = getPara(0);
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record client = getSessionAttr(SessionParams.CLIENT_INFO);
		Feedback feedback = new Feedback(device.getStr("router_sn"),client.getStr("client_mac"));
		if("save".equals(cmd)){
			JSONObject json = new JSONObject();
			feedback.save(getPara("opinion"));
			json.put("main_page", getMainPageWidthParams(device,client.getStr("client_mac")));
			renderJson(json);
		}else{
			render("mb/feedback/index.jsp");
		}
	}
	
	private String getMainPageWidthParams(Record device,String clientMac){
		if(null != device && null != device.get("type") && "1".equals(device.get("type").toString())){//如果确定是盒子
			String indexPage = PropertyUtils.getProperty("router.mainPage", "http://m.pifii.com/ifidc/pifii.html");
			if(indexPage.indexOf("?") == -1){
				indexPage += "?";
			}else{
				indexPage += "&";
			}
			indexPage += REQUEST_MAC+"="+clientMac+"&"+REQUEST_SN+"="+device.getStr("router_sn");
			return indexPage;
		}else{
			return "http://"+getRequest().getServerName()+":"+getRequest().getServerPort()+
					getRequest().getContextPath()+"/portal/mb/index?"+REQUEST_MAC+"="+clientMac+"&"+REQUEST_SN+"="+device.getStr("router_sn");
		}
	}
	
}
