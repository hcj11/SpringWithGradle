package classLoader;

class Parents {

    Parents() {
        preProcess();
    }

    void preProcess() {
        System.out.println("parent");
    }
}

class Son extends Parents {
    public String hello = "hcj";

    // https://coolshell.cn/articles/1106.html
    @Override
    void preProcess() {
        hello = "world";
        System.out.println("son");
    }
}

public class Problem1 {

    public static void main(String[] args) {
        Son son = new Son();
        System.out.println(son.hello); //
    }
}
