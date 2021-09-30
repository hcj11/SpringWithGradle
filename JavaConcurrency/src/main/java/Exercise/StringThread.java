package Exercise;

import java.util.Properties;

class StringService {
    private static final Properties props = new Properties();
    /**
     * ImmuMutex  Exception in thread "Thread-1" java.lang.NullPointerException
     */
    private String ImmuMutex="lock";

    public void doStringService(String lock) {
        /**
         * this-> 针对的进入方法体的线程， 和 static 的含义一样的 和 成员变量含义一样，
         * 值必须保持不变，
         * */
        synchronized (ImmuMutex) {
            System.out.println(Thread.currentThread().getName() + "---start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "---end");
        }
    }
}

class ThreadCC extends Thread {
    private final StringService StringService;

    public ThreadCC(StringService StringService) {
        this.StringService = StringService;
    }

    @Override
    public void run() {
        super.run();
        // mybatis     new String(); 的话，无法锁住同一个值的。
        StringService.doStringService(new String("A") + new String("B"));
    }
}

class ThreadBB extends Thread {
    private final StringService StringService;

    public ThreadBB(StringService StringService) {
        this.StringService = StringService;
    }

    @Override
    public void run() {
        super.run();
        StringService.doStringService(new String("A") + new String("B"));
    }
}

public class StringThread {

    public static void main(String[] args) {
        // String + String =>  StringBuilder.append("A").append("B").toString;
        String s = "A" + new String("B");
        String s1 = "A" + new String("B");
        System.out.println(s == s1);

        StringService StringService = new StringService();
        ThreadBB ThreadBB = new ThreadBB(StringService);
        ThreadCC ThreadCC = new ThreadCC(StringService);
        ThreadCC ThreadDD = new ThreadCC(StringService);

        ThreadBB.start();
        ThreadCC.start();
        ThreadDD.start();
    }
}

