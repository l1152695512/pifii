package pifii.com.log.udp;

import java.net.SocketAddress;
import java.sql.Connection;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import pifii.com.log.util.DBUtilApache;
import pifii.com.log.util.Log;
import pifii.com.log.util.ResourcesUtil;

public class LogServerHandler extends IoHandlerAdapter {

	static final Logger log=Log.logn();
	private LogServer server;

	
	public LogServerHandler(LogServer logServer) {
		this.server = logServer;
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		session.close(true);
	}
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String expression = message.toString();
		log.debug("日志上报"+message);
		String[] logArray = expression.split("witfii:");
		if (logArray.length >= 2) {
			     String[] logs = logArray[1].split("\\|");
			     	if (logs.length >= 3) {
			    	String pifiiname = logs[0].trim();
			        String sn = logs[1].trim();
					String mac=logs[2].trim();//添加判断去除 无用的00:00:00:00:00MAC地址
			        String url = logs[4].trim();
			        String type = logs[3].trim();
			        int index = type.indexOf("Android");
			        boolean isMobile = false;
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
			        
//	          Connection conn =DBUtilApache.openConn(ResourcesUtil.getVbyKey("dbtype"), ResourcesUtil.getVbyKey("dbbaseurl"), ResourcesUtil.getVbyKey("dbport"), ResourcesUtil.getVbyKey("dbname"), ResourcesUtil.getVbyKey("dbusername"), ResourcesUtil.getVbyKey("dbpassword"));
//		      String s="insert into bpbaselogtbl(device_no,type,input_mac,ip,link,create_date) values ('"+sn+"','"+type+"','"+mac+"','"+0+"','"+url+"',now()) ";
//		      DBUtilApache.update(conn, s, null); 
			        
			      //过滤 image css js
			        
			        log.error(expression);
			        
			      }
		}
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Session closed...");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("Session created...");
		SocketAddress remoteAddress = session.getRemoteAddress();
		System.out.println(remoteAddress);
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("Session idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("Session Opened...");
		SocketAddress remoteAddress = session.getRemoteAddress();
		System.out.println(remoteAddress);
	}
}