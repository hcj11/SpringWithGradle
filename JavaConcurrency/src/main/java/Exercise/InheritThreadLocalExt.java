package Exercise;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Builder
@Data
class Person{
    private String username;
    private String others;
}
class ThreadLocalExt extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        super.run();

        System.out.println("***main say: " + InheritThreadLocalExt.t1.get());
        Thread.sleep(10000);
        System.out.println("***main say: " + InheritThreadLocalExt.t1.get());
    }
}

public class InheritThreadLocalExt {
    public static InheritableThreadLocal<Person> t1 = new InheritableThreadLocal<Person>(){
        @Override
        protected Person childValue(Person parentValue) {
             parentValue.setOthers("parent ����");
             return parentValue;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        Person �� = new Person("��",null);
        t1.set(��);
        ThreadLocalExt threadLocalExt = new ThreadLocalExt();
        threadLocalExt.start();
        Thread.sleep(1000);
        ��.setUsername("���");
    }
}
