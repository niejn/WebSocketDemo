package fix_client;

import java.io.File;

import static java.lang.Thread.sleep;

public class test {
    //public static void main(String args[]) throws Exception
    public static void main(String args[]) {
        System.out.println("test");

        ClientSingleton temp = ClientSingleton.getInstance();
        try {
            System.out.println("sleep");
            sleep(500000);
            System.out.println("stop start");
//            temp.finalize();
//            temp.destroy();
            temp.stop();
            System.out.println("stop end");
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("app killed end");
        return;
    }
}
