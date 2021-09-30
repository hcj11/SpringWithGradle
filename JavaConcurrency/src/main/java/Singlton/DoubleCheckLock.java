package Singlton;

class Instance {
}

public class DoubleCheckLock {
    // ������ Ψһ��������class��
    private volatile static Instance instance;

    public static Instance getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLock.class) {
                if(instance==null){
                    instance=new Instance(); // ��ֹ������    1.���󴴽� 2.�������ø�ֵ��  �������ͱ��� 3.��ʼ��
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) {
        DoubleCheckLock doubleCheckLock = new DoubleCheckLock();
        System.out.println(doubleCheckLock.getInstance());

    }
}
