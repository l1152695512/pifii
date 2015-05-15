package com.yinfu.plugin.quzrtz.adv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.jbase.util.PropertyUtils;

/**
 * 按照用户发布的广告策略，定时更新吸顶的广告及下发盒子的广告更新内容；
 * 
 * 涉及到的表：
 * bp_adv_putin				广告投放策略
 * bp_adv_putin_daytime		广告投放策略中的时间选择
 * bp_adv_putin_industry	广告投放策略中的行业选择
 * bp_adv_putin_org			广告投放策略中的区域选择
 * 
 * bp_adv_shop				当前商铺使用的广告（按照策略中的广告对比当前使用的广告，如果当前使用的广告和策略中即将使用的广告不一致，则更改或者生成更新包）
 * 
 * bp_adv_org				广告位的组织权限分配
 * 
 * bp_adv_content			广告表
 * .....
 *
 * 思路：定时检查当前时间处于投放时间内的投放广告，对比该投放策略对应的商铺使用的广告和投放的广告是否一致，如果不一致，则更新商铺的广告（如果是盒子则下发广告更新包）
 *
 */
public class UpdatePutinAdvJob implements Job {
	private static Logger logger = Logger.getLogger(UpdatePutinAdvJob.class);
	private static int runningThreadNum = 0;
	private static boolean jobIsRunning = false;
	private Map<String,String> effectiveContentIdMap = new HashMap<String,String>();//某个组织某个广告位生效的广告，将查找到的广告存放在这里，下次有同样组织的商铺广告位时不用再去查找
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(!jobIsRunning){
			System.err.println("开始>>>>>>>>>>>>>>>>>>定时更新广告");
			jobIsRunning = true;
			try{
				Map<String,Map<String,String>> effectivePutinAdv = getEffectivePutinAdv();
				if(effectivePutinAdv.size() > 0){
					Map<String,String> orgRelation = orgRelation();
					Map<String,List<String>> advSpaceOrgPermission = getAdvSpaceOrgPermission();
					
					List<Record> orgsId = new ArrayList<Record>();
					Iterator<String> ite = effectivePutinAdv.keySet().iterator();
					while(ite.hasNext()){
						orgsId.add(new Record().set("id", ite.next()));
					}
					Map<String,Map<String,List<Record>>> updateAdvContentShops = new HashMap<String,Map<String,List<Record>>>();//Map<商铺ID,Map<广告位类型,List<需要更新的广告位上的广告（包含广告位ID及广告ID）>>>，存放更新了广告的商铺，遍历该Map，对该商铺下的盒子下发更新包任务（吸顶不需要）
					List<Record> shopsAdv = getOrgShops(orgsId);
					Iterator<Record> shopsIte = shopsAdv.iterator();
					List<Object[]> insertShopAdvs = new ArrayList<Object[]>();
					while(shopsIte.hasNext()){//循环投放的广告在当前时间受影响的商铺，判断商铺实际使用的广告和投放的广告是否一致，不一致则更新
						Record rec = shopsIte.next();
						String thisOrg = rec.get("org_id").toString();
						String thisAdvSpace = rec.get("adv_spaces").toString();
						String thisShopId = rec.get("shop_id").toString();
						String thisContentId = rec.get("content_id").toString();
						String effectiveContentId = checkShopUsedOrgPutinAdv(effectivePutinAdv,orgRelation,advSpaceOrgPermission,thisOrg,thisAdvSpace);
						if(!StringUtils.isBlank(effectiveContentId) && !effectiveContentId.equals(thisContentId)){//如果生效的广告ID既不为空又不等于现在使用的广告，则该商铺需要应用新的广告（对于盒子需要下发更新包）
							System.err.println("需要更新广告（组织ID："+thisOrg+"，商铺ID："+thisShopId+"，广告位ID："+thisAdvSpace+"，原广告ID："+thisContentId+"，生效的广告ID："+effectiveContentId+"）");
							if(StringUtils.isBlank(rec.get("shop_adv_id").toString())){
								String thisId = UUID.randomUUID().toString();
								insertShopAdvs.add(new Object[]{thisId,thisShopId,thisAdvSpace});
							}
							Map<String,List<Record>> advSpaceType = updateAdvContentShops.get(thisShopId);
							if(null == advSpaceType){
								advSpaceType = new HashMap<String,List<Record>>();
								updateAdvContentShops.put(thisShopId, advSpaceType);
							}
							List<Record> updateAdvContents = advSpaceType.get(rec.get("adv_type").toString());
							if(null == updateAdvContents){
								updateAdvContents = new ArrayList<Record>();
								advSpaceType.put(rec.get("adv_type").toString(), updateAdvContents);
							}
							updateAdvContents.add(new Record().set("adv_space", thisAdvSpace).set("adv_content_id", effectiveContentId));
						}else{
//							System.err.println("不需要更新广告（组织ID："+thisOrg+"，商铺ID："+thisShopId+"，广告位ID："+thisAdvSpace+"，原广告ID："+thisContentId+"，生效的广告ID："+effectiveContentId+"）");
						}
					}
					if(insertShopAdvs.size() > 0){//插入商铺对应广告位上的广告（如果不插入，在多线程更改商铺广告时不会起作用）
						Db.batch("insert into bp_adv_shop(id,shop_id,adv_spaces,content_id,update_by_shop,create_date) values(?,?,?,'',0,now()) ", DataOrgUtil.listTo2Array(insertShopAdvs), insertShopAdvs.size());
					}
					//下面的代码使用多线程去下发盒子的更新（需要更新的内容包括商铺的广告以及盒子的下发内容），这个过程要放在一个事务里，在不成功时回退，下次可继续生成任务
					int maxThreadNum = PropertyUtils.getPropertyToInt("adv.update.thread.number", 10);
					while(true){
						if(runningThreadNum < maxThreadNum && updateAdvContentShops.size() > 0){
							Object key = updateAdvContentShops.keySet().toArray()[0];
							updateThreadNum(1);
							new PackageThread(key.toString(),updateAdvContentShops.get(key)).start();;
							updateAdvContentShops.remove(key);
						}else{
							Thread.sleep(200);
						}
						if(runningThreadNum == 0 && updateAdvContentShops.size() == 0){
							break;
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.warn("定时更新广告异常", e);
			}finally{
				jobIsRunning = false;
			}
			System.err.println("结束>>>>>>>>>>>>>>>>>>定时更新广告");
		}
	}
	
	public static synchronized void updateThreadNum(int num){
		runningThreadNum = runningThreadNum+num;
	}
	
	/**
	 * 
	 * @param effectivePutinAdv			当前时间生效的投放策略，Map<组织ID,Map<广告位ID,包含广告ID和广告位类型>>
	 * @param orgRelation				组织的层级关系，Map<当前组织id,父组织id>
	 * @param advSpaceOrgPermission		获取组织已授权的广告位，Map<组织ID,List<广告位ID>>
	 * @param org
	 * @param advSpace
	 * @return 
	 * 		""		代表该广告位上的广告不用更新
	 * 		"广告ID"	代表该广告位上的广告应该更新为指定id的广告
	 * 
	 * 查找某个商铺在当前时间应该投放的广告策略
	 * 查找条件：
	 * 		1.如果要查找的商铺的组织已分配了该广告位权限，则不需要更新该广告位上的广告
	 * 		2.向父组织查找第一个分配了该广告位的组织，如果该组织投放了广告，则使用投放的广告，如果没有投放，则返回空，
	 * 			如果实际需求是向父组织查找第一个配置的广告，此时就应该向父组织查找，直到查找到一个配置的广告
	 */
	private String checkShopUsedOrgPutinAdv(Map<String,Map<String,String>> effectivePutinAdv,Map<String,String> orgRelation,
			Map<String,List<String>> advSpaceOrgPermission,String org,String advSpace){
		if(effectiveContentIdMap.containsKey(org+"*"+advSpace)){//如果该商铺在之前已检查过生效的广告，则直接使用
			return effectiveContentIdMap.get(org+"*"+advSpace);
		}else{
			String thisContentId = "";
			if(null == advSpaceOrgPermission.get(org) || !advSpaceOrgPermission.get(org).contains(advSpace)){//如果要查找的商铺的组织没有授权该广告位（商铺没有编辑该广告位的权限），则需要检查更新该广告位的广告
				String checkOrg = org;
				while(true){
					String parentOrg = orgRelation.get(checkOrg);//获取父组织
					if(null == parentOrg){
						break;
					}
					if(null != advSpaceOrgPermission.get(parentOrg) && advSpaceOrgPermission.get(parentOrg).contains(advSpace)){//如果父组织分配了该广告位，不管此时子组织有没有配置广告，都返回，不再查找
						Map<String,String> thisOrgSpaceAdvContentIds = effectivePutinAdv.get(checkOrg);//获取当前组织已配置的广告策略
						if(null != thisOrgSpaceAdvContentIds && null != thisOrgSpaceAdvContentIds.get(advSpace)){
							thisContentId = thisOrgSpaceAdvContentIds.get(advSpace);//查询当前组织在该广告位上所配置的广告策略
						}
						break;
					}
					checkOrg = parentOrg;
				}
			}
			effectiveContentIdMap.put(org+"*"+advSpace, thisContentId);
			return thisContentId;
		}
	}
	/**
	 * 获取指定组织下的所有商铺
	 * @param orgsId
	 * @return
	 */
	private List<Record> getOrgShops(List<Record> orgsId){
		StringBuffer sqlIn = new StringBuffer("''");
		Map<Object,List<Record>> orgs = DataOrgUtil.getChildrensForOrgs(orgsId,true);
		List<String> orgsList = new ArrayList<String>();
		Iterator<Object> ite = orgs.keySet().iterator();
		while(ite.hasNext()){
			Object key = ite.next();
			List<Record> childrens = orgs.get(key);
			Iterator<Record> iteChildren = childrens.iterator();
			while(iteChildren.hasNext()){
				Record rec = iteChildren.next();
				if(!orgsList.contains(rec.get("id").toString())){
					sqlIn.append(",'"+rec.get("id").toString()+"'");
					orgsList.add(rec.get("id").toString());
				}
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select bs.id shop_id,bs.org_id,basp.id adv_spaces,basp.adv_type,ifnull(bas.id,'') shop_adv_id,ifnull(bas.content_id,'') content_id ");
		sql.append("from bp_shop bs join bp_adv_spaces basp on (bs.org_id in ("+sqlIn.toString()+")) ");
		sql.append("left join bp_adv_shop bas on (bas.shop_id=bs.id and bas.adv_spaces=basp.id) ");
		List<Record> shopsAdv = Db.find(sql.toString());
		return shopsAdv;
	}
	
	/**
	 * 获取组织已授权的广告位
	 * @return
	 * Map<String,List<String>>		Map<组织ID,List<广告位ID>>
	 */
	private Map<String,List<String>> getAdvSpaceOrgPermission(){
		Map<String,List<String>> orgAdvSpace = new HashMap<String, List<String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select so.id org_id,bao.adv_spaces ");
		sql.append("from sys_org so join bp_adv_org bao on (so.id=bao.org_id)");
		sql.append("order by org_id ");
		List<Record> orgAdvSpaces = Db.find(sql.toString());
		Iterator<Record> ite = orgAdvSpaces.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			List<String> spaces = orgAdvSpace.get(rec.get("org_id").toString());
			if(null == spaces){
				spaces = new ArrayList<String>();
				orgAdvSpace.put(rec.get("org_id").toString(), spaces);
			}
			if(!spaces.contains(rec.get("adv_spaces").toString())){
				spaces.add(rec.get("adv_spaces").toString());
			}
		}
		printAdvSpaceOrgPermission(orgAdvSpace);
		return orgAdvSpace;
	}
	
	/**
	 * 获取组织关系
	 * Map<String,String>		Map<当前组织id,父组织id>
	 * @return
	 */
	private Map<String,String> orgRelation(){
		Map<String,String> orgRelation = new HashMap<String, String>();
		List<Record> orgs = Db.find("select id,ifnull(pid,'') pid from sys_org");
		Iterator<Record> ite = orgs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			orgRelation.put(rec.get("id").toString(), rec.get("pid").toString());
		}
		return orgRelation;
	}
	
	/**
	 * 获取当前生效的投放广告
	 * 
	 * 过滤条件：
	 * 	1.权限过滤：拥有该广告位权限的用户投放的广告（防止取消广告位权限后之前投放的广告依然生效）
	 * 	2.周过滤：投放的广告时间包含当前周的投放（如今天是周一，如果投放的广告包含的日期包含周一，则会查询到该投放策略）
	 * 	3.时间过滤：投放的广告时间包含当前时间的投放（如当前的时间是10：30，如果某个广告的投放时间是今天的10：00到14：00，则会查询到该投放策略）
	 * 	4.组织过滤：已被父组织投放广告的组织，更换组织后，原父组织投放的广告，不再生效（如果组织更换了，要更新bp_adv_putin_org中对应组织的enable为0）
	 * 
	 * return	
	 * Map<String,Map<String,Record>>	Map<组织ID,Map<广告位ID,广告ID>>
	 * 返回在组织上投放的广告,用于对比商铺实际在用的广告和当前应该使用的广告是否一致，如果不一致则更改，并下发更新包（盒子）
	 */
	private Map<String,Map<String,String>> getEffectivePutinAdv(){
		Map<String,Map<String,String>> orgAdvSpace = new HashMap<String,Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select bap.adv_content_id,bap.adv_space,bapo.org_id ");
		sql.append("from bp_adv_putin bap ");
		sql.append("join bp_adv_org bao on (bap.enable and date(now()) between bap.start_date and bap.end_date and bao.edit_able and bao.org_id=bap.org_id and bao.adv_spaces=bap.adv_space) ");//过滤出分配了广告位权限的投放策略（用于开始有权限投放广告时，投放的广告，后来上级组织又取消了权限，这样的投放不应该生效）
		sql.append("join bp_adv_putin_daytime bapd on (bapd.adv_putin_id=bap.id and bapd.week_value=? and (TIME(now()) between bapd.time_start and bapd.time_end or (bapd.time_start>bapd.time_end and (TIME(now())>=bapd.time_start or TIME(now())<=bapd.time_end)))) ");//过滤出当前时间在投放策略中的广告
		sql.append("join bp_adv_putin_org bapo on (bapo.enable and bapo.adv_putin_id=bap.id) ");//组织过滤
		sql.append("order by bap.create_date desc ");//组织过滤
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		List<Record> datas = Db.find(sql.toString(), new Object[]{now.get(Calendar.DAY_OF_WEEK)-1});
		Iterator<Record> ite = datas.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			String thisOrg = rec.get("org_id").toString();
			String thisAdvSpace = rec.get("adv_space").toString();
			Map<String,String> spaces = orgAdvSpace.get(thisOrg);
			if(null == spaces){
				spaces = new HashMap<String,String>();
				orgAdvSpace.put(thisOrg, spaces);
			}
			if(!spaces.containsKey(thisAdvSpace)){
				spaces.put(thisAdvSpace, rec.get("adv_content_id").toString());
			}
		}
		printEffectivePutinAdv(orgAdvSpace);
		return orgAdvSpace;
	}
	
	private void printEffectivePutinAdv(Map<String,Map<String,String>> orgAdvSpace){
		Iterator<String> iteOrg = orgAdvSpace.keySet().iterator();
		while(iteOrg.hasNext()){
			String orgId = iteOrg.next();
			Map<String,String> spacesMap = orgAdvSpace.get(orgId);
			Iterator<String> iteSpaces = spacesMap.keySet().iterator();
			while(iteSpaces.hasNext()){
				String spaceId = iteSpaces.next();
				System.err.println("组织ID："+orgId+"--------广告位ID："+spaceId+"------广告ID："+spacesMap.get(spaceId));
			}
		}
	}

	private void printAdvSpaceOrgPermission(Map<String,List<String>> orgAdvSpace){
		Iterator<String> ite = orgAdvSpace.keySet().iterator();
		while(ite.hasNext()){
			String orgId = ite.next();
			System.err.println("组织ID："+orgId+"------已授权的广告位ID："+Arrays.toString(orgAdvSpace.get(orgId).toArray()));
		}
	}
}
