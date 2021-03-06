package pifii.com.log.udp;

import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.collections.functors.ExceptionClosure;
import org.apache.commons.dbutils.DbUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.ParseException;

import pifii.com.log.util.DBUtilApache;
import pifii.com.log.util.Log;
import pifii.com.log.util.ResourcesUtil;
import pifii.com.log.util.TableNameFort;

import com.ifidc.common.ApplicationContextHelper;
import com.ifidc.persistent.BpBaseLogTbl;
import com.ifidc.persistent.BpBaseLogTblDAO;
import com.pifii.dao.CutTableJdbc;

public class LogServerHandler extends IoHandlerAdapter {

	static final Logger log = Log.logn();
	private LogServer server;

	public LogServerHandler(LogServer logServer) {
		this.server = logServer;
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		session.close(true);
	}
	static Connection conn=DBUtilApache.openConn("mysql", "localhost", "3306", "test", "root", "ifidc2403");
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		boolean isFlag=true;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ds = df.format(new Date());
		Timestamp tt = null;
		try {
			Date ss = df.parse(ds);
			tt = new Timestamp(ss.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		BpBaseLogTbl bpBaseLogTbl = new BpBaseLogTbl();
		bpBaseLogTbl.setCreateDate(tt);
		String[] logArray = message.toString().split("witfii:");
		if (logArray.length >= 2) {
			String[] logs = logArray[1].split("\\|");
			if (logs.length >= 3) {
				String wifiName = logs[0].trim();
				String mac = logs[1].trim();
				String local = logs[2].trim();//添加判断去除 无用的00:00:00:00:00MAC地址
				String url = logs[4].trim();
				String type = logs[3].trim();
				int index = type.indexOf("Android");
				boolean isMobile = false;
				String urlRep=ResourcesUtil.getVbyKey("url");
				if(url.contains(urlRep)){
					isFlag=false;
				}
				if (index > -1) {
					type = "Android";
					isMobile = true;
				}
				index = type.indexOf("iPhone");
				if (index > -1) {
					type = "iPhone";
					isMobile = true;
				}
				index = type.indexOf("iPad");

				if (index > -1) {
					type = "iPad";
					isMobile = true;
				}
				if (!isMobile) {
					type = "PC";
				}
				bpBaseLogTbl.setDeviceNo(mac.trim());// 设备编号
				bpBaseLogTbl.setInputMac(local.trim());
				bpBaseLogTbl.setLink(url);
				bpBaseLogTbl.setType(type);
				ApplicationContext ctx = ApplicationContextHelper
						.getApplicationContext();
				BpBaseLogTblDAO dao = (BpBaseLogTblDAO) ctx
						.getBean("BpBaseLogTblDAO");
				if(isFlag){
				/*	String tablename=TableNameFort.nowTable();
					//业务逻辑处理
				if(CutTableJdbc.isHere(ResourcesUtil.getVbyKey("dbname"), tablename)){
					DBUtilApache.update(conn, "insert into "+tablename+"(device_no,type,input_mac,ip,link,create_date)  values ('"+mac+"','"+type+"','"+local+",'"+url+"','"+new Date().toLocaleString()+"')" , null);
				}else {
					CutTableJdbc.createtables(tablename);
					DBUtilApache.update(conn, "insert into "+tablename+"(device_no,type,input_mac,ip,link,create_date)  values ('"+mac+"','"+type+"','"+local+",'"+url+"','"+new Date().toLocaleString()+"')" , null);
				}*/
				dao.save(bpBaseLogTbl);
				}
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Session closed...");
		log.debug("断开链接");
	}
	
	private int count=0;
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("Session created...");
		SocketAddress remoteAddress = session.getRemoteAddress();
		System.out.println(remoteAddress);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		log.debug("链接空闲");
		System.out.println("Session idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		count++;
		System.out.println("Session Opened...");
		SocketAddress remoteAddress = session.getRemoteAddress();
		log.debug("新客户端链接："+remoteAddress+"第几个"+count);
		System.out.println(remoteAddress);
	}
}