package Exercise.Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Service {
    private static ReentrantLock reentrantLock = new ReentrantLock(false);
    private static Condition condition = reentrantLock.newCondition();

    public void dothis() {
        reentrantLock.lock();

        try {
            System.out.println("----" + Thread.currentThread().getName() + " start");
            condition.await(3, TimeUnit.SECONDS);
            System.out.println("----" + Thread.currentThread().getName() + " end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reentrantLock.unlock();
    }

    public void signal() {
        reentrantLock.lock();
        condition.signalAll();
        reentrantLock.unlock();
    }
}

class ThreadA extends Thread {
    private Service service;

    public ThreadA(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.dothis();
    }
}

class ThreadB extends Thread {
    private Service service;

    public ThreadB(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.dothis();
    }
}

public class CustomLock {

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();
        ThreadA threadA = new ThreadA(service);
        ThreadB threadB = new ThreadB(service);
        threadA.start();
        ;
        threadB.start();
        ;

//        Thread.sleep(2000);
//        service.signal();
    }
}
