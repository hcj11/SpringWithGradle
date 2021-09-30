package Exercise;

class Sub extends MainAndSubReentrant implements Runnable {
    public synchronized void operateISub() throws InterruptedException {
        count--;
        Thread.sleep(10);
        System.out.println("sub---count = " + count);
        super.operateIMain();
    }

    @Override
    public void run() {
        try {
            int i = 5;
            while (i > 0) {
                operateISub();
                i--;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class MainAndSubReentrant {
    static int count = 10;

    public synchronized void operateIMain() throws InterruptedException {
        count--;
        Thread.sleep(10);
        System.out.println("main---count = " + count);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Sub());
        thread.start();
    }
}
