package Service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import Commons.IniFileUtil;

/**
 * 用户通话业务逻辑
 * @author liuxing
 * @date 2015-01-24
 * */
public class ChatService {
	private static Jedis jedis;
	private String serverRedisIp;
	private int serverRedisPort;
	private String path = "src/conf/system.ini";
	
	public ChatService(){
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
	 * 设置socketAddress与open_id映射关系
	 * */
	public String setIpRelationship(SocketAddress socketAddress, String open_id){
		return jedis.set("relationOfIp_" + socketAddress, open_id);
	}
	
	/**
	 * 根据ip获取open_id
	 * */
	public String getOpenIdByIpRelationship(SocketAddress socketAddress){
		return jedis.get("relationOfIp_" + socketAddress);
	}
	
	
	/**
	 * 获取通话对象
	 * */
	public String getTalkObj(String openId){
		return jedis.hget("talk_" + openId, "to");
	}
	
	/**
	 * 检查是否存在通话
	 * */
	public boolean checkTalk(String openId){
		return jedis.exists("talk_" + openId);
	}
	
	/**
	 * 获取通话对象通讯信息
	 * */
	public Map<String, Object> getTalkObjMsg(String openId){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("host", jedis.hget("online_" + openId, "host"));
		data.put("port", jedis.hget("online_" + openId, "port"));
		return data;
	}
}
