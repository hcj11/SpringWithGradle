package run;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class startUp {

    class Dummy{}

    @Test
    public void startup(){
        double a=1.1d;
        int a1 = (int) a;
        System.out.println(a1);
        double b =0.99;
        Assertions.assertEquals(Math.ceil(b),1.00 ,"" );;
        double c=1.32;
        Assertions.assertEquals(Math.ceil(c),2 ,"" );;
    }

}
