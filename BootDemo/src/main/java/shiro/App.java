package shiro;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import shiro.config.AopConfig;
import shiro.config.TemplateConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;
/**
 * MapperAnnotationBuilder and XmlMapperBuilder
 *
 */
@interface CustomAsync{};
/**
 * 默认 cglib动态代理，
 * 或者aspectJ 代理。
 */
@EnableAsync(annotation = CustomAsync.class)
@Slf4j
@Configuration
@ControllerAdvice
@SpringBootApplication
public class App {
    /**
     * 通过beanfactory 上的bean （proxy） 动态代理生成， 当调用方法之前，需要先执行interceptor chain,
     *  before  original method  after  处理方式由 切面完成，  异常则捕获。并调用目标方法，或者返回。
     * httpclient .
     */
    @CustomAsync
    public void asyncRun(){

    }

    /**
     * 注册成一个bean
     */
    @Component
    public static class CustomApplicationListener implements ApplicationListener {

        @Override
        public void onApplicationEvent(ApplicationEvent event) {

        }
    }

    /**
     * 不需要aop 进行 check for
     */
    @EventListener
    public void listener(ApplicationEvent applicationEvent){
        applicationEvent.getSource();
    }


    public static void main(String[] args) throws IOException, TemplateException, URISyntaxException {

        ConfigurableApplicationContext run = SpringApplication.run(App.class);
        TemplateConfig bean = run.getBean(TemplateConfig.class);

    }

}
