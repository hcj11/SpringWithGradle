package ConcurrentDemo;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class VectorThreadSafe {
    /**
     * Vector 保证了size add remove 的线程同步，如果不控制调用端口，错误的删除元素，导致数组越界等问题
     * 此处： 采用strings同步进行控制，
     */
    private static Vector<String> strings = new Vector<>();

    public static void main(String[] args) {
        while (true) {

            for (int i = 0; i < 10; i++) {
                strings.add(i + "");
            }

            new Thread(() -> {
                synchronized (strings) { // 必须放置到外部，保证线程安全。避免指令重排序
                    for (int i = 0; i < strings.size(); i++) {
                        strings.remove(i);
                    }
                }
            }).start();

            new Thread(() -> {
                synchronized (strings) {
                    for (int i = 0; i < strings.size(); i++) {
                        strings.get(i);
                    }
                }
            }).start();

            while (Thread.activeCount() > 20) ;
        }


    }
}
