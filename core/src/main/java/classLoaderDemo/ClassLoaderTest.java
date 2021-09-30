package classLoaderDemo;

/**
 * �ڶ��߳��У�jvm���Ա�֤class ��ʼ���б�֤һ��ֻ��һ���߳̿���ִ�У�����ֻ��ʼ��һ�� �� ���ҵ�����ʧ�ܺ󣬡�
 * ͬ��һ��class�������������̲߳������½����ʼ����
 */
public class ClassLoaderTest {

    static class Parent {
        static {
            if (true) {
                System.out.println("init class");
                while (true) {
                }
            }
        }

        // ��ȡclass�ı������г�ʼ������
        private static int a = 1;

    }
    /**
     * one: Thread-0
     * two: Thread-1
     * init class
     *
     * �޷�ִ�и�ֵ���������ڶ����̱߳���ȴ�����һ���̳߳�ʼ�������ȡֵ
     */
    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                System.out.println("one: " + Thread.currentThread().getName());
                System.out.println(Parent.a);
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                System.out.println("two: " + Thread.currentThread().getName());
                System.out.println(Parent.a);
            }
        });
        thread1.start();
        thread2.start();


    }
}
