package pifii.com.log.udp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import pifii.com.log.util.ResourcesUtil;

public class LogServer {
	  private static final long serialVersionUID = 201506261506L;
	  public static final int PORT = Integer.valueOf(ResourcesUtil.getVbyKey("port")).intValue();
	  public LogServer() throws IOException { 
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
	    acceptor.setHandler(new LogServerHandler(this));
	    Executor threadPool = Executors.newCachedThreadPool();
	    DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
	    chain.addLast("logger", new LoggingFilter());
	    chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
	    chain.addLast("threadPool", new ExecutorFilter(threadPool));
	    DatagramSessionConfig dcfg = acceptor.getSessionConfig();
	    dcfg.setReadBufferSize(94096);
	    dcfg.setMaxReadBufferSize(965536);
	    dcfg.setReceiveBufferSize(91024);
	    dcfg.setSendBufferSize(91024);
	    dcfg.setReuseAddress(true);
	    acceptor.bind(new InetSocketAddress(PORT));
	    System.out.println("派联日志服务启动成功：UDPServer listening on port " + PORT);
	  }
	  public static void main(String[] args) throws IOException {
		  new LogServer();
	  }
}