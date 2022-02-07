package com.provider;

import com.api.UserInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


public class ProviderBean {
    /**
     * and nest class don't filter ?
     */
//    @Service(interfaceClass = UserInterface.class) maybe use the loadBalance?
    @Component
    @EqualsAndHashCode(callSuper = false)
    @Data
    public static class UserInterfaceImpl implements UserInterface{

        @Override
        public String getName() {
            return "hcjForDubbo";
        }
    }



}
