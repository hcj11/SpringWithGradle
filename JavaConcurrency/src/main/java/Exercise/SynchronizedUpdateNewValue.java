package Exercise;

class UpdateService {
    private boolean running = true;

    public void runing() {
        String helloWakeup = "helloWakeup";
        while (running) { // synchronized 提供可见性.
            synchronized (helloWakeup) {
                if(running){
                    running=false;
                }
            }
        }
        System.out.println("end");
    }

    public void stop() {
        running = false;
    }
}

class ThreadCCC extends Thread {
    private final UpdateService UpdateService;

    public ThreadCCC(UpdateService UpdateService) {
        this.UpdateService = UpdateService;
    }

    @Override
    public void run() {
        super.run();
        UpdateService.stop();
    }
}

class ThreadBBB extends Thread {
    private final UpdateService UpdateService;

    public ThreadBBB(UpdateService UpdateService) {
        this.UpdateService = UpdateService;
    }

    @Override
    public void run() {
        super.run();
        UpdateService.runing();
    }
}

public class SynchronizedUpdateNewValue {

    public static void main(String[] args) throws InterruptedException {
        UpdateService updateService = new UpdateService();
        ThreadBBB threadBBB = new ThreadBBB(updateService);
        ThreadBBB threadBBBA = new ThreadBBB(updateService);

//        ThreadCCC threadCCC = new ThreadCCC(updateService);
        threadBBB.start();
        threadBBBA.start();
//        Thread.sleep(1000);
//        threadCCC.start();
        System.out.println("calm down");

    }
}
