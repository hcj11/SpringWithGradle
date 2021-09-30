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
 * Ĭ�� cglib��̬����
 * ����aspectJ ����
 */
@EnableAsync(annotation = CustomAsync.class)
@Slf4j
@Configuration
@ControllerAdvice
@SpringBootApplication
public class App {
    /**
     * ͨ��beanfactory �ϵ�bean ��proxy�� ��̬�������ɣ� �����÷���֮ǰ����Ҫ��ִ��interceptor chain,
     *  before  original method  after  ����ʽ�� ������ɣ�  �쳣�򲶻񡣲�����Ŀ�귽�������߷��ء�
     * httpclient .
     */
    @CustomAsync
    public void asyncRun(){

    }

    /**
     * ע���һ��bean
     */
    @Component
    public static class CustomApplicationListener implements ApplicationListener {

        @Override
        public void onApplicationEvent(ApplicationEvent event) {

        }
    }

    /**
     * ����Ҫaop ���� check for
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
