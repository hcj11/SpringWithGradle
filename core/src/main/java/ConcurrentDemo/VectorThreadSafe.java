package ConcurrentDemo;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class VectorThreadSafe {
    /**
     * Vector ��֤��size add remove ���߳�ͬ������������Ƶ��ö˿ڣ������ɾ��Ԫ�أ���������Խ�������
     * �˴��� ����stringsͬ�����п��ƣ�
     */
    private static Vector<String> strings = new Vector<>();

    public static void main(String[] args) {
        while (true) {

            for (int i = 0; i < 10; i++) {
                strings.add(i + "");
            }

            new Thread(() -> {
                synchronized (strings) { // ������õ��ⲿ����֤�̰߳�ȫ������ָ��������
                    for (int i = 0; i < strings.size(); i++) {
                        strings.remove(i);
                    }
                }
            }).start();

            new Thread(() -> {
                synchronized (strings) {
                    for (int i = 0; i < strings.size(); i++) {
                        strings.get(i);
                    }
                }
            }).start();

            while (Thread.activeCount() > 20) ;
        }


    }
}
