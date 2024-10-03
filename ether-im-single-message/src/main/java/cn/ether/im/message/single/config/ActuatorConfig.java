package cn.ether.im.message.single.config;

import org.springframework.boot.actuate.redis.RedisHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class ActuatorConfig {

    @Bean
    public RedisHealthIndicator redisHealthIndicator(RedisConnectionFactory redisConnectionFactory) {
        return new RedisHealthIndicator(redisConnectionFactory);
    }
}