package RunningProcess;

/**
 * static方法等条件下，会在jvm编译期间，确定符号引用到的方法引用。成为非虚方法。
 *
 * <p>
 * 方法重新，动态分派
 */
public class DynamicDispatch {
    static abstract class Human {
        abstract void sayHello();
    }

    static class Man extends Human {
        @Override
        void sayHello() {
            System.out.println("man");
        }
    }

    static class Woman extends Human {
        @Override
        void sayHello() {
            System.out.println("woman");
        }
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        man.sayHello();
        woman.sayHello();
        man = new Woman();
        man.sayHello();
    }

}
