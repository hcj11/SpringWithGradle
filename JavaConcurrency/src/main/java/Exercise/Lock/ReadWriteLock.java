package Exercise.Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReaWriteService {
    private static ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock(false);

    public void dothis() {
        reentrantLock.readLock().lock();
        System.out.println("-dothis-"+Thread.currentThread().getName()+":"+System.currentTimeMillis());
        reentrantLock.readLock().unlock();
    }
    public void doOthers() {
        reentrantLock.writeLock().lock();
        System.out.println("-doOthers-"+Thread.currentThread().getName()+":"+System.currentTimeMillis());
        reentrantLock.writeLock().unlock();
    }

}

class ThreadAA extends Thread {
    private ReaWriteService ReaWriteService;

    public ThreadAA(ReaWriteService ReaWriteService) {
        this.ReaWriteService = ReaWriteService;
    }

    @Override
    public void run() {
        super.run();
        ReaWriteService.dothis();
    }
}

class ThreadBB extends Thread {
    private ReaWriteService ReaWriteService;

    public ThreadBB(ReaWriteService ReaWriteService) {
        this.ReaWriteService = ReaWriteService;
    }

    @Override
    public void run() {
        super.run();
        ReaWriteService.doOthers();
    }
}

public class ReadWriteLock {

    public static void main(String[] args) throws InterruptedException {
        ReaWriteService ReaWriteService = new ReaWriteService();
        ThreadAA ThreadAA = new ThreadAA(ReaWriteService);
        ThreadBB ThreadBB = new ThreadBB(ReaWriteService);
        ThreadAA.start();
        ThreadBB.start();
    }
}
