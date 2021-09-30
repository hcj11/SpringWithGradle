package Singlton;

class Instance {
}

public class DoubleCheckLock {
    // 单例， 唯一对象属于class下
    private volatile static Instance instance;

    public static Instance getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLock.class) {
                if(instance==null){
                    instance=new Instance(); // 禁止重排序，    1.对象创建 2.对象引用赋值给  对象类型变量 3.初始化
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
