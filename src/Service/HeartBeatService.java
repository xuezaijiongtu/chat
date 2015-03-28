package Service;

import java.net.InetSocketAddress;

import com.danga.MemCached.MemCachedClient;

/**
 * 心跳业务逻辑处理
 * @author liuxing
 * @date 2015-01-20
 * */
public class HeartBeatService {
	private static MemCachedClient mc;
	private static ChatService chatService;
	
	public HeartBeatService(MemCachedClient mc){
		this.mc = mc;
		chatService = new ChatService(mc);
	}
	
	/**
	 * 记录心跳数据
	 * */
	public void setHeartBeatData(String openId, String host, int port, String dateTimeStamp){
		mc.set("chat_online_" + openId + "_host", host);
		mc.set("chat_online_" + openId + "_port", String.valueOf(port));
		mc.set("chat_online_" + openId + "_openId", openId);
		mc.set("chat_online_" + openId + "_timestamp", dateTimeStamp);
		//设置ip与open_id关系
		chatService.setIpRelationship(new InetSocketAddress(host, port), openId);
	}
	
	/**
	 * 检查是否有此open_id的心跳记录
	 * */
	public boolean checkHeartBeat(String onlinePrefix){
		return mc.keyExists(onlinePrefix);
	}
}
