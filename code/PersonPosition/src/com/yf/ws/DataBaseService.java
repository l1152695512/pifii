package com.yf.ws;

import javax.jws.WebService;

import com.yf.remote.service.DataBaseDTO;
import com.yf.ws.domain.CallResult;

@WebService
public interface DataBaseService {
	public CallResult saveDataBase(DataBaseDTO dataBaseDTO);
}
