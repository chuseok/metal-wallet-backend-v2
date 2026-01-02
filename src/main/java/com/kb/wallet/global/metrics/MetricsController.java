package com.kb.wallet.global.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prometheus")
@Slf4j
public class MetricsController {
  @Autowired
  private PrometheusMeterRegistry registry;

  @GetMapping(produces = "text/plain")
  public String scope() {
    log.error("Metrics endpoint registry identity={}",
      System.identityHashCode(registry));
    return registry.scrape();
  }
}
