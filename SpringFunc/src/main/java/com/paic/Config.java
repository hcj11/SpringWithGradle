package com.paic;

import cn.hutool.core.io.resource.InputStreamResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@Slf4j
@EnableWebMvc
@SpringBootApplication(scanBasePackages = {"com.paic.*"})
@Data
public class Config {

    public static void demo2() throws URISyntaxException, IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        ClientHttpRequest request = factory.createRequest(new URI("http://www.baidu.com"), HttpMethod.POST);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity<String>("hello world", httpHeaders);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://www.baidu.com", httpEntity, String.class, Maps.newHashMap());
        String body = stringResponseEntity.getBody();
    }

    public static void demo1() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://www.baidu.com",null, String.class);
        String body = forEntity.getBody();
        System.out.println(body);
        /**
         * json->
         */
        String s = restTemplate.postForObject("http://www.baidu.com", null, String.class);
        HashMap<String, String> stringStringHashMap = Maps.<String, String>newHashMap();
        stringStringHashMap.put("userId","1");
        String s1 = restTemplate.postForObject("http://www.baidu.com", stringStringHashMap, String.class);


    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        /**
         *   SpringApplication.run(com.paic.Config.class, args)
         */
        ConfigurableApplicationContext run = SpringApplication.run(new Class[]{Config.class}, args);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = contextClassLoader.getResourceAsStream("others.txt");
        log.info("{}", resourceAsStream != null);

    }


}
