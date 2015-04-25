package com.yf.remote.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface DataBaseService extends Remote{
	
	public int saveDataService(DataBaseDTO dataBaseDTO) throws RemoteException, Exception;
	
}
