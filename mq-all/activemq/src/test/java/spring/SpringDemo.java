package spring;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.*;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.Utils;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDemo.SimpleDummy.class})
public class SpringDemo {
    @Autowired
    private ConfigurableApplicationContext context;
    Object lock = new Object();

    private JmsTemplate createJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        return jmsTemplate;
    }

    public static CustomBoolean mock = null;
    @Before
    public void setup(){
         mock = mock(CustomBoolean.class);
         /**
          * 默认是6次以上进入死信队列，后commit该事务，不进行回滚。
          * 事后，可以拉取，并进行处理，而不会影响该队列后面的 msg.
          */
         when(mock.isFlag()).thenReturn(false,false,false,false,false,false,false,true);

    }
    @Data
    public static class CustomBoolean{
        private boolean flag;
    }

    @Test
    public void sendMsgWithInTransactionListener() throws InterruptedException {



        ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);

        JmsTemplate jmsTemplate = createJmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestinationName("simplequeueId");

        jmsTemplate.execute((Session session, MessageProducer producer) -> {
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText("hello world!!!");
            producer.send(textMessage);
            return "";
        });
        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void loadConfig() throws InterruptedException {
        Assert.assertNotNull(context);
        Utils.print(context);
        // JmsListenerEndpoint bean. SimpleMessageListenerContainer
        //  in case of the id  of lisenter. create endpoint
        SimpleJmsListenerContainerFactory bean = context.getBean(SimpleJmsListenerContainerFactory.class);
        JmsListenerContainerTestFactory bean1 = context.getBean(JmsListenerContainerTestFactory.class);
        JmsListenerEndpoint endpointId = bean1.getListenerContainer("endpointId").getEndpoint();
        Assert.assertNotNull(endpointId);
        Assert.assertTrue(bean1.getListenerContainers().size() == 2);


        synchronized (lock) {
            lock.wait();
        }

    }

    @Slf4j
    @EnableJms
    @Configuration
    @Import(SimpleDummy.CustomListner.class)
    public static class SimpleDummy {
        @Slf4j
        public static class CustomListner {
            @JmsListener(id = "simpleId", destination = "simplequeueId", containerFactory = "customFactory")
            public void receiveMsgWithSimpleFactory(String msg) {
                /**
                 * can nest the other transaction.
                 * when exception ,to rollback.
                 */
                if(mock.isFlag()){
                    log.info("receive msg with simpleFactory  is {}", msg);
                }else{
                    throw new RuntimeException("intend!!!");
                }
                // clientAck. and to rollback.
            }
        }

        @Bean
        public ConnectionFactory connectionFactory() {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory();
            singleConnectionFactory.setExceptionListener(new ExceptionListener() {
                @Override
                public void onException(JMSException exception) {
                    exception.printStackTrace();
                }
            });
            singleConnectionFactory.setReconnectOnException(true);

            singleConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
            return singleConnectionFactory;
        }

        @Bean
        public JmsListenerEndpointRegistry endpointRegistry() {
            return new JmsListenerEndpointRegistry();
        }

        @Bean
        public SimpleJmsListenerContainerFactory customFactory(ConnectionFactory connectionFactory) {
            SimpleJmsListenerContainerFactory simpleJmsListenerContainerFactory = new SimpleJmsListenerContainerFactory();
            simpleJmsListenerContainerFactory.setConnectionFactory(connectionFactory);
            simpleJmsListenerContainerFactory.setSessionAcknowledgeMode(2);
            simpleJmsListenerContainerFactory.setSessionTransacted(true);
            return simpleJmsListenerContainerFactory;
        }
    }

    @Slf4j
    @EnableJms
    @Configuration
    @Import(CustomListner.class)
    public static class Dummy implements JmsListenerConfigurer {
        @Bean
        public JmsListenerContainerFactory jmsListenerContainerFactory() {
            JmsListenerContainerTestFactory jmsListenerContainerTestFactory = new JmsListenerContainerTestFactory();
            return jmsListenerContainerTestFactory;
        }

        @Bean
        public ConnectionFactory connectionFactory() {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory();
            singleConnectionFactory.setExceptionListener(new ExceptionListener() {
                @Override
                public void onException(JMSException exception) {
                    exception.printStackTrace();
                }
            });
            singleConnectionFactory.setReconnectOnException(true);

            singleConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
            return singleConnectionFactory;
        }

        @Bean
        public SimpleJmsListenerContainerFactory customFactory(ConnectionFactory connectionFactory) {
            // JmsListenerContainerTestFactory
            SimpleJmsListenerContainerFactory simpleJmsListenerContainerFactory = new SimpleJmsListenerContainerFactory();
            simpleJmsListenerContainerFactory.setConnectionFactory(connectionFactory);
            return simpleJmsListenerContainerFactory;
        }

        @Bean
        public JmsListenerEndpointRegistry endpointRegistry() {
            return new JmsListenerEndpointRegistry();
        }

        @Override
        public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
            registrar.setEndpointRegistry(endpointRegistry());

            SimpleJmsListenerEndpoint simpleJmsListenerEndpoint = new SimpleJmsListenerEndpoint();
            simpleJmsListenerEndpoint.setId("customId");
            simpleJmsListenerEndpoint.setDestination("customQueueId");
            simpleJmsListenerEndpoint.setMessageListener(listener());
            registrar.registerEndpoint(simpleJmsListenerEndpoint);
            /**
             *  use defaut  jmsListenerContainerFactory ;
             */

        }

        @Bean
        public MessageListenerAdapter listener() {
            return new MessageListenerAdapter();
        }
    }

    @Slf4j
    public static class CustomListner {

        @JmsListener(id = "defaultId", destination = "defaultqueueId", concurrency = "jmsListenerContainerFactory")
        public void receiveMsgWithJmsListenerContainerFactory(String msg) {
            log.info("receive msg with jmsListenerContainerFactory  is {}", msg);
            // autoAck.
        }

        @JmsListener(id = "simpleId", destination = "simplequeueId", containerFactory = "customFactory")
        public void receiveMsgWithSimpleFactory(String msg) {
            log.info("receive msg with simpleFactory  is {}", msg);
        }

    }


}
