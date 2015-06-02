package pifii.com.log.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramSocket;

/**
 *  使用Sender类来代表客户端程序，
 * @author ccna_zhang
 *
 */
public class Sender {

	public static void main(String[] args) {
		String msg = "Hello, World";
		byte[] buf = msg.getBytes();
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1");  //服务器地址
			int port = 10000;  //服务器的端口号
			//创建发送方的数据报信息
			DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, address, port);
			
			DatagramSocket socket = new DatagramSocket();  //创建套接字
			socket.send(dataGramPacket);  //通过套接字发送数据
			
			//接收服务器反馈数据
			byte[] backbuf = new byte[1024];
			DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);
			socket.receive(backPacket);  //接收返回数据
			String backMsg = new String(backbuf, 0, backPacket.getLength());
		    System.out.println("服务器返回的数据为:" + backMsg);
		    
			socket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
