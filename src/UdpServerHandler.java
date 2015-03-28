import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import Commons.DateTool;
import Commons.Memcached;
import Service.ChatService;
import Service.HeartBeatService;

import com.danga.MemCached.MemCachedClient;

public class UdpServerHandler extends IoHandlerAdapter {
	private MemCachedClient mc = Memcached.getClient();
	private ChatController server;
	private ChatService chatService;
    
    protected MemCachedClient mcc = new MemCachedClient();  
      
	public UdpServerHandler(ChatController server) {
		this.server = server;
		chatService = new ChatService(mc);

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		IoBuffer ioBuffer = (IoBuffer)message;   
	    byte[] dataBytes = new byte[ioBuffer.limit()];
	    ioBuffer.get(dataBytes);
	    HeartBeatService heartBeatService = new HeartBeatService(mc);
	    String openId = "";  
	    
	    String socketHost = ((InetSocketAddress)session.getRemoteAddress()).getHostName();
	    int socketPort = ((InetSocketAddress)session.getRemoteAddress()).getPort();
	    InetSocketAddress socketAddress = new InetSocketAddress(socketHost, socketPort);
		System.out.println(socketAddress + " len: " + dataBytes.length);
		
//		openId = "11111111111111111111111111111111";
		mc.set("chat_talk_11111111111111111111111111111111", "22222222222222222222222222222222");
		mc.set("chat_talk_22222222222222222222222222222222", "11111111111111111111111111111111");
//		chatService.checkTalk(openId);
		
		//当数据包为32个字节时，认为是心跳数据，且数据为openId
		if(dataBytes.length == 32){
	        openId = new String(dataBytes, "utf-8");
			//回复 对方拒绝通话，0表示拒绝状态,1表示接受，2表示心跳返回，服务器已收到心跳
			//double num = Math.random();
			//String returnData = String.valueOf(num);
			//System.out.println(num);
			String returnData = "2";
			IoBuffer.setUseDirectBuffer(false);
			IoBuffer buff = IoBuffer.allocate(returnData.length());
	        buff.setAutoExpand(true);       
	        buff.put(returnData.getBytes());
			buff.flip();
			session.write(buff, socketAddress);
			heartBeatService.setHeartBeatData(openId, socketHost, socketPort, DateTool.getTimeStamp());
		}else{
			//根据用户ip获取open_id
			openId = chatService.getOpenIdByIpRelationship(new InetSocketAddress(socketHost, socketPort));
			System.out.println("收到" + socketAddress + "发的数据包, openId为:" + openId);
			//检查语音通话表看看是否存在
			if(chatService.checkTalk(openId)){
				System.out.println("转发" + socketAddress + "发的数据包");
				//查找根据open_id查找通话表，获得通话对方open_id
				String toOpenId = chatService.getTalkObj(openId);
				//转发数据
				//获取通话对象信息
				Map<String, Object> obj = chatService.getTalkObjMsg(toOpenId);
				String host = obj.get("host").toString();
				int port = Integer.parseInt(obj.get("port").toString());
				InetSocketAddress sck = new InetSocketAddress(host, port);
				ioBuffer.flip();
				System.out.println("源地址:" + socketAddress);
				System.out.println("转发至:" + sck);
				session.write(ioBuffer, sck);
			}else{
				//回复 对方拒绝通话，0表示拒绝状态
				String returnData = "0";
				System.out.println("拒绝" + socketAddress + "发的数据包");
				IoBuffer buff = IoBuffer.allocate(returnData.length());
		        buff.setAutoExpand(true);       
		        buff.put(returnData.getBytes());
				buff.flip();
				session.write(buff, socketAddress);
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		//System.out.println("Session closed...");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
//		System.out.println("Session created...");
//		SocketAddress remoteAddress = session.getRemoteAddress();
//		System.out.println(remoteAddress);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
//		System.out.println("Session idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
//		System.out.println("Session Opened...");
//		SocketAddress remoteAddress = session.getRemoteAddress();
//		System.out.println(remoteAddress);
	}
}