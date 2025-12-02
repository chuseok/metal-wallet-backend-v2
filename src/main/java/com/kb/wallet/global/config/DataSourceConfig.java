package com.kb.wallet.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.prometheus.PrometheusMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("prod")
@Slf4j
@PropertySource("classpath:application-prod.properties")
public class DataSourceConfig {

  @Value("${spring.datasource.hikari.minimum-idle}")
  private int minimumIdle;

  @Value("${spring.datasource.hikari.maximum-pool-size}")
  private int maximumPoolSize;

  @Value("${spring.datasource.hikari.connection-timeout}")
  private long connectionTimeout;

  @Value("${spring.datasource.hikari.idle-timeout}")
  private long idleTimeout;

  @Value("${spring.datasource.hikari.max-lifetime}")
  private long maxLifetime;

  @Value("${spring.datasource.driver-class-name}")
  private String driverClassName;

  @Value("${spring.datasource.url}")
  private String datasourceUrl;

  @Value("${spring.datasource.username}")
  private String datasourceUsername;

  @Value("${spring.datasource.password}")
  private String datasourcePassword;

  @Bean
  public DataSource dataSource(MeterRegistry registry) {
    HikariDataSource ds = (HikariDataSource) createHikariDataSource(
        datasourceUrl, datasourceUsername, datasourcePassword
    );

    ds.setMetricsTrackerFactory(new PrometheusMetricsTrackerFactory());

    return ds;
  }

  private DataSource createHikariDataSource(String dbUrl,
      String dbUsername,
      String dbPassword) {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(driverClassName);
    config.setJdbcUrl(dbUrl);
    config.setUsername(dbUsername);
    config.setPassword(dbPassword);
    config.setConnectionTimeout(connectionTimeout);
    config.setMinimumIdle(minimumIdle);
    config.setMaximumPoolSize(maximumPoolSize);
    config.setIdleTimeout(idleTimeout);
    config.setMaxLifetime(maxLifetime);
    config.setAutoCommit(true);

    return new HikariDataSource(config);
  }
}
