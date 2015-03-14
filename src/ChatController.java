import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import Commons.IniFileUtil;


/**
 * 聊天服务器入口控制器
 * @author liuxing
 * @date 2015-01-19
 * */
public class ChatController{  
    private static final long serialVersionUID = 1L;  
  
    private static int PORT;
    
	private static String path = "system.ini";  
  
    public ChatController() throws IOException {  
  
        NioDatagramAcceptor acceptor = new NioDatagramAcceptor();  
        acceptor.setHandler(new UdpServerHandler(this));  
  
        Executor threadPool = Executors.newCachedThreadPool();  
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();  
        chain.addLast("logger", new LoggingFilter());  
        //chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));  
        chain.addLast("threadPool", new ExecutorFilter(threadPool));  
  
        DatagramSessionConfig udpSession = acceptor.getSessionConfig();  
        udpSession.setReadBufferSize(4096);// 设置接收最大字节默认2048  
        udpSession.setMaxReadBufferSize(65536);  
        udpSession.setReceiveBufferSize(1024);// 设置输入缓冲区的大小  
        udpSession.setSendBufferSize(1024);// 设置输出缓冲区的大小  
        udpSession.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用  
  
        acceptor.bind(new InetSocketAddress(PORT));  
        System.out.println("UDPServer listening on port " + PORT);  
    }  
  
    public static void main(String[] args) throws IOException {
    	try {
			IniFileUtil ini = new IniFileUtil(path);
			PORT = Integer.parseInt(ini.readValue("server", "serverPort"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        new ChatController();
    }  
}  
