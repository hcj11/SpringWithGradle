package refresh;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import static utils.Utils.print;

/**
 *  startup the apps,
 *
 */
public class RefreshScopeTest {

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    @Test
    public void refreshOnBeanWithoutRefreshScopeAnno() {
        context.register(Controller.class, PropertyPlaceholderAutoConfiguration.class,
                PropertySourcesPlaceholderConfigurer.class, RefreshAutoConfiguration.class,
                LifecycleMvcEndpointAutoConfiguration.class
        );
        context.refresh();
        Controller.OldMessage bean = context.getBean(Controller.OldMessage.class);
        String id1=bean.toString();
        String s = bean.sayHello();
        Assertions.assertEquals(s,"hello");
        RefreshAll();
        Controller.OldMessage bean2 = context.getBean(Controller.OldMessage.class);
        String s1 = bean2.sayHello();
        Assertions.assertEquals(s1,"hello");
        Assertions.assertNotEquals(id1,bean2.toString());
    }
    @Test
    public void refreshOnBean(){
        context.register(Controller.class, PropertyPlaceholderAutoConfiguration.class,
                PropertySourcesPlaceholderConfigurer.class, RefreshAutoConfiguration.class,
                LifecycleMvcEndpointAutoConfiguration.class
        );
        context.refresh();
        Controller.Message bean = context.getBean(Controller.Message.class);
        String id1=bean.toString();
        String s = bean.sayHello();
        Assertions.assertEquals(s,"hello");
        RefreshAll();
        Controller.Message bean2 = context.getBean(Controller.Message.class);
        String s1 = bean2.sayHello();
        Assertions.assertEquals(s1,"good bye");
        Assertions.assertNotEquals(id1,bean2.toString());


    }
    @Test
    public void configureWithRefreshScope(){

        context.register(Application.class, PropertyPlaceholderAutoConfiguration.class,
                PropertySourcesPlaceholderConfigurer.class, RefreshAutoConfiguration.class,
                LifecycleMvcEndpointAutoConfiguration.class
        );
        context.refresh();
        print(context);

        Application bean = context.getBean(Application.class);
        String s = bean.sayHello();
        Assertions.assertEquals(s,"hello");
        String id1=bean.toString();

        RefreshAll();
        Application bean1 = context.getBean(Application.class);
        Application bean2 = context.getBean("application",Application.class);
        Application bean3 = context.getBean("scopedTarget.application",Application.class);
        Assertions.assertTrue(context.getBeanDefinition("scopedTarget.application").getScope().equals("refresh"));;
        String id2=bean1.toString();
        // this method to verify the context has been refresh.
        Assertions.assertNotEquals(id1,id2);

        Assertions.assertEquals(bean,bean1);
        Assertions.assertEquals(bean1,bean2);
        Assertions.assertNotEquals(bean1,bean3);
        Assertions.assertEquals(bean1.sayHello(),"good bye");



    }

    private void RefreshAll(){
        // scopedTarget.appliction => refresh the single bean,
        // resume load the dataSource or , others bean .
        // and how to invoke the refreshAll . to send the refreshEvent ,
        EnvironmentManager environmentManager = context.getBean(EnvironmentManager.class);
        environmentManager.setProperty("message", "good bye");
        org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope =
                context.getBean(org.springframework.cloud.context.scope.refresh.RefreshScope.class);
        refreshScope.refreshAll();
    }

    @Configuration(value = "application",proxyBeanMethods = true)
    @RefreshScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public static class Application{
        @Value("${message:hello}")
        public String msg;

        public String sayHello(){
            return msg;
        }

    }
    @Configuration
    public static class Controller{
        @RefreshScope
        @Bean
        public Message msg(){
           return new Message();
        }
        @Bean
        public OldMessage oldMessage(){return new OldMessage();}

        public static class OldMessage{
            @Value("${message:hello}")
            public String msg;

            public String sayHello(){
                return msg;
            }
        }

       public static class Message{
           @Value("${message:hello}")
           public String msg;

           public String sayHello(){
               return msg;
           }
       }

    }


}
