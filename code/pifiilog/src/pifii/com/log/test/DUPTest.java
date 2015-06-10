package pifii.com.log.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

/**
 * @author ZYWANG
 * @date 2010-3-9
 */
public class DUPTest {

	/**
	 * @param args
	 * @author ZYWANG
	 * @date 2010-3-9
	 */
	public static void main(String[] args) {
		try {
			DatagramSocket socket = new DatagramSocket();
			String s = "测试文字ABC";
			byte[] buffer = s.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length,InetAddress.getByName("127.0.0.1"),10000);
			socket.send(packet);
			socket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void fomDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("_yyyy_MM_dd");
		System.out.println(sdf.format(new Date()));;
	}
	
}