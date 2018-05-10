package me.gacl.websocket;

import fix_client.ClientSingleton;
import msg.MsgSendSingleton;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import static java.lang.Thread.sleep;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket")
public class WebSocketTest {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();

	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	private static ClientSingleton _client = null;
	// 类共有一个send_message 和 client
	private static Thread _send_msg_thread = null;
	private static MsgSendSingleton staticMsgSendSingleton = MsgSendSingleton.getInstance(webSocketSet);
	/**
	 * 连接建立成功调用的方法
	 * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 *                 C:\Program Files\Apache Software Foundation\Tomcat 9.0\bin\1.txt
	 */
	/*{
		_send_msg_thread = new Thread(() -> {
			System.out.println("static New msg send thread ");
			while (true){
				String order_msg = _client.get_msg();
				for(WebSocketTest item: webSocketSet){
					try {

						item.sendMessage(order_msg);
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				}
			}

		});
	}*/
	@OnOpen
	public void onOpen(Session session){

		this.session = session;
		webSocketSet.add(this);     //加入set中
		addOnlineCount();           //在线数加1
		System.out.println("New Connection, Current Clients Num: " + getOnlineCount());
/*
		_client = ClientSingleton.getInstance();
*/





	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		webSocketSet.remove(this);  //从set中删除
		subOnlineCount();           //在线数减1
		System.out.println("Connection Closed, Current Clients Num: " + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Msg From Clients:" + message);
		if(message.equals("shutdown")){
			_client.stop();
		}
		//群发消息
		for(WebSocketTest item: webSocketSet){
			try {
				item.sendMessage(message);

			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("============Error===================");
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
		//this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketTest.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketTest.onlineCount--;
	}

	protected void destroy() {

		System.out.println("关闭线程");

		_client.stop();
		_send_msg_thread.interrupt();

	}


	@Override
	public void finalize() {
		System.out.println("====================destructor==============================");
		destroy();
//		super.finalize();
		System.out.println("destructor");
	}
}
