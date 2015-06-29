package com.uradiosys.www;

import java.rmi.RemoteException;

import com.uradiosys.www.ServiceApiStub.GetTagStatus;
import com.uradiosys.www.ServiceApiStub.GetTagStatusResponse;
import com.uradiosys.www.ServiceApiStub.GetTagStatusResult;

public class MainTest {
	public static void main(String[] args) throws RemoteException {
		 
		 ServiceApiStub serviceApiStub= new ServiceApiStub();
		 GetTagStatus object = new GetTagStatus();
//		 object.setTagMac("B0:8E:1A:31:03:3C");
		 object.setTagMac("B0:8E:1A:30:0F:4B");
		 GetTagStatusResponse response = serviceApiStub.GetTagStatus(object);
		 GetTagStatusResult result = response.getGetTagStatusResult();
		 System.err.println(result.getX()+"------"+result.getY());

		// 使用RPC方式调用WebService
//		RPCServiceClient serviceClient = null;
//		try {
//			serviceClient = new RPCServiceClient();
//			Options options = serviceClient.getOptions();
//			// 指定调用WebService的URL
//			EndpointReference targetEPR = new EndpointReference(
//					"http://www.uradiosys.com:8092/ServiceApi.asmx");
//			options.setTo(targetEPR);
////			options.setAction("http://www.uradiosys.com:8092/ServiceApi.asmx");
//			// 指定sayHello方法的参数值
//			Object[] opAddEntryArgs = new Object[] { "B0:8E:1A:31:03:3C" };
//			// 指定sayHello方法返回值的数据类型的Class对象
//			Class[] classes = new Class[] { String.class };
//			// 指定要调用的sayHello方法及WSDL文件的命名空间
//			QName opAddEntry = new QName("http://www.uradiosys.com:8092",
//					"GetTagStatus");
//			// 调用sayHello方法并输出该方法的返回值
//			String result = serviceClient.invokeBlocking(opAddEntry,
//					opAddEntryArgs, classes)[0].toString();
//			serviceClient.cleanupTransport(); // 为了防止连接超时
//			System.out.println(result);
//			// System.out.println(serviceClient.invokeBlocking(opAddEntry,
//			// opAddEntryArgs, classes)[0]);
//		} catch (AxisFault e) {
//			e.printStackTrace();
//		}
		 
	}
}
