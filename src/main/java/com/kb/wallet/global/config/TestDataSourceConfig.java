package com.kb.wallet.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataSourceConfig {
  @Bean
  public DataSource dataSource() {
    String url = System.getProperty("DATASOURCE_URL");
    String username = System.getProperty("DATASOURCE_USERNAME");
    String password = System.getProperty("DATASOURCE_PASSWORD");

    HikariConfig config = new HikariConfig();
    config.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
    config.setJdbcUrl(url);
    config.setUsername(username);
    config.setPassword(password);
    config.setMaximumPoolSize(5);
    config.setMinimumIdle(1);
    return new HikariDataSource(config);
  }
}
