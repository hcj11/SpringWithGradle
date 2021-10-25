package cloud.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.apache.commons.configuration.web.ServletConfiguration;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * custom server  + httpclient test in use
 */
@Configuration
@Slf4j
@SpringBootTest(properties = {"key=value", "key1=value2"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {EmbeddServerTest.TestBean.class})
@RunWith(SpringRunner.class)
public class EmbeddServerTest {
    @LocalServerPort
    private int port = 8011;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @EnableWebMvc
    @Configuration
    @RestController
    public static class TestBean {

        @Configuration
        @ImportAutoConfiguration({ServletWebServerFactoryAutoConfiguration.class,DispatcherServletAutoConfiguration.class})
        static class ImportClass {}
        // @RequestBody ~ HttpMessageConverter ~ @EnableWebMvc
        @PostMapping(value = "post")
        public String post(@RequestBody List<CustomDto> customDtos) throws IOException {
// ,  RequestFacade requestFacade = null
//            ServletInputStream inputStream = requestFacade.getInputStream();
//            byte[] bytes = new byte[4096];
//            int read = inputStream.read(bytes);
//            log.info("{},{},{}", requestFacade,read,new String(bytes));
            return "i got!!!";
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class CustomDto {
        @JsonProperty("name")
        private String name;
        @JsonProperty("addDate")
        private Date addDate;
    }

    @Test
    public void serverStart() {
        String url = String.format("http://localhost:%s/post", String.valueOf(port));
        log.info("url : ,{}", url);
        ArrayList<CustomDto> hcj = Lists.newArrayList(CustomDto.builder().addDate(new Date()).name("hcj").build());

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<List<CustomDto>> listHttpEntity = new HttpEntity<List<CustomDto>>(hcj, headers);

        String s = testRestTemplate.postForObject(url, listHttpEntity, String.class);
        log.info("result:{}", s);

    }
}
