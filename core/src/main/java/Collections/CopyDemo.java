package Collections;

import lombok.Data;

import java.util.ArrayList;

public class CopyDemo {

    @Data
   static class A{
        private String name;
        public A(){}
        public A(String name){this.name=name;}

    }
    public static void main(String[] args) {
        final A a = new A("hello");
        final A a2 = new A("world");
        ArrayList<A> as = new ArrayList<A>(){{
            add(a);
            add(a2);
        }};
        as.clone();



    }
}
