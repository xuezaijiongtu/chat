package Commons;
import java.io.IOException;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
  
public class Memcached {  
	private static String path = "system.ini";
	protected static MemCachedClient mcc = null;  
      
    static{
    	String serverMemcachedIp = "";
    	int serverMemcachedPort = 0;
    	
    	try {
			IniFileUtil ini = new IniFileUtil(path);
			serverMemcachedIp = ini.readValue("memcached", "host");
			serverMemcachedPort = Integer.parseInt(ini.readValue("memcached", "port"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        //服务器列表和其权重  
        String[] servers = {serverMemcachedIp + ":" + serverMemcachedPort};   
          
        //获取sock连接池的实例对象  
        SockIOPool pool = SockIOPool.getInstance();  
          
        //设置服务器信息  
        pool.setServers(servers); 
          
        //设置初始连接数、最小最大连接数、最大处理时间  
        pool.setInitConn(20);  
        pool.setMinConn(20);  
        pool.setMaxConn(250);  
        pool.setMaxIdle(1000*60*60*6);  
          
        //设置主线程的睡眠时间  
        pool.setMaintSleep(30);  
          
        pool.setNagle(false);  
        pool.setSocketTO(30);  
        pool.setSocketConnectTO(0);  
          
        //设置Tcp的参数，连接超时等  
        pool.initialize(); 
        System.out.println(pool);
          
        //压缩设置，超过制定大小（单位K）的数据都会压缩  
        //mcc.setCompressEnable(false);  
        //mcc.setCompressThreshold(64*1024);
        if(mcc == null){
        	mcc = new MemCachedClient();
        }
    }  
      
    //保护构造函数，不允许实例化  
    protected Memcached(){}  
      
    public static MemCachedClient getClient(){  
        return mcc;  
    }  
}  
