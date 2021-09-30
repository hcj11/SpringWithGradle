package classLoader.dipatcher.staticdynamic;
 class QQ{}
 class _360{}
public class StaticDynamicDipather {
    static class Father{
        public void hardChose(QQ qq){
            System.out.println("father choose qq");
        }
        public void hardChose(_360 _360){
            System.out.println("father choose _360");
        }
    }
    static class Son extends Father{
        public void hardChose(QQ qq){
            System.out.println("son choose qq");
        }
        public void hardChose(_360 _360){
            System.out.println("son choose _360");
        }
    }

    public static void main(String[] args) {
        Father father = new Father();
        Father son = new Son();
        father.hardChose(new _360());
        son.hardChose(new QQ());
    }

}
