package com.kb.wallet.global.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import java.io.IOException;
import java.time.Duration;
import javax.servlet.*;
import javax.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HttpMetricsFilter implements Filter {
  private final PrometheusMeterRegistry registry;

  public HttpMetricsFilter(PrometheusMeterRegistry registry) {
    log.error("HttpMetricsFilter registry identity={}",
        System.identityHashCode(registry));
    this.registry = registry;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    log.error("🔥 HttpMetricsFilter DO_FILTER CALLED thread={}",
        Thread.currentThread().getName());
    if (!(request instanceof HttpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }
    log.info("HttpFilter2");

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String path = normalizeUri(httpRequest.getRequestURI());
    String method = httpRequest.getMethod();
    String status = String.valueOf(httpResponse.getStatus());

    Timer.Sample sample = Timer.start(registry);

    try {
      chain.doFilter(request, response);
    } finally {
      Timer timer = Timer.builder("http.server.requests")
          .description("HTTP Server Requests")
          .tags("uri", path, "method", method, "status", status)
          .publishPercentileHistogram()
          .serviceLevelObjectives(
              Duration.ofMillis(100),
              Duration.ofMillis(300),
              Duration.ofMillis(500),
              Duration.ofSeconds(1),
              Duration.ofSeconds(3)
          )
          .register(registry);

      sample.stop(timer);
    }
  }

  private String normalizeUri(String uri) {
    return uri.replaceAll("/\\d+", "/{id}");
  }
}
