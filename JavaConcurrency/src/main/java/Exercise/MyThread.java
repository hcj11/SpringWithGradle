package Exercise;

public class MyThread  extends Thread {
    private int count=5;
    // ��ʱcount Ϊ���������
    public MyThread(String name){
        super();
        setName(name);
    }
    @Override
    public void run() {
        super.run();
        while (count>0){
            count--;
            System.out.println(Thread.currentThread().getName()+"���� count="+count);
        }
    }
    public static void main(String[] args) {
        MyThread myThread1 = new MyThread(" thread - 1");
        MyThread myThread2 = new MyThread(" thread - 2");
        MyThread myThread3 = new MyThread(" thread - 3");
        myThread1.start();;
        myThread2.start();;
        myThread3.start();;


    }
}
