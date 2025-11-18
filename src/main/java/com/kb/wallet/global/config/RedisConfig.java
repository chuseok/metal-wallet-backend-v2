package com.kb.wallet.global.config;

import java.util.HashMap;
import java.util.Map;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
@Profile("prod")
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  private static final String REDISSON_HOST_PREFIX = "redis://";

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port);

    return Redisson.create(config);
  }
  @Bean
  CacheManager cacheManager(RedissonClient redissonClient) {
    Map config = new HashMap<>();

    // create "testMap" spring cache with ttl = 24 minutes and maxIdleTime = 12 minutes
    config.put("testMap", new CacheConfig(24*60*1000, 12*60*1000));
    return new RedissonSpringCacheManager(redissonClient, config);
  }

  @Bean
  public RedissonConnectionFactory redisConnectionFactory(RedissonClient redissonClient) {
    return new RedissonConnectionFactory(redissonClient);
  }
}