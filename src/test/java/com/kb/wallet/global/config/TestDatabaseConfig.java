package com.kb.wallet.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Configuration
@Profile("test")
public class TestDatabaseConfig {
  @Autowired
  private Environment env;

  @Bean
  public DataSource dataSource() {
    String url = env.getProperty("spring.datasource.url");
    String log4jdbcUrl = url.replace(
        "jdbc:mysql:",
        "jdbc:log4jdbc:mysql:"
    ) + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true";
    String username = env.getProperty("spring.datasource.username");
    String password = env.getProperty("spring.datasource.password");

    return new HikariDataSource(

    new HikariConfig() {{
          setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
          setJdbcUrl(log4jdbcUrl);
          setUsername(username);
          setPassword(password);
          setMaximumPoolSize(5);
          setMinimumIdle(1);
        }}
    );
  }
}