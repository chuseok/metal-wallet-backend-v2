package com.kb.wallet.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@DependsOn("dataSource")
@PropertySource(value = "classpath:application-prod.properties")
@ComponentScan(basePackages = {
    "com.kb.wallet"
})
@MapperScan(
    basePackages = {
        "com.kb.wallet.member.repository",
        "com.kb.wallet.ticket.repository",
        "com.kb.wallet.seat.repository",
        "com.kb.wallet.musical.repository"
    },
    annotationClass = org.apache.ibatis.annotations.Mapper.class //해당패키지에서 @Mapper어노테이션이 선언된 인터페이스 찾기
)
@EnableJpaRepositories(basePackages = {
    "com.kb.wallet.member.repository",
    "com.kb.wallet.ticket.repository",
    "com.kb.wallet.seat.repository",
    "com.kb.wallet.musical.repository",
    "com.kb.wallet.account.repository"

})
@EnableJpaAuditing
@EnableTransactionManagement
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class AppConfig {
  private final DataSource dataSource;
  private final Properties jpaProperties;
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()); // LocalDate와 LocalDateTime을 지원
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
        false); // 날짜를 타임스탬프가 아닌 ISO 8601 형식으로 출력
    return objectMapper;
  }

  // JPA 설정
  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource);
    emf.setPackagesToScan("com.kb.wallet.member.domain", "com.kb.wallet.ticket.domain",
        "com.kb.wallet.seat.domain", "com.kb.wallet.musical.domain",
        "com.kb.wallet.account.domain");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    // JPA Properties 설정
    emf.setJpaProperties(jpaProperties);
    return emf;
  }

  @Bean
  @Primary
  public PlatformTransactionManager jpaTransactionManager(
      LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
    return jpaTransactionManager;
  }

  // MyBatis 설정
  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setTypeAliasesPackage("com.kb.wallet.member.domain,"
        + "com.kb.wallet.ticket.domain,"
        + "com.kb.wallet.seat.domain,"
        + "com.kb.wallet.musical.domain");

    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
        "classpath*:mapper/**/*.xml"));  // MyBatis 매퍼 설정

    org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
    configuration.setAutoMappingBehavior(
        org.apache.ibatis.session.AutoMappingBehavior.PARTIAL); // Set AUTO_MAPPING_BEHAVIOR to PARTIAL
    configuration.setMapUnderscoreToCamelCase(true);
    sessionFactory.setConfiguration(configuration);

    return sessionFactory.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(dataSource);
  }

  // MyBatis 트랜잭션 매니저
  @Bean
  public PlatformTransactionManager myBatisTransactionManager() {
    return new DataSourceTransactionManager(dataSource);
  }

  // 두 트랜잭션 매니저를 ChainedTransactionManager로 묶음
  @Bean
  public PlatformTransactionManager transactionManager(
      @Qualifier("jpaTransactionManager") PlatformTransactionManager jpaTransactionManager,
      @Qualifier("myBatisTransactionManager") PlatformTransactionManager myBatisTransactionManager) {
    return new ChainedTransactionManager(jpaTransactionManager, myBatisTransactionManager);
  }

  /*
   * sql 초기 데이터 세팅
   * 로컬에서 개발하며 테스트가 필요할 때만 활성화해서 사용할 것
   */
  /*@Bean
  public DataSourceInitializer dataSourceInitializer() {
    // HACK: db/data.sql 파일이 두 번 실행되는 문제 확인 및 수정 필요
    // 현재 DataSourceInitializer로 인해 중복 데이터 삽입이 발생하며,
    // Duplicate entry 오류가 발생하고 있음 (예: 'test1234@gmail.com').
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("db/data.sql"));

    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(populator);
    return initializer;
  }

  @Bean
  public org.springframework.context.ApplicationListener<ContextRefreshedEvent> contextRefreshedEventListener(
      DataSourceInitializer initializer) {
    return event -> {
      // JPA 초기화가 완료된 후 DataSourceInitializer 실행
      initializer.afterPropertiesSet();
    };
  }*/
}
