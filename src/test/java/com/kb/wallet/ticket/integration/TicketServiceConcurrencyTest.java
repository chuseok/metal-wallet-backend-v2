package com.kb.wallet.ticket.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kb.wallet.global.config.AppConfig;
import com.kb.wallet.global.config.TestConfig;
import com.kb.wallet.member.domain.Member;
import com.kb.wallet.member.repository.MemberRepository;
import com.kb.wallet.musical.domain.Musical;
import com.kb.wallet.musical.repository.MusicalRepository;
import com.kb.wallet.seat.constant.Grade;
import com.kb.wallet.seat.domain.Seat;
import com.kb.wallet.seat.domain.Section;
import com.kb.wallet.seat.repository.SeatRepository;
import com.kb.wallet.seat.repository.SectionRepository;
import com.kb.wallet.ticket.domain.*;
import com.kb.wallet.ticket.dto.request.TicketRequest;
import com.kb.wallet.ticket.repository.*;
import com.kb.wallet.ticket.service.TicketService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@Tag("integration")
class TicketServiceConcurrencyTest {

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

  @Autowired
  private TicketService ticketService;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private MusicalRepository musicalRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;
  @Autowired
  private SectionRepository sectionRepository;
  @Autowired
  private SeatRepository seatRepository;
  @Autowired
  private TicketRepository ticketRepository;

  private Long testSeatId;
  private Long testSectionId;
  private final int testMemberCnt = 100;
  private final int testAvailableSeats = 100;

  Section section;
  Schedule schedule;
  Musical musical;

  @BeforeEach
  void setUpBeforeEach() {
    cleanUpAll();
    createTestMusicalScheduleSection();

  }

  private void createTestMusicalScheduleSection() {
    musical = musicalRepository.save(new Musical(
        1L,
        "킹키부츠",
        1,
        "서울",
        "서울 아트센터",
        LocalDate.parse("2024-10-01"),
        LocalDate.parse("2024-10-16"),
        150,
        null,
        null,
        null,
        null
    ));

    schedule = scheduleRepository.save(new Schedule(
        null,
        LocalDate.parse("2024-10-17"),
        musical,
        LocalTime.parse("10:00:00"),
        LocalTime.parse("12:30:00")
    ));

    section = sectionRepository.save(new Section(
        1L,
        musical,
        schedule,
        Grade.R,
        19000,
        testAvailableSeats
    ));

    testSectionId = section.getId();
  }

  @BeforeAll
  void setUp() {
    initTestcontainers();
    insertMemberData();
  }

  private void initTestcontainers() {
    mysql.start();
    redis.start();

    System.setProperty("test.redis.host", redis.getHost());
    System.setProperty("test.redis.port", String.valueOf(redis.getFirstMappedPort()));

    System.setProperty("DATASOURCE_URL", mysql.getJdbcUrl());
    System.setProperty("DATASOURCE_USERNAME", mysql.getUsername());
    System.setProperty("DATASOURCE_PASSWORD", mysql.getPassword());

    context = new AnnotationConfigApplicationContext();
    context.register(TestRedisConfig.class);
    context.register(TestConfig.class);
    context.refresh();

    redisson = context.getBean(RedissonClient.class);

    System.out.println("🔧 MySQL URL: " + mysql.getJdbcUrl());
    System.out.println("🔧 Redis Host:Port " + redis.getHost() + ":" + redis.getFirstMappedPort());
  }

  @Transactional
  void insertMemberData() {
    for (int i = 1; i <= testMemberCnt; i++) {
      String phoneNumber = String.format("010%08d", i);
      memberRepository.save(new Member(
          "test" + i + "@gmail.com",
          "test" + i,
          phoneNumber,
          "password" + i,
          String.format("%06d", i)
      ));
    }
  }

  @AfterEach
  void tearDown() {
    ticketRepository.deleteAll();
  }

  void cleanUpAll() {
    ticketRepository.deleteAll();
    seatRepository.deleteAll();
    sectionRepository.deleteAll();
    scheduleRepository.deleteAll();
    musicalRepository.deleteAll();
  }

  @Test
  void testDatabaseConnection() throws Exception {
    DataSource dataSource = context.getBean(DataSource.class);
    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1")) {

      while (rs.next()) {
        System.out.println("DB Test Query Result: " + rs.getInt(1));
      }
    }
  }

  @Test
  @DisplayName("100명이 동시에 티켓 예매 시 단 1명만 성공")
  void testBookTicket_multipleUsersSingleSeatSuccess() throws InterruptedException {
    //given
    Seat seat1 = seatRepository.save(new Seat(
        1L,
        1,
        section,
        schedule,
        true
    ));
    testSeatId = seat1.getId();

    int threadCount = testMemberCnt;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    List<Future<BookResult>> results = new ArrayList<>();

    //when
    for (int i = 1; i <= threadCount; i++) {
      String email = "test" + i + "@gmail.com";
      TicketRequest request = new TicketRequest();
      request.setDeviceId("deviceID" + i);
      request.setSeatId(Collections.singletonList(testSeatId));

      results.add(executor.submit(() -> {
        try {
          ticketService.bookTicket(email, request);
          return new BookResult(email, true, "success");
        } catch (Exception e) {
          return new BookResult(email, false, e.getMessage());
        } finally {
          latch.countDown();
        }
      }));
    }

    latch.await();
    executor.shutdown();
    
    int successCount = 0;
    for(Future<BookResult> future: results) {
      try {
        BookResult result = future.get();
        if(result.isSuccess())
          successCount++;
        else
          System.err.println(result.getEmail() + ": "+ "Exception: " + result.getMessage());
      } catch (ExecutionException e) {
        System.err.println("ExecutionException: " + e.getMessage());
      }
    }
    System.out.println("successCount: " + successCount);
    int availableSeats = sectionRepository.findById(testSectionId).get().getAvailableSeats();

    //then
    assertEquals(99, availableSeats, "가용 가능한 좌석 수는 99이여야 합니다.");
    assertEquals(1, ticketRepository.count(), "티켓 테이블에 단 1건의 데이터만 존재해야 합니다.");
  }


  public static class TestDataSourceConfig {
    @Bean
    public DataSource dataSource() {
      String url = System.getProperty("DATASOURCE_URL");
      String log4jdbcUrl = url.replace(
          "jdbc:mysql:",
          "jdbc:log4jdbc:mysql:"
      ) + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true";
      String username = System.getProperty("DATASOURCE_USERNAME");
      String password = System.getProperty("DATASOURCE_PASSWORD");

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

  @Configuration
  public class TestRedisConfig {

    @Bean
    public RedissonClient redissonClient() {
      String host = System.getProperty("test.redis.host");
      String port = System.getProperty("test.redis.port");

      Config config = new Config();
      config.useSingleServer()
          .setAddress("redis://" + host + ":" + port);

      return Redisson.create(config);
    }
  }

  /*@Test
  @DisplayName("100명이 100개의 좌석을 예매할 때 재고가 정상적으로 감소")
  void testBookTicket_multipleUsersMulripleSeatsSuccess() throws InterruptedException {
    //given
    int seatCount = 100;
    for(long i=1L; i <=100L; i++) {
      seatRepository.save(new Seat(
          i,
          (int) i,
          section,
          schedule,
          true
      ));
    }

    ExecutorService executor = Executors.newFixedThreadPool(seatCount);
    CountDownLatch latch = new CountDownLatch(seatCount);

    List<Future<BookResult>> results = new ArrayList<>();

    //when
    for (int i = 1; i <= seatCount; i++) {
      String email = "test" + i + "@gmail.com";
      TicketRequest request = new TicketRequest();
      request.setDeviceId("deviceID" + i);
      request.setSeatId(Collections.singletonList((long) i));

      results.add(executor.submit(() -> {
        try {
          ticketService.bookTicket(email, request);
          return new BookResult(email, true, "success");
        } catch (Exception e) {
          return new BookResult(email, false, e.getMessage());
        } finally {
          latch.countDown();
        }
      }));
    }

    latch.await();
    executor.shutdown();

    //then
    int availableSeats = sectionRepository.findById(testSectionId).get().getAvailableSeats();
    assertEquals(0, availableSeats, "가용 좌석 수는 0이어야 합니다.");

    long bookedSeatCount = seatRepository.findAll().stream().filter(seat -> !seat.isAvailable()).count();
    assertEquals(seatCount, bookedSeatCount, "예약된 좌석 수는 총 " + seatCount + "이어야 합니다.");


    long ticketCount = ticketRepository.count();
    assertEquals(seatCount, ticketCount, "티켓 테이블에는 " + seatCount + "개의 데이터가 있어야 합니다.");
  }*/
}