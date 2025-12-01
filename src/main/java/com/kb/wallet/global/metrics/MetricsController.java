package com.kb.wallet.global.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prometheus")
public class MetricsController {
  @Autowired
  private PrometheusMeterRegistry registry;

  @GetMapping
  public String scope() {
    return registry.scrape();
  }
}
