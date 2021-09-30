package Exercise;

import io.netty.util.internal.InternalThreadLocalMap;

class ThreadOne extends Thread {
    public DBTools dbTools;

    public ThreadOne(DBTools dbTools) {
        this.dbTools = dbTools;
    }

    @Override
    public void run() {
        super.run();
        try {
            dbTools.BackupA();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ThreadTwo extends Thread {
    public DBTools dbTools;

    public ThreadTwo(DBTools dbTools) {
        this.dbTools = dbTools;
    }

    @Override
    public void run() {
        super.run();
        try {
            dbTools.BackupB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class WaitAndNotifyDemo {
    public static void main(String[] args) {
        DBTools dbTools = new DBTools();
        for (int i = 0; i < 10; i++) {
            ThreadOne threadOne = new ThreadOne(dbTools);
            ThreadTwo threadTwo = new ThreadTwo(dbTools);
            threadOne.start();
            threadTwo.start();
        }


    }

}
