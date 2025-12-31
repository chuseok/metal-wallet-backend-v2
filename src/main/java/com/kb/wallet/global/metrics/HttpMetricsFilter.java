package com.kb.wallet.global.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.io.IOException;
import java.time.Duration;
import javax.servlet.*;
import javax.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HttpMetricsFilter implements Filter {

  private MeterRegistry registry;

  public HttpMetricsFilter(MeterRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (!(request instanceof HttpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }

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
