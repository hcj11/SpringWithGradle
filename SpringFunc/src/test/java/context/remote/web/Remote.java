package context.remote.web;

import com.api.UserInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

@Slf4j
public class Remote {

    public void export(){
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();

    }
    @Test
    public void httpInvokeTest(){
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceUrl("");
        httpInvokerProxyFactoryBean.setServiceInterface(UserInterface.class);
        httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(null);
        UserInterface userInterface = (UserInterface)httpInvokerProxyFactoryBean.getObject();
        String name = userInterface.getName();
        log.info("name:{}",name);

    }
}
