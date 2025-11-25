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

  @Container
  private static final MySQLContainer<?> mysql =
      new MySQLContainer<>("mysql:8.0")
          .withDatabaseName(System.getenv("TEST_MYSQL_DB"))
          .withUsername(System.getenv("TEST_MYSQL_USER"))
          .withPassword(System.getenv("TEST_MYSQL_PASSWORD"));

  @Bean
  public DataSource dataSource() {
    String url = mysql.getJdbcUrl();
    String log4jdbcUrl = url.replace(
        "jdbc:mysql:",
        "jdbc:log4jdbc:mysql:"
    ) + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true";

    return new HikariDataSource(
        new HikariConfig() {{
          setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
          setJdbcUrl(log4jdbcUrl);
          setUsername(mysql.getUsername());
          setPassword(mysql.getPassword());
          setMaximumPoolSize(5);
          setMinimumIdle(1);
        }}
    );
  }
}