package com.yinfu.routersyn;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.InitDemoDbConfig;
import com.yinfu.business.util.DataOrgUtil;

public class DataBaseInit {
	public static void main(String[] args) {
		InitDemoDbConfig.initPlugin("jdbc:mysql://223.82.251.51:3306/jxsqt?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
				"root", "jxyd2403");
//		InitDemoDbConfig.initPlugin("jdbc:mysql://127.0.0.1:3306/pifii?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
//				"root", "ifidc1120");
		
//		InitDemoDbConfig.initPlugin("jdbc:mysql://113.106.98.233:3306/pifii_test?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
//				"root", "ifidc1120");
		
//		initShopGroup();
//		initAdvOrg();
//		initNewAdv();
//		setTemp();
//		getPostDevice();
//		setPostShopIcon();
//		initShopAuthTypes();
		
//		insertMacBlackList();
		
//		System.err.println(DataOrgUtil.recordListToSqlIn(DataOrgUtil.getShopsForUser(740), "id"));
		
//		Db.tx(new IAtom(){public boolean run() throws SQLException {
//			String temporaryName = DataOrgUtil.getShopsForUserInTemporary(90);
//			List<Record> list = Db.find("select id from "+temporaryName);
//			System.err.println(list.size());
//			System.err.println(DataOrgUtil.recordListToSqlIn(list, "id"));
//			
////			StringBuffer todayPersonSql = new StringBuffer();
////			todayPersonSql.append("select count(distinct r.mac) person_num ");
////			todayPersonSql.append("from bp_device d ");
////			todayPersonSql.append("join "+temporaryName+" temp on (d.shop_id=temp.id)");
////			todayPersonSql.append("join bp_report r on (r.sn=d.router_sn and date(r.create_date)=date(now())) ");
////			System.err.println(todayPersonSql.toString());
//////			todayPersonSql.append("and d.shop_id in ("+shopSqlIn+")) ");
////			Record today = Db.findFirst(todayPersonSql.toString());
////			System.err.println(today.get("person_num"));
//			
//			Db.update("DROP TEMPORARY TABLE IF EXISTS "+temporaryName);
//			return true;
//		}});
//		Db.update("CREATE TEMPORARY TABLE tmp_table(id VARCHAR(40))");
//		Db.update("insert into tmp_table(id) values('123')");
//		Db.update("insert into tmp_table(id) values('456')");
//		List<Record> list = Db.find("select * from tmp_table");
//		System.err.println(DataOrgUtil.recordListToSqlIn(list,"id"));
		
		
		insertDevice();
	}
	
	public static void insertDevice() {
		String[] routers = new String[]{
				"0100860000005832"
				,"0100860000005831"
				,"0100860000005830"
				,"0100860000005835"
				,"0100860000005839"
				,"0100860000005829"
				,"0100860000005843"
				,"0100860000005858"
				,"0100860000005834"
				,"0100860000005842"
				,"0100860000005844"
				,"0100860000005837"
				,"0100860000005859"
				,"0100860000005841"
				,"0100860000005846"
				,"0100860000005845"
				,"0100860000005838"
				,"0100860000005836"
				,"0100860000005840"
				,"0100860000005833"
				,"0100860000005739"
				,"0100860000005732"
				,"0100860000005725"
				,"0100860000005733"
				,"0100860000005730"
				,"0100860000005727"
				,"0100860000005735"
				,"0100860000005728"
				,"0100860000005724"
				,"0100860000005734"
				,"0100860000005729"
				,"0100860000005726"
				,"0100860000005731"
				,"0100860000005738"
				,"0100860000005736"
				,"0100860000005720"
				,"0100860000005737"
				,"0100860000005723"
				,"0100860000005721"
				,"0100860000005722"
				,"0100860000005762"
				,"0100860000005757"
				,"0100860000005760"
				,"0100860000005761"
				,"0100860000005750"
				,"0100860000005755"
				,"0100860000005751"
				,"0100860000005765"
				,"0100860000005758"
				,"0100860000005767"
				,"0100860000005763"
				,"0100860000005768"
				,"0100860000005754"
				,"0100860000005766"
				,"0100860000005852"
				,"0100860000005764"
				,"0100860000005752"
				,"0100860000005756"
				,"0100860000005850"
				,"0100860000005777"
				,"0100860000005782"
				,"0100860000005785"
				,"0100860000005784"
				,"0100860000005778"
				,"0100860000005776"
				,"0100860000005769"
				,"0100860000005771"
				,"0100860000005770"
				,"0100860000005773"
				,"0100860000005788"
				,"0100860000005772"
				,"0100860000005786"
				,"0100860000005787"
				,"0100860000005779"
//				,"0100860000005773"
				,"0100860000005781"
				,"0100860000005780"
				,"0100860000005783"
				,"0100860000005793"
				,"0100860000005869"
				,"0100860000005791"
				,"0100860000005864"
				,"0100860000005790"
				,"0100860000005795"
				,"0100860000005794"
				,"0100860000005871"
				,"0100860000005862"
				,"0100860000005861"
				,"0100860000005863"
				,"0100860000005792"
				,"0100860000005867"
				,"0100860000005789"
				,"0100860000005865"
				,"0100860000005860"
				,"0100860000005866"
				,"0100860000005870"
				,"0100860000005746"
				,"0100860000005747"
				,"0100860000005740"
				,"0100860000005745"
				,"0100860000005744"
				,"0100860000005851"
				,"0100860000005748"
				,"0100860000005741"
				,"0100860000005743"
				,"0100860000005742"
				,"0100860000005855"
				,"0100860000005853"
				,"0100860000005759"
				,"0100860000005854"
				,"0100860000005848"};
		List<String> sqls = new ArrayList<String>();
		StringBuffer sqlIn = new StringBuffer();
		sqlIn.append("''");
		for(int i=0;i<routers.length;i++){
			sqlIn.append(",'"+routers[i]+"'");
		}
		System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<Record> recs = Db.find("select router_sn from bp_device where router_sn in ("+sqlIn+")");
		List<String> existRouter = new ArrayList<String>();
		Iterator<Record> ite = recs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			existRouter.add(rec.getStr("router_sn"));
			System.err.println("'"+rec.getStr("router_sn")+"',");
		}
		System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		for(int i=0;i<routers.length;i++){
			if(!existRouter.contains(routers[i])){
				sqls.add("insert into bp_device(name,time_out,router_sn,remote_account,remote_pass,max_access_num,create_date,init_step,type,lng,lat,location) values('QingLongMall-FREE',120,'"+routers[i]+"','"+routers[i]+"@pifii.com','2014@pifii.com-yinfu',20,now(),'0','2',114.394055,27.80146,江西省, 宜春市, 袁州区, 东风大街, 248号) ");
				System.err.println("'"+routers[i]+"',");
				existRouter.add(routers[i]);
			}
		}
		if(sqls.size() > 0){
			Db.batch(sqls, sqls.size() +1);
		}
	}
	
	private static void deleteRepeatDevice(){
		List<Record> device = Db.find("select min(id) id,router_sn from bp_device group by router_sn having count(*)>1");
		List<String> sqls = new ArrayList<String>();
		Iterator<Record> ite = device.iterator();
		while(ite.hasNext()){
			Record record = ite.next();
			String sql = "delete from bp_device where router_sn='"+record.getStr("router_sn")+"' and id > "+record.get("id").toString();
			sqls.add(sql);
		}
		Db.batch(sqls, sqls.size());
	}
	
	private static void insertMacBlackList(){
		String[] macs = new String[]{
		"B4-B6-76-70-BA-D4",
		"FC-1F-19-FA-FE-78",
		"FC-1F-19-FA-D1-42",
		"FC-1F-19-FA-FE-68",
		"B4-B6-76-71-40-49",
		"FC-1F-19-FA-FB-A4",
		"FC-1F-19-FA-FC-EE",
		"FC-1F-19-FA-FB-18",
		"FC-1F-19-FE-F4-54",
		"FC-1F-19-FA-FF-BA",
		"1C-3E-84-30-24-33",
		"70-18-8B-58-D9-A9",
		"FC-1F-19-FA-FE-A6",
		"FC-1F-19-FB-10-1A",
		"FC-1F-19-F8-B6-FC",
		"B8-76-3F-8C-3B-F3",
		"B8-76-3F-16-95-A9",
		"B8-76-3F-6A-77-33",
		"1C-3E-84-74-80-CF",
		"1C-3E-84-2F-F4-83",
		"B8-76-3F-8C-27-C7",
		"1C-3E-84-2F-FE-33",
		"1C-3E-84-74-B2-3D",
		"B8-76-3F-4E-28-3D",
		"B8-76-3F-4E-14-8B",
		"B8-76-3F-6A-63-C5",
		"B8-76-3F-16-AC-91",
		"B8-76-3F-4E-2D-6F",
		"B8-76-3F-16-91-DF",
		"BB-76-3F-16-92-01",
		"FC-1F-19-F8-B8-DA",
		"78-61-7C-56-93-AE",
		"78-61-7C-57-17-66",
		"78-61-7C-56-EA-7A",
		"78-61-7C-63-0D-A6",
		"78-61-7C-56-65-AA",
		"78-61-7C-57-16-C8",
		"78-61-7C-63-32-2A",
		"78-61-7C-57-23-64",
		"78-61-7C-56-A9-2E",
		"78-61-7C-56-CE-F0",
		"78-61-7C-57-16-7C",
		"78-61-7C-56-93-B4",
		"78-61-7C-57-17-A8",
		"78-61-7C-57-17-06",
		"78-61-7C-56-AA-EA",
		"78-61-7C-63-68-94",
		"78-61-7C-56-65-96",
		"78-61-7C-63-51-DA",
		"78-61-7C-56-19-18",
		"78-61-7C-63-00-E4",
		"78-61-7C-56-CE-EA",
		"78-61-7C-57-17-50",
		"78-61-7C-57-17-68",
		"78-61-7C-63-55-48",
		"78-61-7C-63-21-E0",
		"78-61-7C-56-CE-AE",
		"78-61-7C-57-16-40",
		"78-61-7C-57-18-10",
		"78-61-7C-56-96-22",
		"78-61-7C-56-AA-BC",
		"78-61-7C-63-69-54"
		};
		List<String> sqls = new ArrayList<String>();
		for(int i=0;i<macs.length;i++){
			String id = UUID.randomUUID().toString();
			String thisMac = macs[i].toLowerCase().replaceAll("-", ":");
			sqls.add("insert into bp_shop_blacklist(id,shop_id,type,tag,create_date) values('"+id+"',5,'mac','"+thisMac+"',now()) ");
		}
		Db.batch(sqls, sqls.size());
	}
	
	/**
	 * 初始化商铺的认证方式，之前的商铺如果没有配置就不会有认证方式
	 */
	private static void initShopAuthTypes(){
		List<Record> shops = Db.find("select id from bp_shop where delete_date is null ");//所有使用的商铺
		System.err.println(shops.size());
		List<Record> authTypes = Db.find("select id from bp_auth_type where is_used ");//所有可用的认证方式
		if(shops.size() > 0 && authTypes.size() > 0){
			Object[][] params = new Object[shops.size()*authTypes.size()][3];
			for(int i=0;i<shops.size();i++){
				Object shopId = shops.get(i).get("id");
				for(int j=0;j<authTypes.size();j++){
					Object authTypeId = authTypes.get(j).get("id");
					params[i*authTypes.size()+j][0] = UUID.randomUUID().toString();
					params[i*authTypes.size()+j][1] = shopId;
					params[i*authTypes.size()+j][2] = authTypeId;
				}
			}
			Db.batch("insert into bp_auth_setting(id,shop_id,auth_type_id,create_date) values(?,?,?,now()) ", params, params.length);
		}
		
	}
	
	private static void initShopGroup(){
		List<String> sqls = new ArrayList<String>();
		List<Record> shops = Db.find("select distinct owner from bp_shop");//初始化商铺数据
		Iterator<Record> ite = shops.iterator();
		while(ite.hasNext()){
			Record shop = ite.next();
			Object owner = shop.get("owner");
			String id = UUID.randomUUID().toString();
			sqls.add("insert into bp_shop_group (id,user_id,name,access_delete,create_date) "
					+ "values('"+id+"','"+owner+"','默认分组',0,now())");
			sqls.add("update bp_shop set group_id='"+id+"' where owner ='"+owner+"'");
		}
		Db.batch(sqls, sqls.size()+1);
	}
	
	/**
	 * 初始化管理员的广告编辑及分配权限
	 */
	private static void initAdvOrg(){
		List<String> sqls = new ArrayList<String>();
		List<Record> orgs = Db.find("select id from sys_org where pid is null");
		
		List<String> adminAdvOrgsStr = new ArrayList<String>();
		List<Record> adminAdvOrgs = Db.find("select ifnull(adv_spaces,'') adv_spaces,ifnull(org_id,'') org_id from bp_adv_org ");
		Iterator<Record> iteOrg = adminAdvOrgs.iterator();
		while(iteOrg.hasNext()){
			Record rec = iteOrg.next();
			adminAdvOrgsStr.add(rec.getStr("adv_spaces")+"_"+rec.getStr("org_id"));
		}
		
		List<Record> advs = Db.find("select id from bp_adv_spaces");//初始化商铺数据
		Iterator<Record> ite = advs.iterator();
		while(ite.hasNext()){
			Record adv = ite.next();
			Object advSpaces = adv.get("id");
			for(int i=0;i<orgs.size();i++){
				Record org = orgs.get(i);
				if(!adminAdvOrgsStr.contains(advSpaces.toString()+"_"+org.get("id").toString())){
					String id = UUID.randomUUID().toString();
					sqls.add("insert into bp_adv_org(id,adv_spaces,org_id,create_date) "
							+ "values('"+id+"','"+advSpaces.toString()+"',"+org.get("id").toString()+",now())");
				}
			}
		}
		if(sqls.size() > 0){
			Db.batch(sqls, sqls.size()+1);
		}
	}
	
	/**
	 * 将旧广告表中的数据移到新广告表中
	 */
	private static void initNewAdv(){
		Map<String,Object> advIds = new HashMap<String,Object>();
		List<Record> advTypes = Db.find("select id,adv_index from bp_adv_type where template_id=1 and adv_type='adv'");
		Iterator<Record> typesIte = advTypes.iterator();
		while(typesIte.hasNext()){
			Record rowData = typesIte.next();
			advIds.put(rowData.get("adv_index").toString(), rowData.get("id").toString());
		}
		
		List<String> sqls = new ArrayList<String>();
		List<Record> advs = Db.find("select shop_id,image,link,des,create_date,serial from bp_adv");
		Iterator<Record> ite = advs.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			String advTypeId = advIds.get(rowData.get("serial").toString()).toString();
			String id = UUID.randomUUID().toString();
			String img = null == rowData.get("image")?"":rowData.get("image").toString();
			String link = null == rowData.get("link")?"":rowData.get("link").toString();
			String des = null == rowData.get("des")?"":rowData.get("des").toString();
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rowData.getDate("create_date"));
			
			String sql1 = "insert into bp_adv_content(id,adv_type_id,name,img,link,des,create_date,update_date) "
					+ "values('"+id+"','"+advTypeId+"','广告"+rowData.get("serial").toString()+"','"+
					img+"','"+link+"','"+des+"',now(),now())";
			System.err.println(sql1);
			sqls.add(sql1);
			String sql2 = "insert into bp_adv_shop(id,shop_id,adv_type_id,content_id,update_by_shop,create_date) "
					+ "values('"+UUID.randomUUID().toString()+"','"+rowData.get("shop_id").toString()+"','"+advTypeId+"','"+
					id+"',1,'"+date+"')";
			System.err.println(sql2);
			sqls.add(sql2);
		}
		Db.batch(sqls, sqls.size()+1);
	}

	/**
	 * 江西邮政的所有商铺设置模板2
	 */
	private static void setTemp(){
		List<Record> allPages = Db.find("select shop_id from bp_shop_page");
		List<Integer> allPageShops = new ArrayList<Integer>();
		Iterator<Record> ite = allPages.iterator();
		while(ite.hasNext()){
			Record thisShop = ite.next();
			allPageShops.add(thisShop.getInt("shop_id"));
		}
		List<String> sqls = new ArrayList<String>();
		List<Record> myShops = Db.find("select shop_id from bp_adv_shop where content_id='25dd72d1-0dc4-4009-aab9-03252fb23a3d' ");
		Iterator<Record> myIte = myShops.iterator();
		while(myIte.hasNext()){
			Record thisShop = myIte.next();
			Integer thisShopId = thisShop.getInt("shop_id");
			if(!allPageShops.contains(thisShopId)){
				sqls.add("insert into bp_shop_page(shop_id,template_id,step,create_date,update_date) values("+thisShopId+",13,1,now(),now())");
			}
		}
		if(sqls.size() > 0){
			int[] changs = Db.batch(sqls, sqls.size() +1);
			System.err.println("更改的数据"+changs.length+"行。");
		}
	}
	
	private static List<Record> getPostDevice(){
		List<Record> orgs = DataOrgUtil.getChildrens(3, true);
		List<Record> devices = Db.find("select d.router_sn from bp_device d join bp_shop s on (d.shop_id=s.id) where s.org_id in ("+DataOrgUtil.recordListToSqlIn(orgs, "id")+") ");
		System.err.println(devices.size());
		System.err.println(DataOrgUtil.recordListToSqlIn(devices, "router_sn"));
		Iterator<Record> ite = devices.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			System.err.println(rec.getStr("router_sn"));
		}
		return devices;
	}
	
	private static void setPostShopIcon(){
		List<Record> orgs = DataOrgUtil.getChildrens(3, true);
		int changeRows = Db.update("update bp_shop set icon='upload/image/shop/1421641220109.jpg' where icon is null and org_id in ("+DataOrgUtil.recordListToSqlIn(orgs, "id")+")");
		System.err.println(changeRows);
	}
	
	private static void setPostalShopName(){
		String namePrefix = "江西省邮政公司";
		List<Record> orgs = DataOrgUtil.getChildrens(3, true);
		List<String> sqls = new ArrayList<String>();
		List<Record> shops = Db.find("select id,ifnull(name,'') name from bp_shop where org_id in ("+DataOrgUtil.recordListToSqlIn(orgs, "id")+") ");
		Iterator<Record> ite = shops.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			String thisName = rec.getStr("name");
			if(!thisName.startsWith(namePrefix)){
				System.err.println("需要更新的商铺："+rec.get("id").toString()+"		"+thisName);
				sqls.add("update bp_shop set name='"+namePrefix+"("+thisName+")' where id="+rec.get("id").toString());
			}else{
				System.err.println("不需要更新的商铺："+thisName);
			}
		}
		if(sqls.size() > 0){
			Db.batch(sqls, sqls.size());
			System.err.println("更新了"+sqls.size()+"个商铺");
		}else{
			System.err.println("没有需要更新的商铺");
		}
	}
	
}
