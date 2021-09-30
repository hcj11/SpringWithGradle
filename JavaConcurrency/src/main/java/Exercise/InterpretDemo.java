package Exercise;

public class InterpretDemo  extends Thread{
    public InterpretDemo(String name ){
        super();
        setName(name);
    }

    @Override
    public void run() {
        super.run();
        System.out.println("log info insert start");
        if(Thread.currentThread().isInterrupted()){
            try {
                throw new InterruptedException("intend");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("log info insert end");

    }

    public static void main(String[] args) {
        InterpretDemo interpretDemo = new InterpretDemo("Thread -1 ");
        interpretDemo.start();
        interpretDemo.interrupt();
        System.out.println("end");

    }
}
