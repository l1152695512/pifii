package com.pifii.test;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pifii.dao.CutTableJdbc;
import com.pifii.util.DateUtil;
import com.pifii.util.ResourcesUtil;
public class Table extends TimerTask {
	private static final Logger log = LoggerFactory.getLogger(Table.class);
	@Override
	public void run() {
		log.info("分表日期：day :"+DateUtil.getTabCurrentDay());
		String data=DateUtil.getTabCurrentDay();
		String dateYesterday=DateUtil.getTabYester();
		if (!CutTableJdbc.isHere(ResourcesUtil.getVbyKey("db"),ResourcesUtil.getVbyKey("tablename")+data)) {
			log.debug("开始分表+++++++++++++++++++++++++++");
			CutTableJdbc.reamTableName(true, ResourcesUtil.getVbyKey("tablename"), data);
			log.debug("结束分表+++++++++++++++++++++++++++");
			if(CutTableJdbc.isdureHere(ResourcesUtil.getVbyKey("tablename")+data,DateUtil.getTabDay())){/*
				log.debug("表中有其他日期的值+++++++++++++++++++++++++++"+DateUtil.getTabYesterDay());
				List<String> lst=CutTableJdbc.getReplace(ResourcesUtil.getVbyKey("tablename")+data,DateUtil.getTabDay());
				for (String string : lst) {
					System.out.println(ResourcesUtil.getVbyKey("tablename")+data+":中存在的其他日期："+string);
					log.error(ResourcesUtil.getVbyKey("tablename")+data+":中存在的其他日期："+string);
					CutTableJdbc.insertTab(ResourcesUtil.getVbyKey("tablename")+DateUtil.getTabCurrentDay(string),ResourcesUtil.getVbyKey("tablename")+data,string);
					CutTableJdbc.deleteDureTab(ResourcesUtil.getVbyKey("tablename")+data,string);
					String tabData=DateUtil.getTabDay();
					if(!tabData.equals(string)){
						String val=string.replace("-", "_");
						String nval="_"+val;
						//这里是要转移的数据
						//1.查一下有多少条
						Integer num=CutTableJdbc.selectDateTab(ResourcesUtil.getVbyKey("tablename")+data, string);
						System.out.println(num);
						for(int s=0;s<=num;s++){
							CutTableJdbc.insertTabLimit(ResourcesUtil.getVbyKey("tablename")+nval, ResourcesUtil.getVbyKey("tablename")+data, string);
							CutTableJdbc.deleteTabLimit(ResourcesUtil.getVbyKey("tablename")+data, string);
						}
						log.debug("已经删除了表中的其他日期+++++++++++++++++++++++++++");
					}
				}
			*/}	
		}
		
/*		if(CutTableJdbc.isdureHere(ResourcesUtil.getVbyKey("tablename")+data,DateUtil.getTabDay())){
			log.debug("表中有其他日期的值+++++++++++++++++++++++++++"+DateUtil.getTabYesterDay());
			List<String> lst=CutTableJdbc.getReplace(ResourcesUtil.getVbyKey("tablename")+data,DateUtil.getTabDay());
			for (String string : lst) {
				System.out.println(ResourcesUtil.getVbyKey("tablename")+data+":中存在的其他日期："+string);
				log.error(ResourcesUtil.getVbyKey("tablename")+data+":中存在的其他日期："+string);
				CutTableJdbc.insertTab(ResourcesUtil.getVbyKey("tablename")+DateUtil.getTabCurrentDay(string),ResourcesUtil.getVbyKey("tablename")+data,string);
				CutTableJdbc.deleteDureTab(ResourcesUtil.getVbyKey("tablename")+data,string);
				String tabData=DateUtil.getTabDay();
				if(!tabData.equals(string)){
					String val=string.replace("-", "_");
					String nval="_"+val;
					//这里是要转移的数据
					//1.查一下有多少条
					Integer num=CutTableJdbc.selectDateTab(ResourcesUtil.getVbyKey("tablename")+data, string);
					System.out.println(num);
					for(int s=0;s<=num;s++){
						CutTableJdbc.insertTabLimit(ResourcesUtil.getVbyKey("tablename")+nval, ResourcesUtil.getVbyKey("tablename")+data, string);
						CutTableJdbc.deleteTabLimit(ResourcesUtil.getVbyKey("tablename")+data, string);
					}
					log.debug("已经删除了表中的其他日期+++++++++++++++++++++++++++");
				}
			}
		}
*/		}
	}

			
						
							
								
									
									

											
													
													
														
															
																
																
																			
																			
																				
																					
																						
																							
																								