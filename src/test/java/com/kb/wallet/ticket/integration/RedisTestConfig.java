package com.kb.wallet.ticket.integration;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RedisTestConfig {
  @Bean
  @Primary
  public RedissonClient redissonClient() {
    return TestContainersTest.redisson;
  }
}
