import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

interface Interceptor{
    Object  _responseHandler =  new Object();
}
class SuperClass{
    Object  _superObject =  new Object();
}

class SubClass extends SuperClass{

}
public class SimpleInterfaceDemo {
    @Test
    public void demo1(){
        SubClass subClass1 = new SubClass();
        SubClass subClass2 = new SubClass();

        Object responseHandler = Interceptor._responseHandler;
        Assert.isTrue(subClass1._superObject!=subClass2._superObject);;

    }
}
