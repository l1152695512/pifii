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
	  
	  public LogServer() throws IOException { NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
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
	    dcfg.setSendBufferSize(1024);
	    dcfg.setReuseAddress(true);
	    acceptor.bind(new InetSocketAddress(PORT));
	    System.out.println("UDPServer listening on port " + PORT);
	  }
	  
	  public static void main(String[] args) throws IOException {
	    new LogServer();
	  }
	  
/*	private static Logger log = LoggerFactory.getLogger(LogServer.class);

public static void main(String[] args) throws Exception {
	SocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime()
			.availableProcessors() + 1);// tcp/ip 接收者
	DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();// 过滤管道
	chain.addLast("logger", new LoggingFilter());
	chain.addLast("threadPool",
			new ExecutorFilter(Executors.newCachedThreadPool()));
	acceptor.getSessionConfig().setUseReadOperation(true);
	acceptor.getSessionConfig().setReadBufferSize(
			9024000);// 发送缓冲区10M
	acceptor.getSessionConfig().setReceiveBufferSize(
			9024000);// 接收缓冲区10M
	// acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,JConstant.IDEL_TIMEOUT);//
	// 读写通道10s内无操作进入空闲状态
	acceptor.setReuseAddress(true);
	acceptor.setHandler(new LogServerHandler(null));// 设置handler
	int port=Integer.parseInt(ResourcesUtil.getVbyKey("port"));
	acceptor.bind(new InetSocketAddress(port));// 设置端口
	log.debug(String.format("Server Listing on %s",port));
}
*/


}