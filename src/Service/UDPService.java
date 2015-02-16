package Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * UDP服务
 * @author liuxing
 * @date 2015-01-19
 * */
public class UDPService {
	private DatagramPacket recvPacket;
	public int timeout = 0;
	/**
	 * 创建UDP连接
	 * */
	public DatagramSocket createUDPServer(int serverPort) throws IOException{
		DatagramSocket  server = new DatagramSocket(serverPort);
		server.setSoTimeout(this.timeout);
		return server;
	}
	
	/**
	 * 读取UDP数据
	 * */
	public byte[] getDataByUDP(DatagramSocket server) throws IOException{
		byte[] recvBuf = new byte[256];
        recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
        server.receive(recvPacket);
        return recvPacket.getData();
	}
	
	/**
	 * 发送UDP数据
	 * */
	public void sendDataByUDP(DatagramSocket server, InetAddress ipAddr, int clientPort, byte[] sendBuf) throws IOException{
		DatagramPacket sendPacket = new DatagramPacket(sendBuf , sendBuf.length , ipAddr , clientPort );
		server.send(sendPacket);
	}
	
	/**
	 * 获取客户端UDP通信端口
	 * */
	public int getClientPort(){
		return recvPacket.getPort();
	}
	
	/**
	 * 获取客户端UDP通信IP
	 * */
	public InetAddress getIpAddress(){
		return recvPacket.getAddress();
	}
	
	/**
	 * 关闭连接
	 * */
	public void closeUDP(DatagramSocket server) throws IOException{
		server.close();
	}
}
