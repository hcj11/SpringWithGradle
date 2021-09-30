package RunningProcess;


/**
 * 方法重载， 静态分派
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
        // 采用静态分派，在jvm静态编译期间确定静态类型，但是只在运行期去确定实际类型，,所以此处传入HUamn时，就已经确定调用类型。
        // 当然这种推断，是模糊的，多个候选实际类型按照逻辑进行选择

        Human man = new Man();
        Human woMan = new WoMan();
        StaticDispatch staticDispatch = new StaticDispatch();
//        staticDispatch.sayHello(man);
//        staticDispatch.sayHello(woMan);

        staticDispatch.sayHello((Man)man);
        staticDispatch.sayHello((WoMan) woMan);

    }
}
