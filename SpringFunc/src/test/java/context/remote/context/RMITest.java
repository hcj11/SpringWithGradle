package context.remote.context;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.rmi.Remote;

public class RMITest {

    public interface CustomBeanInterface extends Remote{
      public   String getName();
    }
    public class CustomBeanImpl implements CustomBeanInterface{
        @Override
        public String getName() {
            return "hcj";
        }
    }
    @EqualsAndHashCode(callSuper = false)
    @Data
    public class CustomRmiProxyFactoryBean extends RmiProxyFactoryBean{
        @Override
        protected Remote lookupStub() throws RemoteLookupFailureException {
            return new CustomBeanImpl();
        }
    }
    /**
     public abstract , 缺省
     */
    @Test
    public void rmiTest(){
        CustomRmiProxyFactoryBean customRmiProxyFactoryBean = new CustomRmiProxyFactoryBean();
        customRmiProxyFactoryBean.setServiceInterface(CustomBeanInterface.class);
        customRmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/test");
        customRmiProxyFactoryBean.afterPropertiesSet();;

        CustomBeanInterface customBeanInterface = (CustomBeanInterface)customRmiProxyFactoryBean.getObject();
        Assert.isTrue(customBeanInterface instanceof CustomBeanInterface);
        org.junit.Assert.assertEquals(customBeanInterface.getName(),"hcj");


    }

    @Test
    public void otherTest(){
        //        new RmiRegistryFactoryBean();
//        new RMICacheReplicatorFactory();
//        new RmiClientInterceptor().setRegistryClientSocketFactory(null);
    }
}
