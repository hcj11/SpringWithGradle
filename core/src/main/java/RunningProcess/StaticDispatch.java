package RunningProcess;


/**
 * �������أ� ��̬����
 */
public class StaticDispatch {
    static abstract class Human {
    }

    static class Man extends Human {

    }

    static class WoMan extends Human {
    }

    public void sayHello(Human human) {
        System.out.println("hello guy");
    }

    public void sayHello(Man man) {
        System.out.println("hello man");

    }

    public void sayHello(WoMan woMan) {
        System.out.println("hello woman");
    }

    public static void main(String[] args) {
        // ���þ�̬���ɣ���jvm��̬�����ڼ�ȷ����̬���ͣ�����ֻ��������ȥȷ��ʵ�����ͣ�,���Դ˴�����HUamnʱ�����Ѿ�ȷ���������͡�
        // ��Ȼ�����ƶϣ���ģ���ģ������ѡʵ�����Ͱ����߼�����ѡ��

        Human man = new Man();
        Human woMan = new WoMan();
        StaticDispatch staticDispatch = new StaticDispatch();
//        staticDispatch.sayHello(man);
//        staticDispatch.sayHello(woMan);

        staticDispatch.sayHello((Man)man);
        staticDispatch.sayHello((WoMan) woMan);

    }
}
