package fix_client;

import msg.OrderMsg;
import msg.OrderMsgQueue;

public class ClientSingleton {
    private static ClientSingleton staticSingleton=null;
    private static Thread fix_client_thread = null;
    public static Fix_Client _client = null;
    public static OrderMsgQueue my_orderMsgQueue = null;

    public OrderMsgQueue get_orderMsgQueue(){
        return my_orderMsgQueue;
    }

    public String get_msg(){

        OrderMsg my_order = my_orderMsgQueue.get_ordermsg();
        System.out.println(Thread.currentThread().getName() + " handles  " + my_order);
        return my_order.toString();
    }
    private ClientSingleton(){
        my_orderMsgQueue = new OrderMsgQueue();
         _client = new Fix_Client(my_orderMsgQueue);
//        fix_client_thread = new Thread(_client);
        fix_client_thread = _client;
        fix_client_thread.start();

    };
    public void stop(){
        System.out.println("关闭线程");
        _client.stop_initiator();
        fix_client_thread.interrupt();
    }
    protected void destroy() {

        System.out.println("关闭线程");

        stop();

    }
    protected void finalize() throws java.lang.Throwable {

        destroy();

        // 递归调用超类中的finalize方法

        super.finalize();

    }
    static {
        //通过静态代码块的执行,来获取实例
        System.out.println("static ClientSingleton create");
        staticSingleton = new ClientSingleton();
    }

    public static ClientSingleton getInstance(){
        return staticSingleton;
    }
}
