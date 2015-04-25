package com.yf.ws;

import java.util.Date;
import java.util.Map;

import javax.jws.WebService;

import com.yf.remote.service.DataBaseDTO;
import com.yf.util.DBUtil;
import com.yf.util.FormatUtils;
import com.yf.util.HibernateUUId;
import com.yf.ws.domain.CallResult;

@WebService
public class DataBaseServiceImpl implements DataBaseService {
	
	public CallResult saveDataBase(DataBaseDTO dataBaseDTO) {
		CallResult callResult = new CallResult();
		dataBaseDTO.setDb_id(new HibernateUUId().generate().toString());
		dataBaseDTO.setCreate_time(new java.sql.Timestamp(new Date().getTime()));
		Map dataBaseMap;
		try {
			dataBaseMap = FormatUtils.convertBean(dataBaseDTO);
			//新增
			final String inserSql = DBUtil.ctIstSl("BP_DATA_BASE_TBL", dataBaseMap);
			boolean bool = new DBUtil().insert(inserSql, DBUtil.delNullArray(dataBaseMap.values().toArray()));	
			if(!bool){
				callResult.setReturnCode(-1);
				callResult.setReturnMessage("保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callResult; 
	}
}
