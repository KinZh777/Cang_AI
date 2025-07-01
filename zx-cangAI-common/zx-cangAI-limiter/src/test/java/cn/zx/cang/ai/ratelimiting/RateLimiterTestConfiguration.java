package cn.zx.cang.ai.ratelimiting;

import cn.zx.cang.ai.limiter.SlidingWindowRateLimiter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author kinchou
 */
@AutoConfiguration
public class RateLimiterTestConfiguration {

    @Value("${redis.serverAddress}")
    private String redisAddress;

    @Value("${redis.serverPassword}")
    private String redisPassword;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisAddress).setPassword(redisPassword);
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public SlidingWindowRateLimiter slidingWindowRateLimiter(RedissonClient redisson) {
        return new SlidingWindowRateLimiter(redisson);
    }
}
