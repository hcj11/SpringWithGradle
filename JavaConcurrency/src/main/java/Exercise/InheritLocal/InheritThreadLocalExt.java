package Exercise.InheritLocal;

class ThreadLocalExt extends Thread {
    @Override
    public void run() {
        super.run();
        String s = InheritThreadLocalExt.t1.get();
        System.out.println("ThreadLocalExt say: " + s);
    }
}

public class InheritThreadLocalExt {
    public static InheritableThreadLocal<String> t1 = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        t1.set("----main ------");
        System.out.println("***main say: " + t1.get());
        ThreadLocalExt threadLocalExt = new ThreadLocalExt();
        threadLocalExt.start();
        Thread.sleep(1000);
        t1.set("new main");
        System.out.println("****main say: " + t1.get() + "***");

    }
}
