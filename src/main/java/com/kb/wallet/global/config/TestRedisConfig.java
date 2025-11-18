package com.kb.wallet.global.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestRedisConfig {

  @Bean
  public RedissonClient redissonClient() {
    String host = System.getProperty("test.redis.host");
    String port = System.getProperty("test.redis.port");

    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://" + host + ":" + port);

    return Redisson.create(config);
  }
}
