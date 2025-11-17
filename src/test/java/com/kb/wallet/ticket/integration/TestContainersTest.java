package com.kb.wallet.ticket.integration;

import com.kb.wallet.global.config.RedisConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Tag("integration")
public class TestContainersTest {
  @Container
  static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
      .withDatabaseName(System.getenv("TEST_MYSQL_DB"))
      .withUsername(System.getenv("TEST_MYSQL_USER"))
      .withPassword(System.getenv("TEST_MYSQL_PASSWORD"));
  @Container
  static GenericContainer<?> redis = new GenericContainer<>("redis:7.0.11-alpine")
      .withExposedPorts(6379);

  static RedissonClient redisson;
  static AnnotationConfigApplicationContext context;

  @BeforeAll
  static void setUp() {
    mysql.start();
    redis.start();

    String redisHost = redis.getHost();
    int redisPort = redis.getFirstMappedPort();

    System.setProperty("spring.redis.host", redisHost);
    System.setProperty("spring.redis.port", String.valueOf(redisPort));

    // DB connection properties
    System.setProperty("DATASOURCE_URL", mysql.getJdbcUrl());
    System.setProperty("DATASOURCE_USERNAME", mysql.getUsername());
    System.setProperty("DATASOURCE_PASSWORD", mysql.getPassword());

    context = new AnnotationConfigApplicationContext();

    context.register(TestDataSourceConfig.class); // MySQL
    context.register(RedisConfig.class);          // Redis

    redisson = context.getBean(RedissonClient.class);

    System.out.println("✅ TestContainers MySQL URL: " + mysql.getJdbcUrl());
    System.out.println("✅ TestContainers Redis Host:Port " + redisHost + ":" + redisPort);
  }
  @Test
  void testDatabaseConnection(DataSource dataSource) throws Exception {
    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1")) {

      while (rs.next()) {
        System.out.println("DB Test Query Result: " + rs.getInt(1));
      }
    }
  }

  public static class TestDataSourceConfig {
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
}
