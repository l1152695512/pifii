package com.yinfu.business.application.controller;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ContextUtil;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.IndexTask;
import com.yinfu.routersyn.util.SynUtils;
/**
 * 
 * 广告Controller
 * @author JiaYongChao
 *
 */
@ControllerBind(controllerKey = "/business/app/adv", viewPath = "/page/business/application/adv")
public class AdvController extends Controller<Record>
{
	private static final String PREVIEW_PATH = "upload/image/adv/";
	
	public void index(){
		render("index.jsp");
	}
	
	/**
	 * 根据shopId获取记录
	 */
	public void getAdvs(){
		StringBuffer sql = new StringBuffer();
		//sql解释：bao.id is null or !bao.edit_able 时商户是没有权限编辑广告的，所以没必要把修改广告时需要的数据查出来
		sql.append("select if(bao.id is null or !bao.edit_able,'',basp.id) adv_spaces_id,if(bao.id is null or !bao.edit_able,'',IFNULL(bas.id,'')) id,");
		sql.append("if(bao.id is null or !bao.edit_able,'',IFNULL(bac.id, '')) adv_content_id,");
		sql.append("bat.img_width,bat.img_height,IFNULL(bac.name, basp.name) name,IFNULL(batr.res_url, '') image ");
		sql.append("from bp_adv_spaces basp join bp_adv_type bat on (basp.id=bat.adv_spaces) ");
		sql.append("left join bp_shop s on (s.id=?) ");
		sql.append("left join bp_adv_org bao on (bao.org_id=s.org_id and bao.adv_spaces=basp.id) ");
		sql.append("left join bp_adv_shop bas on (bas.shop_id=s.id and bas.adv_spaces=basp.id) ");
		sql.append("left join bp_adv_content bac on (bac.id=bas.content_id) ");
		sql.append("left join bp_adv_type_res batr on (batr.content_id=bac.id and batr.adv_type_id=bat.id) ");
		sql.append("where bat.template_id=? and basp.adv_type='adv' ");
		sql.append("order by basp.adv_index ");
//		sql.append("select if(bao.id is null,'',basp.id) adv_spaces_id,if(bao.id is null,'',IFNULL(bas.id,'')) id,");
//		sql.append("if(bao.id is null,'',IFNULL(bac.id, '')) adv_content_id,");
//		sql.append("bat.img_width,bat.img_height,IFNULL(bac.name, basp.name) name,IFNULL(batr.res_url, '') image ");
//		sql.append("from bp_adv_spaces basp join bp_adv_type bat on (basp.id=bat.adv_spaces) ");
//		sql.append("left join bp_shop s on (s.id=?) ");
//		sql.append("left join bp_adv_org bao on (bao.org_id=s.org_id and bao.adv_type_id=bat.id) ");
//		sql.append("left join bp_adv_shop bas on (bas.shop_id=s.id and bas.adv_type_id=bat.id) ");
//		sql.append("left join bp_adv_content bac on (bac.id=bas.content_id) ");
//		sql.append("left join bp_adv_type_res batr on (batr.content_id=bac.id and batr.adv_type_id=bat.id) ");
//		sql.append("where bat.template_id=? and basp.adv_type='adv' ");
//		sql.append("order by basp.adv_index ");
//		sql.append("select if(bao.id is null,'',basp.id) adv_spaces_id,if(bao.id is null,'',IFNULL(bas.id,'')) id,");
//		sql.append("if(bao.id is null,'',IFNULL(bac.id, '')) adv_content_id,");
//		sql.append("bat.img_width,bat.img_height,IFNULL(bac.name, basp.name) name,IFNULL(batr.res_url, '') image ");
//		sql.append("from bp_adv_spaces basp join bp_adv_type bat on (basp.id=bat.adv_spaces) ");
//		sql.append("left join bp_shop s on (s.id=?) ");
//		sql.append("left join bp_adv_org bao on (bao.org_id=s.org_id and bao.adv_type_id=bat.id) ");
//		sql.append("left join bp_adv_shop bas on (bas.shop_id=s.id and bas.adv_type_id=bat.id) ");
//		sql.append("left join bp_adv_content bac on (bac.id=bas.content_id) ");
//		sql.append("left join bp_adv_template_res batr on (batr.content_id=bac.id and batr.template_id=?) ");
//		sql.append("where bat.template_id=? and basp.adv_type='adv' ");
//		sql.append("order by basp.adv_index ");
		Object templateId = PageUtil.getTemplateId(getPara("shopId"));
		List<Record> list = Db.find(sql.toString(), new Object[]{getPara("shopId"),templateId});
		renderJson(list);
	}
	
	public void addOrEditInfo(){
		setAttr("id", getPara("id"));
		setAttr("advSpacesId", getPara("advSpacesId"));
		setAttr("advContentId", getPara("advContentId"));
		setAttr("imgWidth", getPara("imgWidth"));
		setAttr("imgHeight", getPara("imgHeight"));
		if(StringUtils.isNotBlank(getPara("advContentId"))){
			StringBuffer sql = new StringBuffer();
			sql.append("select ifnull(bac.name,'') name,ifnull(batr.res_url,'') img,ifnull(bac.link,'') link,ifnull(bac.des,'') des ");
			sql.append("from bp_adv_content bac ");
			sql.append("left join bp_adv_type_res batr on (bac.id=batr.content_id) ");
			sql.append("left join bp_adv_type bat on (bat.template_id=? and bat.id=batr.adv_type_id) ");
			sql.append("where bac.id=? ");
			Record adv = Db.findFirst(sql.toString(),new Object[]{PageUtil.getTemplateId(getPara("shopId")),getPara("advContentId")});
			if(null != adv){
				setAttr("name", adv.get("name"));
				setAttr("img", adv.get("img"));
				setAttr("link", adv.get("link"));
				setAttr("des", adv.get("des"));
			}
		}
		render("edit.jsp");
	}
	
	private Record getAdvModel(){
		Record advContent = new Record();
		advContent.set("id", getPara("advContentId"));
//		advContent.set("adv_type_id", getPara("advTypeId"));
		advContent.set("name", getPara("name"));
//		advContent.set("img", getPara("img"));
		advContent.set("link", getPara("link"));
		advContent.set("des", getPara("des"));
		return advContent;
	}
	public void add(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			UploadFile file = getFile("upload", PathKit.getWebRootPath()+"/"+PREVIEW_PATH.replaceAll("/", File.separator+File.separator));
			String image="";
			if(file!=null){
				String name = String.valueOf(System.currentTimeMillis());
				ImageKit.renameFile(file, name);
				File src = file.getFile();
				image = PREVIEW_PATH + name + ImageKit.getFileExtension(src.getName());
			}
//			if(!image.equals("")){//如果修改了图片，则需要删除盒子里以前的图片
//				advContent.set("img", image);
//			}
			final Record advContent = getAdvModel();//.set("id", getPara("id"));
			String operate = "添加";
			Object templateId = PageUtil.getTemplateId(getPara("shopId"));
			Record advTypeRec = Db.findFirst("select id from bp_adv_type where adv_spaces=? and template_id=? ", new Object[]{getPara("advSpacesId"),templateId});
			if(null != advTypeRec){
				List<String> routerDeleteRes = new ArrayList<String>();//更新广告图片后需要删除盒子里旧的广告图片
				boolean hasOldRes = false;
				boolean isUpdate = false;
				if(null!=advContent.get("id") && StringUtils.isNotBlank(advContent.get("id").toString())){//编辑广告，这里不能删除之前的图片文件，可能有其他关联
					if(!image.equals("")){
						Record oldAdv = Db.findFirst("select ifnull(res_url,'') image from bp_adv_type_res where content_id=? and adv_type_id=? ", 
								new Object[]{getPara("advContentId"),advTypeRec.get("id")});
						if(null != oldAdv){
							hasOldRes = true;
							if(StringUtils.isNotBlank(oldAdv.getStr("image"))){
								String fileName = oldAdv.getStr("image").substring(oldAdv.getStr("image").lastIndexOf("/")+1);
								routerDeleteRes.add("/storageroot/Data/mb/logo/"+fileName);
							}
						}
					}
					Record advShop = Db.findFirst("select id from bp_adv_shop where shop_id=? and adv_spaces=? and update_by_shop order by create_date desc", 
							new Object[]{getPara("shopId"),getPara("advSpacesId")});//如果该广告之前是自己投放的则本次操作是修改，如果是由组织投放的则本次操作为添加
					if(null == advShop){
						insertShopAdv(advContent);
					}else{
						advContent.set("update_date", new Timestamp(System.currentTimeMillis()));
						Db.update("bp_adv_content", advContent);
						isUpdate = true;
						operate = "修改";
					}
				}else{//新增广告
					insertShopAdv(advContent);
				}
				if(hasOldRes && isUpdate){//修改或者新加了广告的图片资源
					Db.update("update bp_adv_type_res set res_url=? where content_id=? and adv_type_id=? ", 
							new Object[]{image,advContent.get("id"),advTypeRec.get("id")});
				}else{
					Db.update("insert into bp_adv_type_res(adv_type_id,content_id,res_url,create_date) values(?,?,?,now()) ", 
							new Object[]{advTypeRec.get("id"),advContent.get("id"),image});
				}
				boolean success = true;
				List<String> sqls = new ArrayList<String>();
				Record taskInfo = new Record().set("task_desc", operate+"了广告【"+advContent.get("name")+"】");
				Map<String,List<File>> res = IndexTask.synRes(getPara("shopId"), sqls, taskInfo,routerDeleteRes,null);
				if(null != res){
					SynUtils.putAllFiles(deleteRes, res);
					if(sqls.size()>0){
						DbExt.batch(sqls);
					}
					success = true;
				}else{
					success = false;
				}
				return success;
			}else{
				return false;
			}
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			//旧的升级任务展示
//			try{
////				String shopId = getPara("adv.shop_id");
////				String serial = getPara("adv.serial");
////				Record record= Db.findFirst("select id from bp_adv where shop_id=? and serial=? and template_id=? and delete_date is null", 
////						new Object[]{shopId,serial,PageUtil.getTemplateId(shopId)});
////				if(null != record){
//					boolean success = DataSynUtil.addTask(getPara("shopId"), "index_adv", advContent.get("id").toString(), "1");
////					if(success){//这里不能基于succes判断是否执行下面代码，坑
//						PageUtil.changPageLog(getPara("shopId"), "index_adv", advContent.get("id").toString(),"2");//记录更新日志
////					}
////				}
//			}
//			catch(Exception e){
//				e.printStackTrace();
//			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	
	private void insertShopAdv(Record advContent){
		String id = UUID.randomUUID().toString();
		advContent.set("id", id);
		advContent.set("create_user", ContextUtil.getCurrentUserId());
		advContent.set("create_date", new Timestamp(System.currentTimeMillis()));
		advContent.set("update_date", new Timestamp(System.currentTimeMillis()));
		Db.save("bp_adv_content", advContent);//这个方法里会把id覆盖掉，所以下面需要重新设置
		advContent.set("id", id);
		if(StringUtils.isNotBlank(getPara("id"))){
			Db.update("update bp_adv_shop set content_id=?,update_by_shop=1 where shop_id=? and adv_spaces=? ", 
					new Object[]{advContent.get("id"),getPara("shopId"),getPara("advSpacesId")});
		}else{
			Db.update("insert into bp_adv_shop(id,shop_id,adv_spaces,content_id,update_by_shop,create_date) values(?,?,?,?,1,now()) ", 
					new Object[]{UUID.randomUUID().toString(),getPara("shopId"),getPara("advSpacesId"),advContent.get("id")});
		}
	}

}

