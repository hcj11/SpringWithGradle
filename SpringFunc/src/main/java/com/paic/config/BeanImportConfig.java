package com.paic.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.stereotype.Component;
@Data
class Beans{

}
@Import(value = {RedisAutoConfiguration.class})
@Configuration
public class BeanImportConfig {

    @Import(value={DataSourceTransactionManagerAutoConfiguration.class})
    @Component
    static class SubBeanImportConfig{

    }

}

