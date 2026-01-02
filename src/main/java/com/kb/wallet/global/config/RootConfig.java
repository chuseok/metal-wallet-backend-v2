package com.kb.wallet.global.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
    basePackages = "com.kb.wallet",
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class)
)
@Import({
    AppConfig.class,
    ProdJpaConfig.class,
    DataSourceConfig.class,
    MetricsConfig.class,
    RedisConfig.class,
    SecurityConfig.class
})
public class RootConfig {
}