package Exercise;

class Service {
    public static synchronized void doService() {
        System.out.println(Thread.currentThread().getName() + "---start");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "---end");
    }
}

class ThreadC extends Thread {
    private final Service service;

    public ThreadC(Service service) {
       this.service=service;
    }

    @Override
    public void run() {
        super.run();
        service.doService();
    }
}

class ThreadB extends Thread {
    private final Service service;

    public ThreadB(Service service) {
        this.service=service;
    }

    @Override
    public void run() {
        super.run();
        service.doService();
    }
}

public class ThreadA extends Thread {
    private final Service service;

    public ThreadA(Service service) {
        this.service=service;
    }

    @Override
    public void run() {
        super.run();
        service.doService();
    }

    public static void main(String[] args) {
        Service service = new Service();
        ThreadA threadA = new ThreadA(service);
        ThreadB threadB = new ThreadB(service);
        ThreadC threadC = new ThreadC(service);
        threadA.start();
        threadB.start();
        threadC.start();
    }
}

