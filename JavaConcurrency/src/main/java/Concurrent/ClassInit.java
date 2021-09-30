package Concurrent;

import java.math.BigDecimal;
import java.util.concurrent.Executors;

class Son extends ClassInit{
    static int B=A;
}

public class ClassInit {
    static int A=1;
    static {
         A=2;
    }
    public static void main(String[] args) {
        System.out.println(Son.B);
        BigDecimal bigDecimal = new BigDecimal(0.153);
        System.out.println(bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP));
        System.out.println(bigDecimal.setScale(1,BigDecimal.ROUND_DOWN));
        System.out.println(bigDecimal.setScale(1,BigDecimal.ROUND_UP));
        System.out.println(bigDecimal.setScale(1,BigDecimal.ROUND_CEILING));
        System.out.println(bigDecimal.setScale(1,BigDecimal.ROUND_FLOOR));

        BigDecimal subtract = new BigDecimal(100).subtract(new BigDecimal(100.01)).
                multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
        System.out.println(subtract);


    }

}
