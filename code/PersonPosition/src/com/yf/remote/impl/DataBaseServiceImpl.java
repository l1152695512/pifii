package com.yf.remote.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Map;

import com.yf.remote.service.DataBaseDTO;
import com.yf.remote.service.DataBaseService;
import com.yf.util.DBUtil;
import com.yf.util.FormatUtils;
import com.yf.util.HibernateUUId;

public class DataBaseServiceImpl extends UnicastRemoteObject implements DataBaseService{
	

	//远程对象的无参构造器是必须的，可能被使用来动态生成实例
	public DataBaseServiceImpl() throws RemoteException {
		super();
	}
	
	@Override
	public int saveDataService(DataBaseDTO dataBaseDTO) throws RemoteException, Exception {
		int i = 0;
		dataBaseDTO.setDb_id(new HibernateUUId().generate().toString());
		dataBaseDTO.setCreate_time(new java.sql.Timestamp(new Date().getTime()));
		Map dataBaseMap = FormatUtils.convertBean(dataBaseDTO);
		//新增
		final String inserSql = DBUtil.ctIstSl("BP_DATA_BASE_TBL", dataBaseMap);
		boolean bool = new DBUtil().insert(inserSql, DBUtil.delNullArray(dataBaseMap.values().toArray()));	
		if(!bool){
			i = -1;
		}
		return i;
	}
}
