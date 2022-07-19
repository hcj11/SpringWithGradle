package run;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@SpringBootTest(classes = RedisRateConfig.Dummy.class)
public class RedisRateConfig {
    @Autowired
    private RedisRateLimiter redisRateLimiter;

    @Test
    public void verfiy(){
        String routeId="gateway_circuit_breaker";
        Map<String, RedisRateLimiter.Config> config = redisRateLimiter.getConfig();
        RedisRateLimiter.Config gateway_circuit_breaker = config.get("gateway_circuit_breaker");
        int replenishRate = gateway_circuit_breaker.getReplenishRate();
        int burstCapacity = gateway_circuit_breaker.getBurstCapacity();
        int requestedTokens = gateway_circuit_breaker.getRequestedTokens();
        Assertions.assertEquals(replenishRate,70);
        Assertions.assertEquals(burstCapacity,80);
        Assertions.assertEquals(requestedTokens,10);

    }

    @Configuration
    @EnableAutoConfiguration
    static class Dummy{}

}
