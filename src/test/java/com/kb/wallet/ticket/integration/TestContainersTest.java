package com.kb.wallet.ticket.integration;

import com.kb.wallet.global.config.TestDataSourceConfig;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestDataSourceConfig.class })
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

  @BeforeAll
  static void setUp() {
    mysql.start();
    redis.start();

    String redisHost = redis.getHost();
    int redisPort = redis.getFirstMappedPort();

    System.setProperty("REDIS_HOST", redisHost);
    System.setProperty("REDIS_PORT", String.valueOf(redisPort));

    // DB connection properties
    System.setProperty("DATASOURCE_URL", mysql.getJdbcUrl());
    System.setProperty("DATASOURCE_USERNAME", mysql.getUsername());
    System.setProperty("DATASOURCE_PASSWORD", mysql.getPassword());

    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://" + redisHost + ":" + redisPort);
    redisson = Redisson.create(config);

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
}
