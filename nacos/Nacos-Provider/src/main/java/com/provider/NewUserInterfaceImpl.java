package com.provider;

import com.api.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.monitor.MonitorService;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.RegistryService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
//@Slf4j
//@org.apache.dubbo.config.annotation.Service(interfaceClass = MonitorService.class, version = "2.5.7",registry = {"zk1"},protocol = "dubbo1")
//class CustomMonitorInterfaceImpl implements MonitorService {
//
//}
@Slf4j
@org.apache.dubbo.config.annotation.Service(interfaceClass = RegistryService.class, version = "2.5.7",registry = {"dubboProtocol1"},protocol = "dubbo1")
class CustomRegistryServiceInterfaceImpl implements RegistryService {
    final HashMap<URL,URL> urls = new HashMap<>();
    @Override
    public void register(URL url) {
        urls.put(url,url);
    }
    @Override
    public void unregister(URL url) {

    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        urls.put(url,url);
        log.info("===================subscribe: received the url,{} and try do this ",url.toString());
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        log.info("unsubscribe the url,{} and try do this ",url.toString());
    }

    @Override
    public List<URL> lookup(URL url) {
        return Lists.newArrayList(urls.get(url));
    }
}
@Slf4j
@org.apache.dubbo.config.annotation.Service(interfaceClass = MonitorService.class, version = "2.5.7",registry = {"zk1"},protocol = "dubbo1")
class CustomMonitorInterfaceImpl implements MonitorService {
    HashSet set = Sets.<URL>newHashSet();
    @Override
    public void collect(URL statistics) {
        set.add(statistics);
        log.info("=============get url statistics msg:{}",statistics.toString());
    }

    @Override
    public List<URL> lookup(URL query) {
        return null;
    }
}

@org.apache.dubbo.config.annotation.Service(interfaceClass = RmiInterface.class, version = "2.5.7",registry = {"zk1"},protocol = "rmi1")
class CustomRmiInterfaceImpl implements RmiInterface {
    @Override
    public String doActionForRmi() {
        return "doActionForRmi";
    }
}
//@org.apache.dubbo.config.annotation.Service(interfaceClass = HttpInterface.class, version = "2.5.7",registry = {"zk1"},protocol = "http1")
//class CustomHttpInterfaceImpl implements HttpInterface {
//    @Override
//    public String doActionForHttp() {
//        return "doActionForHttp";
//    }
//}
/**
 * and to duplicate the beanDefinition    in  case of the serviceKey .  so ,add the interface .
 */
@org.apache.dubbo.config.annotation.Service(interfaceClass = DemoInterfaceWithDubboRegistry.class, version = "2.5.7",registry = {"dubboProtocol1"})
class CustomDemoInterfaceImpl implements DemoInterfaceWithDubboRegistry {
    @Override
    public String doActionForDemo() {
        return "doActionForDemo";
    }
}
@org.apache.dubbo.config.annotation.Service(interfaceClass = DemoInterface.class, version = "2.5.7",registry = {"zk1"})
class CustomDemoInterfaceWithZkImpl implements DemoInterface {
    @Override
    public String doActionForDemo() {
        return "doActionForDemoWithzk";
    }
}
//
@org.apache.dubbo.config.annotation.Service(interfaceClass = UserInterface.class, version = "2.5.7", registry = {"zk1"},protocol = {"dubbo1"})
class NewUserInterfaceForQosImpl implements UserInterface {

    @Override
    public String getName() {
        return "hcjForNewDubbo";
    }
}
@org.apache.dubbo.config.annotation.Service(interfaceClass = UserInterface.class, version = "2.5.7", registry = {"zk1"})
public class NewUserInterfaceImpl implements UserInterface {

    @Override
    public String getName() {
        return "hcjForNewDubbo";
    }
}