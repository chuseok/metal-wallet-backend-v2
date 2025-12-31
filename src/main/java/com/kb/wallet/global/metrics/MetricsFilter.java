package com.kb.wallet.global.metrics;

import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import java.io.IOException;
import javax.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MetricsFilter implements Filter {

  private final HttpRequestTimer httpRequestTimer;
  private final PrometheusMeterRegistry registry;

  public MetricsFilter(HttpRequestTimer httpRequestTimer, PrometheusMeterRegistry registry) {
    this.httpRequestTimer = httpRequestTimer;
    this.registry = registry;
  }

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {

    Timer.Sample sample = Timer.start(registry);
    log.info(">>> MetricsFilter called");
    try {
      chain.doFilter(request, response);
    } finally {
      sample.stop(httpRequestTimer.getTimer());
    }
  }
}
