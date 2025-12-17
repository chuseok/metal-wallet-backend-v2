package com.kb.wallet.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class ProfileInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
//    applicationContext.getEnvironment().setActiveProfiles("prod");  // refresh 전에 적용됨


    String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
    log.info("Root WebApplicationContext: initialization started");
    log.info("Active profiles: {}", String.join(", ", activeProfiles));
  }
}