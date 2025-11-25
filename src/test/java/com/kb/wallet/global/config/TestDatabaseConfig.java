package com.kb.wallet.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Profile("test")
public class TestDatabaseConfig {
  @Autowired
  private Environment env;

  @Bean
  public DataSource dataSource() {
    String url = env.getProperty("spring.datasource.url");
    String log4jdbcUrl = url.replace("jdbc:mysql:", "jdbc:log4jdbc:mysql:");
    String username = env.getProperty("spring.datasource.username");
    String password = env.getProperty("spring.datasource.password");

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