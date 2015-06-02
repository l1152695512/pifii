package pifii.com.log.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
/**
 * @说明 Mina TCP客户端
 * @version 1.0
 * @since
 */
/**
 * @说明 Mina TCP客户端
 * @version 1.0
 * @since
 */
public class UDPClient  {

	public static void main(String[] args) {
		
		// 创建客户端连接器.
		  NioSocketConnector connector = new NioSocketConnector();
		  connector.getFilterChain().addLast("logger", new LoggingFilter());
		  connector.getFilterChain().addLast(
		    "codec",
		    new ProtocolCodecFilter(new TextLineCodecFactory(Charset
		      .forName("UTF-8")))); // 设置编码过滤器
//		  connector.setConnectTimeout(30);
		  connector.setHandler(new UDPHandler());// 设置事件处理器
		  ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.1.7",
		    10000));// 建立连接
		  cf.awaitUninterruptibly();// 等待连接创建完成
		  cf.getSession().write("hello");// 发送消息
		  cf.getSession().write("quit");// 发送消息
		  cf.getSession().getCloseFuture().awaitUninterruptibly();// 等待连接断开
		  connector.dispose();
		 }
		
	}
	
	
