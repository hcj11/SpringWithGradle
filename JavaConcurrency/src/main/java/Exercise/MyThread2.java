package Exercise;

public class MyThread2 extends Thread {
    // ��ʱcount���˹���������仯��Ҫ��rannable����һ�ݡ�
    private int count = 5;

    @Override
    public void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + "���� count=" + count);
    }

    public static void main(String[] args) {

        MyThread2 myThread1 = new MyThread2();
        Thread thread1 = new Thread(myThread1, "thread-1");
        Thread thread2 = new Thread(myThread1, "thread-2");
        Thread thread3 = new Thread(myThread1, "thread-3");
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
