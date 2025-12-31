package com.kb.wallet.global.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.io.IOException;
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

    String path = normalizeUri(httpRequest.getRequestURI());
    String method = httpRequest.getMethod();

    Timer.Sample sample = Timer.start(registry);

    try {
      chain.doFilter(request, response);
    } finally {
      Timer timer = Timer.builder("http.server.requests")//http_server_requests_count
          .description("HTTP Server Requests")
          .tags("uri", path, "method", method)
          .publishPercentileHistogram(true)
          .register(registry);

      sample.stop(timer);
    }
  }

  private String normalizeUri(String uri) {
    return uri.replaceAll("/\\d+", "/{id}");
  }
}
