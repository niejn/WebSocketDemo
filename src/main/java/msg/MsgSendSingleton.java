package msg;


import fix_client.ClientSingleton;
import me.gacl.websocket.WebSocketTest;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

public class MsgSendSingleton {
    private static MsgSendSingleton staticMsgSendSingleton = null;
    private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = null;
    private static ClientSingleton _client = null;
    private static Thread _send_msg_thread = null;

    private MsgSendSingleton() {
        _client = ClientSingleton.getInstance();
        _send_msg_thread = new Thread(() -> {
            System.out.println("static New msg send thread ");
            while (true) {
                String order_msg = _client.get_msg();

                for (WebSocketTest item : webSocketSet) {
                    try {
                        if(item.isAuthorized()){
                            item.sendMessage(order_msg);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                }


            }

        });
        _send_msg_thread.start();
    }

    public static MsgSendSingleton getInstance() {

        return staticMsgSendSingleton;

    }

    public static void set_webSocketSet(CopyOnWriteArraySet<WebSocketTest> in_WebSocketSet) {
        webSocketSet = in_WebSocketSet;
    }

    public static MsgSendSingleton getInstance(CopyOnWriteArraySet<WebSocketTest> in_WebSocketSet) {
        webSocketSet = in_WebSocketSet;
        return staticMsgSendSingleton;

    }

    static {
        //通过静态代码块的执行,来获取实例
        System.out.println("static MsgSendSingleton create");
        staticMsgSendSingleton = new MsgSendSingleton();
    }
}
