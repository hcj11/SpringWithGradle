package RunningProcess;

/**
 * 多静态分派 +动态单分派（在运行时确定传递的引用）
 */
public class MultiDispatch {
    static class _360 {
    }

    static class QQ {
    }

    static class Parent {
        public void handle(_360 _360) {
            System.out.println("parent choose 360");
        }

        public void handle(QQ qq) {
            System.out.println("parent choose qq");
        }
    }

    static class Son extends Parent {
        public void handle(_360 _360) {
            System.out.println("son choose 360");
        }

        public void handle(QQ qq) {
            System.out.println("son choose qq");
        }
    }

    public static void main(String[] args) {
        Parent parent = new Parent();
        Parent son = new Son();
        parent.handle(new _360());
        son.handle(new QQ());
        parent.handle(new QQ());
    }
}
