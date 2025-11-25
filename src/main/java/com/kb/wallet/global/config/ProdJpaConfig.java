package com.kb.wallet.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@Profile("prod")
@PropertySource("classpath:application-prod.properties")
@Slf4j
public class ProdJpaConfig {

  private final String hbm2ddlAuto = "none";
  private final boolean showSql = false;

  @Bean
  public Properties jpaProperties() {
    Properties properties = new Properties();
    properties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
    properties.put("hibernate.show_sql", showSql);
    properties.put("hibernate.physical_naming_strategy",
        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
    log.info("prod: {}", properties.get("hibernate.hbm2ddl.auto"));
    return properties;
  }
}
