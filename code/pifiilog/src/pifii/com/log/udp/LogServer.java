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

import pifii.com.log.udp.LogServerHandler;
import pifii.com.log.util.ResourcesUtil;

public class LogServer {
	private static final long serialVersionUID = 201506261506L;

	public static final int PORT =Integer.valueOf(ResourcesUtil.getVbyKey("port"));
	public LogServer() throws IOException {
}

	public static void main(String[] args) throws IOException {
		new LogServer();
	}
	
}