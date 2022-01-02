package web.server;
import com.CustomDtoWithScope;
import com.ScopeTest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.log.LogAutoConfiguration;
import com.print.PrintUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.CoyoteInputStream;
import org.apache.tomcat.util.net.NioEndpoint;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    public int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Autowired
    private TestRestTemplate testRestTemplate;
    /**
     * loggingListener  search logback.xml in the classpath.
     */
    @EnableWebMvc
    @Configuration
    @Controller
    @Data
    public static class TestBean implements WebMvcConfigurer {
        RandomAccessFile rw = null;
       volatile FileChannel channel = null;
        AtomicInteger atomicInteger =null;

                {
             atomicInteger = new AtomicInteger(1);

            try {
                rw = new RandomAccessFile("G:\\tmp\\fj.pdf", "rws");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        private  volatile ThreadPoolTaskExecutor threadPoolTaskExecutor;

        @Override
        public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
            ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
            threadPoolTaskExecutor.initialize();;
            configurer.setTaskExecutor(threadPoolTaskExecutor);
            configurer.setDefaultTimeout(1000 * 100 );
            this.threadPoolTaskExecutor=threadPoolTaskExecutor;
        }


        @Configuration
        @ImportAutoConfiguration({ServletWebServerFactoryAutoConfiguration.class,
                DispatcherServletAutoConfiguration.class})
        static class ImportClass {
        }
        // @RequestBody ~ HttpMessageConverter ~ @EnableWebMvc

        private int readWithInputStreamAndWriteWithFileChannel(FileChannel out, CoyoteInputStream coyoteInputStream) throws IOException {
            int size =0 ;

            ByteBuffer buf = ByteBuffer.allocate(8192);

            buf.clear();          // Prepare buffer for use
            while (coyoteInputStream.read(buf) >= 0 || buf.position() != 0) {
                int write = out.write(buf);
                size += write;
                out.force(true);
                buf.compact();    // In case of partial write

            }
            return size;
        }
        @PostMapping
        public void request(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
            HttpServlet httpServlet = new DispatcherServlet();

            CoyoteInputStream inputStream = (CoyoteInputStream)httpServletRequest.getInputStream();
            inputStream.available();
            inputStream.read();
            int andIncrement = atomicInteger.getAndIncrement();
            channel = rw.getChannel();
            int size = readWithInputStreamAndWriteWithFileChannel(channel, inputStream);
            log.info("{}: got inputstream's size : {}",andIncrement,size);
// control loop ’s count .
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(ByteBuffer.allocate(size).array());
            channel.close();;


        }

        @ResponseBody
        @PostMapping("/test")
        public String test(@RequestBody CustomDtoWithScope customDtoWithScope){
            /**
             *  获取代理对象的值
             */
            customDtoWithScope.doAction();
            log.info("customDtoWithScope:{},{}",customDtoWithScope.hashCode(),customDtoWithScope);
            return "i got it !!!";

        }
        /**
         *
         */
        public void test2(){

        }

        public void asyncProcessor(DeferredResult deferredResult ){
            threadPoolTaskExecutor.execute(()->{
                try {
                    Thread.sleep(1000 * 3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deferredResult.setResult("complement object");
            });
        }
        @ResponseBody
        @RequestMapping(value = "/deferredResult")
        public DeferredResult deferredResult(){

            DeferredResult deferredResult = new DeferredResult();
            asyncProcessor(deferredResult);
            return deferredResult;
        }
        /**
         *
         * the webAsyncTask 's  means  is same with  callable
         */
        @ResponseBody
        @RequestMapping(value = "/callabled")
        public Callable callable(){
               log.info("===make any actions");
                return new Callable() {
                    @Override
                    public Object call() throws Exception {
                        // 10s  internal.
                        Thread.sleep(1000 * 10);
                        return "async return data how to get ?";
                    }
                };
        }
        @ResponseBody
        @RequestMapping(value = "/webAsyncTask")
        public WebAsyncTask webAsyncTask(){
            log.info("===make any actions");
            return new WebAsyncTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    // 10s  internal.
                    Thread.sleep(1000 * 10);
                    return "async return data how to get ?";
                }
            });
        }
        /**
         * ByteBuffer allocate = ByteBuffer.allocate(133225);
         * int read = httpServletRequest.getInputStream().read(allocate.array());
         * allocate.limit(read);
         * log.info("have been read size: {}:{}",read,allocate.toString());
         * ServletOutputStream outputStream = httpServletResponse.getOutputStream();
         * outputStream.write(allocate.array());
         * //            outputStream.flush();;
         *             // 块。   transfer-type:chunk
         */

        @ResponseBody
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


    Object lock = new Object();
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void startUpServer() throws InterruptedException {
//  transfer port to ..
        log.info("listen on the port :{}", port);
        synchronized (lock) {
            lock.wait();
        }

    }

    @Test
    public void asyncHttpClient() {

    }
    @Test
    public void test5(){
        /**
         * getSocket().write();
         * selector.
         */
    }
    @Test
    public void test4(){
        NioEndpoint nioEndpoint = new NioEndpoint();
        nioEndpoint.createExecutor();

        Executor executor = nioEndpoint.getExecutor();
        ScheduledExecutorService utilityExecutor = nioEndpoint.getUtilityExecutor();
        log.info("executor:{},utilityExecutor:{}",executor.toString(),utilityExecutor.toString());

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