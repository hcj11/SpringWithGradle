package run;

import com.jayway.awaitility.Awaitility;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.configure.BulkheadAspect;
import io.github.resilience4j.bulkhead.configure.threadpool.ThreadPoolBulkheadConfiguration;
import io.github.resilience4j.bulkhead.event.BulkheadEvent;
import io.github.resilience4j.bulkhead.internal.FixedThreadPoolBulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.common.bulkhead.monitoring.endpoint.BulkheadEventDTO;
import io.github.resilience4j.common.bulkhead.monitoring.endpoint.BulkheadEventsEndpointResponse;
import io.github.resilience4j.common.circuitbreaker.monitoring.endpoint.CircuitBreakerEndpointResponse;
import io.github.resilience4j.common.circuitbreaker.monitoring.endpoint.CircuitBreakerEventsEndpointResponse;
import io.github.resilience4j.common.timelimiter.monitoring.endpoint.TimeLimiterEventsEndpointResponse;
import io.github.resilience4j.consumer.EventConsumerRegistry;
import io.github.resilience4j.core.EventConsumer;
import io.github.resilience4j.core.StopWatch;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.ratelimiter.configure.RateLimiterAspect;
import io.github.resilience4j.ratelimiter.internal.AtomicRateLimiter;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = RunTests.Dummy.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=9909"})
public class RunTests {
    @Autowired
    GenericApplicationContext applicationContext;
    @Autowired
    Normal normal;
    @Autowired
    TimeLimiterNormal timeLimiterNormal;
    @Autowired
    TimeLimiterNormalForBackEnd timeLimiterNormalForBackEnd;
    @Autowired(required = false)
    TestRestTemplate testRestTemplate;
    @Autowired
    BulkHeadConfig bulkHeadConfig;

    @Test
    public void justRunThrowsErrorWithParamIsFuture() throws InterruptedException, ExecutionException {

        CompletableFuture test = normal.getMessage(CompletableFuture.completedFuture("test"));
        Assertions.assertEquals(test.get(), "future val");
        synchronized (lock){
            lock.wait();
        }

    }

    Object lock = new Object();

    @Test
    public void justRunThrowsErrorWithParam() throws InterruptedException {
        String error = normal.error("error");
        Assertions.assertEquals(error, "invoke the fallback method");

        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void justRunThrowsError() throws InterruptedException {
        normal.error();
    }

    @Test
    public void justRunForOthersInstance() throws InterruptedException, ExecutionException {
        CompletableFuture slow = timeLimiterNormalForBackEnd.slow();
        Assertions.assertEquals(slow.get(), "slow");
    }

    @Test
    public void justRun() throws InterruptedException, ExecutionException {
        StopWatch start = StopWatch.start();
        CompletableFuture slow = timeLimiterNormal.slow();
        log.info("{}", slow.get());
        Assertions.assertEquals(slow.get(), "Action is too slow");
        Duration stop = start.stop();
        Assertions.assertTrue(stop.compareTo(Duration.ofSeconds(9)) < 0);

        String uri = "http://localhost:9909/actuator/timelimiterevents";
        TimeLimiterEventsEndpointResponse forObject =
                testRestTemplate.getForObject(uri, TimeLimiterEventsEndpointResponse.class);
        Assertions.assertEquals(1, forObject.getTimeLimiterEvents().size());
        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void run() {
        EventConsumer consumer = (Object event) -> {
//            void consumeEvent(T event);
        };

//        CircuitBreakerAutoConfiguration.class;

        EventConsumer bean = applicationContext.getBean(EventConsumer.class);
//        bean.consumeEvent();

        //
//        applicationContext
        //
        // .get();
        //
    }

    @Autowired
    ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry;
    @Autowired
    BulkheadRegistry bulkheadRegistry;
    @Autowired
    RetryConfig retryConfig;
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    @Test
    public void runForBulkHeadForSemphore() throws InterruptedException {

        io.github.resilience4j.bulkhead.Bulkhead aDefault = bulkheadRegistry.bulkhead("default");
        Assertions.assertEquals(aDefault.getBulkheadConfig().getMaxConcurrentCalls(), 1);
        Assertions.assertEquals(aDefault.getBulkheadConfig().getMaxWaitDuration(), Duration.ofSeconds(0));

        executorService.submit(() -> {
            bulkHeadConfig.concurrentControllerWithSemaphore();
        });
        executorService.submit(() -> {
            bulkHeadConfig.concurrentControllerWithSemaphore();
        });
        executorService.submit(() -> {
            CompletableFuture completableFuture = bulkHeadConfig.concurrentControllerWithSemaphore();
            try {
                String o = (String) completableFuture.get();
                Assertions.assertEquals(o, "rejectMessage");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });


        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        Awaitility.await().atMost(100, TimeUnit.SECONDS).until(() -> {
            return aDefault.getMetrics().getAvailableConcurrentCalls() == 1;
        });

        BulkheadEventsEndpointResponse forObject = testRestTemplate.getForObject("http://localhost:9909/actuator/bulkheadevents", BulkheadEventsEndpointResponse.class);

        List<BulkheadEventDTO> CALL_PERMITTED = forObject.getBulkheadEvents().stream().filter((bulkheadEventDTO) -> {
            return bulkheadEventDTO.getType().equals(BulkheadEvent.Type.CALL_PERMITTED);
        }).collect(Collectors.toList());
        List<BulkheadEventDTO> CALL_REJECTED = forObject.getBulkheadEvents().stream().filter((bulkheadEventDTO) -> {
            return bulkheadEventDTO.getType().equals(BulkheadEvent.Type.CALL_REJECTED);
        }).collect(Collectors.toList());

        assertThat(CALL_PERMITTED.size()).isEqualTo(1);
        assertThat(CALL_REJECTED.size()).isEqualTo(2);

        synchronized (lock) {
            lock.wait();
        }
    }
    @Test
    public void runForBulkHead2() throws InterruptedException {
        ThreadPoolBulkhead aDefault = threadPoolBulkheadRegistry.bulkhead("default");
        range(0,4).forEach((i)->{
            executorService.submit(()->{
                bulkHeadConfig.controller();
            });
        });
        BulkheadEventsEndpointResponse forObject = testRestTemplate.getForObject("http://localhost:9909/actuator/bulkheadevents", BulkheadEventsEndpointResponse.class);
        List<BulkheadEventDTO> bulkheadEvents = forObject.getBulkheadEvents();
        List<BulkheadEvent.Type> collect = bulkheadEvents.stream().map(BulkheadEventDTO::getType).collect(Collectors.toList());
        assertThat(collect).containsOnlyOnce(BulkheadEvent.Type.CALL_PERMITTED);
        synchronized (lock){lock.wait();}

    }
    @Test
    public void runForBulkHead() throws InterruptedException {
        ThreadPoolBulkhead aDefault = threadPoolBulkheadRegistry.bulkhead("default");
        ThreadPoolBulkheadConfig bulkheadConfig = aDefault.getBulkheadConfig();
        Assertions.assertEquals(bulkheadConfig.getCoreThreadPoolSize(), 1);
        Assertions.assertEquals(bulkheadConfig.getMaxThreadPoolSize(), 1);
        Assertions.assertEquals(bulkheadConfig.getQueueCapacity(), 1);
        executorService.submit(() -> {
            bulkHeadConfig.concurrentController();
        });
        executorService.submit(() -> {
            bulkHeadConfig.concurrentController();
        });
        executorService.submit(() -> {
            CompletableFuture completableFuture = bulkHeadConfig.concurrentController();
            try {
                String o = (String) completableFuture.get();
                Assertions.assertEquals(o, "rejectMessage");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        synchronized (lock) {
            lock.wait();
        }
    }
    @Test
    public void runForRetry() throws InterruptedException, ClassNotFoundException {
        String aTry = retryConfig.retry("try");
        Assertions.assertEquals(aTry,"fallback");
    }
    @Autowired
    RateLimiterRegistry rateLimiterRegistry;
    @Autowired
    RateLimitConfig rateLimitConfig;
    @Autowired
    CompositeConfig compositeConfig;


    @Test
    public void runForRateLimiter() throws InterruptedException {
        io.github.resilience4j.ratelimiter.RateLimiter aDefault = rateLimiterRegistry.rateLimiter("backendA");
        Assertions.assertTrue(aDefault instanceof AtomicRateLimiter);
        Assertions.assertTrue(aDefault.acquirePermission(1));;

        int limitForPeriod = aDefault.getRateLimiterConfig().getLimitForPeriod();
        Duration limitRefreshPeriod = aDefault.getRateLimiterConfig().getLimitRefreshPeriod();
        Assertions.assertEquals(limitForPeriod,10);;
        Assertions.assertEquals(limitRefreshPeriod,Duration.ofSeconds(1));

        Awaitility.await().atMost(2,TimeUnit.SECONDS).until(()->{return aDefault.getMetrics().getAvailablePermissions()==10;});
// AtomicRateLimiter
        for (int i=0 ;i<11;i++){
            rateLimitConfig.rateLimiterTest();
        }

        Awaitility.await().atMost(20,TimeUnit.SECONDS).until(()->{return aDefault.getMetrics().getAvailablePermissions()==10;});
        synchronized (lock){lock.wait();;}

//        eventConsumerRegistry.getEventConsumer("");
        // ratelimiterevents

//        BulkheadEventsEndpointResponse forObject = testRestTemplate.getForObject("http://localhost:9909/actuator/bulkheadevents", BulkheadEventsEndpointResponse.class);



    }
    @Test
    public void runForCompositeThatIsTooSimple() throws InterruptedException {

        ThreadPoolBulkhead aDefault = threadPoolBulkheadRegistry.bulkhead("default");
        Awaitility.await().atMost(2,TimeUnit.SECONDS).until(()->{
            int remainingQueueCapacity = aDefault.getMetrics().getRemainingQueueCapacity();
            log.info("{}",remainingQueueCapacity);
            return remainingQueueCapacity==1;
        });
        for (int i=0 ;i<1;i++){ // 0-4
            executorService.submit(()->{compositeConfig.test(true);});
        }
        synchronized (lock){lock.wait();;}
    }
    @Test
    public void runForComposite() throws InterruptedException {
        ThreadPoolBulkhead aDefault = threadPoolBulkheadRegistry.bulkhead("default");
        Assertions.assertTrue(aDefault instanceof FixedThreadPoolBulkhead);

        int maxThreadPoolSize = aDefault.getBulkheadConfig().getMaxThreadPoolSize();
        int coreThreadPoolSize = aDefault.getBulkheadConfig().getCoreThreadPoolSize();

        Assertions.assertEquals(maxThreadPoolSize,1);;
        Assertions.assertEquals(coreThreadPoolSize,1);

        Awaitility.await().atMost(2,TimeUnit.SECONDS).until(()->{
            int remainingQueueCapacity = aDefault.getMetrics().getRemainingQueueCapacity();
            log.info("{}",remainingQueueCapacity);
            return remainingQueueCapacity==1;
        });

        for (int i=0 ;i<4;i++){ // 0-4
            executorService.submit(()->{compositeConfig.test(false);});
        }

        Awaitility.await().atMost(20,TimeUnit.SECONDS).until(()->{return aDefault.getMetrics().getRemainingQueueCapacity()==1;});

        for (int i=0 ;i<4;i++){ // 0-4
            executorService.submit(()->{compositeConfig.test(true);});
        }
        Awaitility.await().atMost(20,TimeUnit.SECONDS).until(()->{return aDefault.getMetrics().getRemainingQueueCapacity()==1;});

        synchronized (lock){lock.wait();;}





    }

    @Configuration
    @EnableAutoConfiguration
    static class Dummy {
        @Bean
        public CompositeConfig compositeConfig(){
            return new CompositeConfig();
        };
        @Bean
        public Normal normal() {
            return new Normal();
        }

        @Bean
        public TimeLimiterNormal timeLimiterNormal() {
            return new TimeLimiterNormal();
        }

        @Bean
        public TimeLimiterNormalForBackEnd timeLimiterNormalForBackEnd() {
            return new TimeLimiterNormalForBackEnd();
        }

        @Bean
        public BulkHeadConfig bulkHeadConfig() {
            return new BulkHeadConfig();
        }
        @Bean
        public RetryConfig retryConfig(){return new RetryConfig();}
        @Bean
        public RateLimitConfig rateLimitConfig(){return new RateLimitConfig();}
    }

    @TimeLimiter(name = "backendA", fallbackMethod = "slowFallback")
    static class TimeLimiterNormalForBackEnd {
        public CompletableFuture slow() throws InterruptedException {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "slow";
            });
        }

        public CompletableFuture slowFallback(java.lang.Throwable throwable) {
            return CompletableFuture.completedFuture("Action is too slow");
        }
    }

    @TimeLimiter(name = "default", fallbackMethod = "slowFallback")
    static class TimeLimiterNormal {
        public CompletableFuture slow() throws InterruptedException {

            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "slow";
            });
        }

        public CompletableFuture slowFallback(java.lang.Throwable throwable) {
            return CompletableFuture.completedFuture("Action is too slow");
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallback")
    static class Normal {

        public CompletableFuture getMessage(Future future) {
            throw new RuntimeException("intending");
        }

        public String error(String param) {
            throw new RuntimeException("intending");
        }

        public void error() {
            throw new RuntimeException("intending");
        }

        public CompletableFuture fallback(Future future, Throwable throwable) {
            return CompletableFuture.completedFuture("future val");
        }

        public String fallback(String parameter, Throwable throwable) {
            return "invoke the fallback method";
        }

        public void fallback(Throwable throwable) {
            RuntimeException runtimeException = (RuntimeException) throwable;
            String message = runtimeException.getMessage();
            Assertions.assertEquals(message, "intending");
        }
    }

    static class BulkHeadConfig {
        @Bulkhead(name = "default", fallbackMethod = "fallback", type = Bulkhead.Type.SEMAPHORE)
        public CompletableFuture concurrentControllerWithSemaphore() {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hcj";
            });
        }
        @Bulkhead(name = "default", fallbackMethod = "fallback2", type = Bulkhead.Type.SEMAPHORE)
        public String controller() {
            try {
                Thread.sleep(1900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hcj";
        }
        @Bulkhead(name = "default", fallbackMethod = "fallback", type = Bulkhead.Type.THREADPOOL)
        public CompletableFuture concurrentController() {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hcj";
            });
        }
        // find the callback method.
        public String fallback2(Throwable throwable) {
            String message = throwable.getMessage();
            log.info("fallback2 message:{}",message);
            return "fallback2";
        }
        // find the callback method.
        public CompletableFuture fallback(Throwable throwable) {

            Assertions.assertTrue(throwable instanceof io.github.resilience4j.bulkhead.BulkheadFullException);
            String message = throwable.getMessage();
            log.info("message:{}",message);
            return CompletableFuture.completedFuture("rejectMessage");
        }
    }
    @Slf4j
    static class RetryConfig{

        @Retry(name = "default",fallbackMethod = "fallback")
        public String retry(String paramter) throws InterruptedException, ClassNotFoundException {
            Thread.sleep(1000);
            Class<?> aClass = Class.forName("run.predicate.CustomRetryExceptionPredicate");
            Assertions.assertNotNull(aClass);
            log.info("invoke the method!!!");
            throw new RuntimeException("intending");
        }
        // : class java.lang.String class run.RunTests$RetryConfig.fallback(class java.lang.String,class java.lang.Throwable)
        public String fallback(String paramter,java.lang.Throwable throwable){
            return "fallback";
        }
    }

    static class RateLimitConfig{

//        @Bulkhead(name = "default")
        @RateLimiter(name = "backendA",fallbackMethod = "fallback")
        public String rateLimiterTest(){
            return "rateLimiter";
        }

        public String fallback(Throwable throwable){
            Assertions.assertTrue(throwable instanceof io.github.resilience4j.ratelimiter.RequestNotPermitted);
            String message = throwable.getMessage();
            log.info(message);
            return "recover from exception ";
        }
    }
    @Slf4j
    static class CompositeConfig{
//        the order is  retry,circuitBreaker,rateLimiter,timeLimiter,bulkHead(max thread size)
// retry ->

        @TimeLimiter(name = "default")
        @RateLimiter(name = "default")
        @Retry(name = "default") // returnType:  String , exceptionType : RuntimeException
        @CircuitBreaker(name = "default")
        @Bulkhead(name = "default",type = Bulkhead.Type.THREADPOOL,fallbackMethod = "fallback")
        public CompletableFuture test(Boolean hasError){
            log.info("CompositeConfig.test method invoke ");
            if(hasError){
                throw new RuntimeException("intend");
            }
            return CompletableFuture.supplyAsync(()->{
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hcj";
            });
        }
        public CompletableFuture fallback(Boolean isFlag , Throwable throwable){
            if(isFlag){   throw new RuntimeException("intend");}
            log.info("hasError:{},error msg:{}",isFlag,throwable.getMessage());
            return CompletableFuture.completedFuture("recover from the concurrent exception");
        };
    }


}
