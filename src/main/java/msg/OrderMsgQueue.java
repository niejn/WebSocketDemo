package msg;
import java.util.LinkedList;
import java.util.Queue;

public class OrderMsgQueue {
    private final Queue<OrderMsg> queue = new LinkedList<OrderMsg>();

    public synchronized OrderMsg get_ordermsg() {
        while (queue.peek() == null) {
            try {
                System.out.println(Thread.currentThread().getName() + ": wait() begins, queue = " + queue);
                wait();
                System.out.println(Thread.currentThread().getName() + ": wait() ends,   queue = " + queue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue.remove();
    }

    public synchronized void put_ordermsg(OrderMsg ordermsg) {
        queue.offer(ordermsg);

        notifyAll();

    }
}
