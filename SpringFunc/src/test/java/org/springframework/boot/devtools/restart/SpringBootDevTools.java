package org.springframework.boot.devtools.restart;

import com.alibaba.druid.sql.parser.SQLParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class SpringBootDevTools {

    private static final String[] ARGS = new String[]{"a", "b", "c"};

    private void testInitialize(boolean failed) {
        Restarter.clearInstance();
        RestartApplicationListener listener = new RestartApplicationListener();
        SpringApplication application = new SpringApplication();
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
        listener.onApplicationEvent(new ApplicationStartingEvent(application, ARGS));
        assertThat(Restarter.getInstance()).isNotEqualTo(nullValue());
        assertThat(Restarter.getInstance().isFinished()).isFalse();
        listener.onApplicationEvent(new ApplicationPreparedEvent(application, ARGS, context));
        if (failed) {
            listener.onApplicationEvent(new ApplicationFailedEvent(application, ARGS, context, new RuntimeException()));
        } else {
            listener.onApplicationEvent(new ApplicationReadyEvent(application, ARGS, context));
        }
    }

    @Test
    public void try1() {
        System.setProperty("spring.devtools.restart.enabled", "true");
        testInitialize(false);
        assertThat(Restarter.getInstance()).hasFieldOrPropertyWithValue("args", ARGS);
        assertThat(Restarter.getInstance().isFinished()).isTrue();
        assertThat((List<?>) ReflectionTestUtils.getField(Restarter.getInstance(), "rootContexts")).isNotEmpty();
        // send restart event
    }

}
