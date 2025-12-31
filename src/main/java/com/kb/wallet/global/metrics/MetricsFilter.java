package com.kb.wallet.global.metrics;

import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import java.io.IOException;
import javax.servlet.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Component
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

    try {
      chain.doFilter(request, response);
    } finally {
      sample.stop(httpRequestTimer.getTimer());
    }
  }
}
