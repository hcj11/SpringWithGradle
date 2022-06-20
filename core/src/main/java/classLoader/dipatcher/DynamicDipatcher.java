package classLoader.dipatcher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * 1. invokevirtual method
 */
class Father {
    public int money = 1;

    public Father() {
        money = 2;
        showTheMoney();
    }

    public void showTheMoney() {
        System.out.println(" this is father money $" + money);
    }
}

class Son extends Father {
    public int money = 3;

    public Son() {
        money = 4;
        showTheMoney();
    }

    public void showTheMoney() {
        System.out.println(" this is son money $" + money);
    }
}

public class DynamicDipatcher {

    public static void main(String[] args) {
//        Father son = new Son();
//        System.out.println(" this is guy money $" + son.money);
        // 0, 4, 2
//        LocalDate localDate = org.joda.time.LocalDate.now().toString("yyyy");
//        System.out.println(localDate);
        BigDecimal bigDecimal = new BigDecimal(0);
        BigDecimal divide = bigDecimal.divide(new BigDecimal(11), 2, RoundingMode.HALF_UP);
        System.out.println(divide.toPlainString());


    }
}
