package fix_client;

import msg.OrderMsgQueue;
import quickfix.*;
//import wx_tool.WxBot;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;
import static quickfix.FixVersions.BEGINSTRING_FIX44;

//implements Runnable
public class Fix_Client extends Thread{
    public static quickfix.SessionID sessionID = null;
    private SocketInitiator initiator = null;
    OrderMsgQueue my_orderMsgQueue = null;
    public Fix_Client(OrderMsgQueue orderMsgQueue){
        my_orderMsgQueue = orderMsgQueue;
    }
    private static SessionSettings createSettings() {
        SessionSettings settings = new SessionSettings();
        //
        String SenderID = "ESolution1";

        String serverIP = "211.95.40.133";
        String serverPort = "7567";
        String startTime = "11:45:00";
        String endTime = "11:20:00";
//        String title = "IntradayForBTG";

        Map defaults = new HashMap<String, String>();

        defaults.put("FileStorePath", "/data");
        defaults.put("ConnectionType", "initiator");
        defaults.put("TargetCompID", "CiticNewedge");

        defaults.put("SenderCompID", SenderID);
        defaults.put("SocketConnectHost", serverIP);
        defaults.put("SocketConnectPort ", serverPort);
        /*{
            String path = "1.txt";
            File file = new File(path);
            System.out.println(file.getAbsolutePath());//输出读取到的文件路径
        }*/
        String path = Fix_Client.class.getClassLoader().getResource("../lib/FIX44_Futu.xml").getPath();
        System.out.println(path);
        defaults.put("DataDictionary", path);
//        defaults.put("DataDictionary", "FIX44_Futu.xml");

        defaults.put("StartTime", startTime);
        defaults.put("EndTime", endTime);
        defaults.put("HeartBtInt", "30");
        defaults.put("ReconnectInterval", "5");
        defaults.put("ScreenIncludeMilliseconds", "Y");
        defaults.put("BeginString", BEGINSTRING_FIX44);

        defaults.put("ResetOnLogon", "Y");
        defaults.put("ResetOnLogout", "Y");
        defaults.put("ResetOnDisconnect", "Y");
        defaults.put("ResetOnError", "Y");
        defaults.put("UseDataDictionary", "Y");
        //E:\恒生fix\java_thread\src\my_fix_xml\FIX44_Futu.xml


//        defaults.put("DataDictionary", "FIX44_Futu.xml");

        defaults.put("EnableNextExpectedMsgSeqNum", "Y");
        settings.set(defaults);


        quickfix.SessionID sessionID = new quickfix.SessionID(BEGINSTRING_FIX44, SenderID, "CiticNewedge", "citicsf");
        settings.setString(sessionID, "SocketConnectPort", serverPort);

        /*
         * 4. quickfix.SessionID：是Session的唯一标识。SessionID中包含beginString（必须），senderCompID（必须），
         * senderSubID （可选），senderLocationID（可选），targetCompID（必须），targetSubID（可选），
         * targetSubID（可选），targetLocationID（可选），sessionQualifier（可选）。
         * sessionQualifer用于区分具有相同的targetCompID不同的session，只能用在initiator角色中。
         * SessionID.toString生成的可读的Session ID字符串组成为：
         * beginString:senderCompID/senderSubID/senderLocationID->
         * targetCompID/targetSubID/targetLocationID/sessionQualifier。
         * 如果可选值未设置则在Session ID字符串中默认空字符串。
         * */
        File f= new File("E:" + File.separator + "fix_demo.txt");
        OutputStream out = null ;
        try {
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        settings.toStream(out);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settings;
    }

    private static SessionSettings createSettingsFrCFG() {
        String file_path ="fix_demo.cfg";
        File f= new File(file_path);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SessionSettings settings = null;
        try {
            settings = new SessionSettings(inputStream);
        } catch (ConfigError configError) {
            configError.printStackTrace();
        }
        return settings;
    }

    public void set_OrderMsgQueue(OrderMsgQueue orderMsgQueue){
        my_orderMsgQueue = orderMsgQueue;
    }
    public void run_fix_client() throws Exception {
//        OrderMsgQueue my_orderMsgQueue = new OrderMsgQueue();

        SessionSettings settings = createSettings();
//        SessionSettings settings = createSettingsFrCFG();
        sessionID = settings.sectionIterator().next();
        Application myApp = new FooApplication(sessionID, my_orderMsgQueue);
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(settings);


        initiator = new SocketInitiator(myApp, storeFactory,
                settings, logFactory, new DefaultMessageFactory());


        initiator.start();


//        wait();
        System.out.println("================ while =============");
       /* while (true){
            sleep(1000);
        }*/
        System.out.println("================ fix client run end =============");
    }

    public void stop_initiator(){
        this.initiator.stop(true);
        System.out.println("================ stop_initiator =============");
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            run_fix_client();

            while (!isInterrupted()){
                sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            stop_initiator();
        }
    }
}