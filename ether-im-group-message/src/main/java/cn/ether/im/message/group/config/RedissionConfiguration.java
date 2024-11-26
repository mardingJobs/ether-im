package cn.ether.im.message.group.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfiguration {

    @Value(value = "${spring.redis.host}")
    private String host;

    @Value(value = "${spring.redis.port}")
    private Integer port;

    @Value(value = "${spring.redis.database}")
    private Integer database;

//    @Value(value = "${spring.redis.password}")
//    private String password;

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        String address = "redis://" + host + ":" + port;
        singleServerConfig.setAddress(address);
        singleServerConfig.setDatabase(database);
        //singleServerConfig.setPassword(password);
        return Redisson.create(config);
    }

}
