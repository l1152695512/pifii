package com.yf.util;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axis2.databinding.types.URI.MalformedURIException;

import com.ctc.uae.framework.isagb.core.MessageException;
import com.ctc.uae.framework.isagb.core.send.SmsMessageSender;
import com.ctc.uae.framework.isagb.core.send.SmsMessageSenderBuilder;
import com.ctc.uae.framework.isagb.req.MessageHeadReq;
import com.ctc.uae.framework.isagb.req.SmsSendReq;
import com.ctc.uae.framework.isagb.rsp.SmsSendRsp;
import com.ctc.uae.framework.platform.util.EncryptMessageUtil;

public class SmsSender {
	
	public static SmsSendRsp send(SmsSendReq sendReq) throws MalformedURIException,RemoteException,MessageException,Exception {
		SmsMessageSender sender = SmsMessageSenderBuilder.build();
		
		MessageHeadReq headReq = new MessageHeadReq();
		headReq.setSi("50");//SP_ID
		headReq.setAccessNumber("1065902082013");//业务接入号
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        headReq.setTimestamp(timeStamp);//时间戳，格式为‘yyyyMMddHHmmssSSS’
        String signature = EncryptMessageUtil.encryptPassword("50", timeStamp, "feniwl29u");//SP标识码，使用MD5对SP_ID+时间戳+SP_PWD进行加密
        headReq.setSignature(signature);
        headReq.setServiceId("CTAttendanceSystem");
        //headReq.setServiceId("TestApp");//应用标示ID
        headReq.setProduct("lsxsms01");//产品表示ID(非验证字段)
        headReq.setAccounting("A0001");//非验证字段
        headReq.setCustomerNumber("C001");//非验证字段
        
        //sendReq.setNotifyURL("zxxx");
        sendReq.setMessageHeadReq(headReq);
      
        SmsSendRsp sendRsp = sender.send(sendReq);
        return sendRsp;
	}

	public static void send(String phone,String msg){
		SmsSendReq sendReq = new SmsSendReq();
		sendReq.setSender("1065902082013189");//发送号码
		sendReq.setReceivers(new String[]{phone});//接受者号码，暂时只支持电信号码
		sendReq.setContent(msg);//消息内容
		sendReq.setTargetURL("http://125.88.75.200:8080/osg/services/SmsService");//短信网关接口url
		sendReq.setCorrelator("CR100000028");
		try {
			SmsSender.send(sendReq);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] src){
		SmsSender.send("18922177086","test messsage!!");
	}
	
}