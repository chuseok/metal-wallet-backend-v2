package com.kb.wallet.global.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ProfileInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    applicationContext.getEnvironment().setActiveProfiles("prod");  // refresh 전에 적용됨
  }
}