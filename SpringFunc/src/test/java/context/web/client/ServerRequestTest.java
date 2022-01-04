package context.web.client;

import cn.hutool.core.io.resource.InputStreamResource;
import com.CustomDtoWithScope;
import com.RestUtils;
import com.ScopeTest;
import context.web.TestInterfaceApi;
import feign.Client;
import feign.Feign;
import feign.Response;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class ServerRequestTest {

    public RestTemplate restTemplate(){
        return RestUtils.restTemplate();
    }

    public void httpInvokeTest(){
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceInterface(null);
        httpInvokerProxyFactoryBean.setServiceUrl("");

    }
    @Test
    public void feginTestWithHttpClient(){

        String url = String.format("http://localhost:%s/test", String.valueOf(57134));
//        LoadBalancerFeignClient loadBalancerFeignClient = new LoadBalancerFeignClient(null);
//        new HttpClient();
//        Client.Default;
//        new LoadBalancerFeignClient();

        TestInterfaceApi target = Feign.builder().client(null).target(TestInterfaceApi.class, url);
        ScopeTest scopeTest = new ScopeTest();
        CustomDtoWithScope customDtoWithScope = scopeTest.customDtoWithScope("hcj",new Date());
        Response post = target.post(customDtoWithScope);

    }
    @Test
    public void feginTest() throws IOException {
        String url = String.format("http://localhost:%s/test", String.valueOf(57134));

        JacksonEncoder jacksonEncoder = new JacksonEncoder();

        TestInterfaceApi target = Feign.builder().encoder(jacksonEncoder)
                .target(TestInterfaceApi.class, url);
        ScopeTest scopeTest = new ScopeTest();
        CustomDtoWithScope customDtoWithScope = scopeTest.customDtoWithScope("hcj",new Date());
        Response post = target.post(customDtoWithScope);
        // i got it !!!
        InputStream inputStream = post.body().asInputStream();
        int i =-1;
        byte[] bytes = new byte[1024];
        while ((i=inputStream.read(bytes))!=-1){
        }
        log.info("{}",new String(bytes));


    }

    /**
     * single and list.
     */
    @Test
    public void serverStartForScope() {
        ScopeTest scopeTest1 = new ScopeTest();
        scopeTest1.getCustomDtoWithScope();

        String url = String.format("http://localhost:%s/test", String.valueOf(61166));
        log.info("url : ,{}", url);
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        ScopeTest scopeTest = new ScopeTest();
        CustomDtoWithScope customDtoWithScope = scopeTest.customDtoWithScope("hcj",new Date());
        HttpEntity<CustomDtoWithScope> HttpEntity = new HttpEntity<CustomDtoWithScope>(customDtoWithScope, headers);

        String s = restTemplate().postForObject(url, HttpEntity, String.class);
        log.info("result:{}", s);

    }
}
