package Service;

import java.io.IOException;
import java.net.InetSocketAddress;

import redis.clients.jedis.Jedis;
import Commons.IniFileUtil;

/**
 * 心跳业务逻辑处理
 * @author liuxing
 * @date 2015-01-20
 * */
public class HeartBeatService {
	private Jedis jedis;
	private String serverRedisIp;
	private int serverRedisPort;
	private String path = "src/conf/system.ini";
	private ChatService chatService = new ChatService();
	
	public HeartBeatService(){
		try {
			IniFileUtil ini = new IniFileUtil(path);
			this.serverRedisIp = ini.readValue("redis", "host");
			this.serverRedisPort = Integer.parseInt(ini.readValue("redis", "port"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		jedis = new Jedis(this.serverRedisIp, this.serverRedisPort);
	}
	
	/**
	 * 记录心跳数据
	 * */
	public void setHeartBeatData(String openId, String host, int port, String dateTimeStamp){
		jedis.hset("online_" + openId, "host", host);
		jedis.hset("online_" + openId, "port", String.valueOf(port));
		jedis.hset("online_" + openId, "openId", openId);
		jedis.hset("online_" + openId, "timestamp", dateTimeStamp);
		//设置ip与open_id关系
		chatService.setIpRelationship(new InetSocketAddress(host, port), openId);
	}
	
	/**
	 * 检查是否有此open_id的心跳记录
	 * */
	public boolean checkHeartBeat(String onlinePrefix){
		return jedis.exists(onlinePrefix);
	}
}
