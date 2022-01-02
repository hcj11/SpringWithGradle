package boot;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
    @EnableAutoConfiguration
    @Configuration
    @Data
    class Empty {
    }

    @Test
    public void runner() {
        String[] args = new String[]{"version=1.9.0","spring.active.profile=dev","spring.default.profile=dev"
        ,"--foo=bar"};
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(Empty.class,
                ApplicationRunner.class);
        springApplicationBuilder.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("runing: {},{}", args.getSourceArgs(),args);
    }
}
