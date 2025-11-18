package com.kb.wallet.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Slf4j
public class DataSourceConfig {

  /**
   * XXX:
   * @Configuration의 중복 처리: DataSourceConfig가 @Configuration을 사용하고 있으므로 Spring은 이 설정 파일들을 스캔할 때
   * @Profile에 따라 적절한 DataSource 설정을 자동으로 선택한다.
   *
   * 프로파일이 "test"일 때 DataSourceConfig.TestConfig가 활성화되고, application-test.properties 파일에서 값을 가져와
   * TestConfig의 dataSource() 메서드가 DataSource 빈으로 등록된다.
   */

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

  @Configuration
  @Profile("dev")
  @PropertySource("classpath:application-dev.properties")
  class DevConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Bean
    public DataSource dataSource() {
      return createHikariDataSource(datasourceUrl, datasourceUsername, datasourcePassword);
    }
  }

  @Configuration
  @Profile("prod")
  @PropertySource("classpath:application-prod.properties")
  class ProdConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Bean
    public DataSource dataSource() {
      return createHikariDataSource(datasourceUrl, datasourceUsername, datasourcePassword);
    }
  }

  @Configuration
  @Profile("test")
  @PropertySource("classpath:application-test.properties")
  public static class TestDataSourceConfig {
    @Bean
    public DataSource dataSource() {
      String url = System.getProperty("spring.datasource.url");
      String log4jdbcUrl = url.replace(
          "jdbc:mysql:",
          "jdbc:log4jdbc:mysql:"
      ) + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true";
      String username = System.getProperty("spring.datasource.username");
      String password = System.getProperty("spring.datasource.password");

      HikariConfig config = new HikariConfig();

      config.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
      config.setJdbcUrl(log4jdbcUrl);
      config.setUsername(username);
      config.setPassword(password);
      config.setMaximumPoolSize(5);
      config.setMinimumIdle(1);
      return new HikariDataSource(config);
    }
  }

  private DataSource createHikariDataSource(String dbUrl,
      String dbUsername, String dbPassword) {
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