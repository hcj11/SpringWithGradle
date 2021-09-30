package Exercise;

public class DBTools {
    volatile static boolean flag = false;

    synchronized void BackupA() throws InterruptedException {
        while (flag) {
            this.wait();
        }
        for (int i = 0; i < 5; i++) {
            System.out.println("A********");
        }
        flag = true;
        this.notifyAll();
    }

    synchronized void BackupB() throws InterruptedException {
        while (!flag) {
            this.wait();
        }
        Thread.yield();

        for (int i = 0; i < 5; i++) {
            System.out.println("B!!!!!!!!!");
        }
        flag = false;
        this.notifyAll();
    }

}
