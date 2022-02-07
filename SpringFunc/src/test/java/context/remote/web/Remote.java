package context.remote.web;

import com.api.UserInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.remoting.httpinvoker.*;
import org.springframework.remoting.support.DefaultRemoteInvocationExecutor;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;

@Configuration
@Slf4j
public class Remote {
    AnnotationConfigApplicationContext context =null;
    @BeforeEach
    public void setUp(){
         context = new AnnotationConfigApplicationContext(Remote.class);

    }
    @Test
    public void try1(){
        CloseableHttpClient build = HttpClientBuilder.create().build();
        RestTemplate restTemplate = new RestTemplate();

    }

    public void export(){
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();

    }
    public HttpInvokerProxyFactoryBean makeInvokeProxyFactory(HttpInvokerRequestExecutor httpInvokerRequestExecutor){
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceUrl("http://localhost:53986/testforRMI");
        httpInvokerProxyFactoryBean.setServiceInterface(UserInterface.class);
        httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(httpInvokerRequestExecutor);
        BeanClassLoaderAware beanClassLoaderAware = (BeanClassLoaderAware)httpInvokerProxyFactoryBean.getHttpInvokerRequestExecutor();
        beanClassLoaderAware.setBeanClassLoader(getClass().getClassLoader());

        httpInvokerProxyFactoryBean.afterPropertiesSet();
        return httpInvokerProxyFactoryBean;
    }

    @Test
    public void httpInvokeTestWithRealRequest(){
        HttpComponentsHttpInvokerRequestExecutor executor = new HttpComponentsHttpInvokerRequestExecutor();
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = makeInvokeProxyFactory(executor);
        UserInterface object = (UserInterface) httpInvokerProxyFactoryBean.getObject();
        String name = object.getName();
        log.info("name:{}",name);

    }
    /**
     todo in the springFactory from local .
     todo todoUpdate. because of don't get HttpInvokerServiceExporter exporter = context
     */
    @Test
    public void httpInvokeTest(){
        HttpInvokerServiceExporter exporter = context.getBean(HttpInvokerServiceExporter.class);

        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceUrl("https://localhost:8080/testforRMI");
        httpInvokerProxyFactoryBean.setServiceInterface(UserInterface.class);

        httpInvokerProxyFactoryBean.setHttpInvokerRequestExecutor(new AbstractHttpInvokerRequestExecutor() {

            @Override
            protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
                MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
                MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
                mockHttpServletRequest.setContent(baos.toByteArray());
                exporter.handleRequest(mockHttpServletRequest,mockHttpServletResponse);
                byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
                RemoteInvocationResult remoteInvocationResult = readRemoteInvocationResult(new ByteArrayInputStream(contentAsByteArray), config.getCodebaseUrl());
                return remoteInvocationResult;

            }
        });
        BeanClassLoaderAware beanClassLoaderAware = (BeanClassLoaderAware)httpInvokerProxyFactoryBean.getHttpInvokerRequestExecutor();
        beanClassLoaderAware.setBeanClassLoader(getClass().getClassLoader());

        httpInvokerProxyFactoryBean.afterPropertiesSet();
        UserInterface userInterface = (UserInterface) httpInvokerProxyFactoryBean.getObject();
        String name = userInterface.getName();
        log.info("name:{}",name);


    }
}
