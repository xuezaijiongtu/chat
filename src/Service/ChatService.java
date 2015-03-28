package Service;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.danga.MemCached.MemCachedClient;

/**
 * 用户通话业务逻辑
 * @author liuxing
 * @date 2015-01-24
 * */
public class ChatService {
	private MemCachedClient mc;
	
	public ChatService(MemCachedClient mc){
		this.mc = mc;
	}
	
	/**
	 * 设置socketAddress与open_id映射关系
	 * */
	public boolean setIpRelationship(SocketAddress socketAddress, String open_id){
		return mc.set("chat_relationOfIp_" + socketAddress, open_id);
	}
	
	/**
	 * 根据ip获取open_id
	 * */
	public String getOpenIdByIpRelationship(SocketAddress socketAddress){
		return mc.get("chat_relationOfIp_" + socketAddress).toString();
	}
	
	
	/**
	 * 获取通话对象
	 * */
	public String getTalkObj(String openId){
		return mc.get("chat_talk_" + openId).toString();
	}
	
	/**
	 * 检查是否存在通话
	 * */
	public boolean checkTalk(String openId){
		String key = "chat_talk_" + openId;
		//boolean status = mc.keyExists(key);
		System.out.println(mc.get(key));
		System.out.println(key);
		return true;
		//return mc.keyExists("chat_talk_" + openId);
	}
	
	/**
	 * 获取通话对象通讯信息
	 * */
	public Map<String, Object> getTalkObjMsg(String openId){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("host", mc.get("chat_online_" + openId + "_host"));
		data.put("port", mc.get("chat_online_" + openId + "_port"));
		return data;
	}
}
