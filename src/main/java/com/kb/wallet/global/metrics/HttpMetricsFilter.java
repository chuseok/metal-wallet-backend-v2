package com.kb.wallet.global.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.*;
import javax.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class HttpMetricsFilter implements Filter {

  private MeterRegistry registry;

  public HttpMetricsFilter(MeterRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    ServletContext sc = filterConfig.getServletContext();
    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
    this.registry = ctx.getBean(MeterRegistry.class);
  }

  @Override
  public  void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    long start = System.nanoTime();

    try {
      chain.doFilter(request, response);
    } finally {
      long durationNs = System.nanoTime() - start;

      Timer.builder("http_server_requests_seconds")
          .description("HTTP Server Requests")
          .tags(
              "method", httpRequest.getMethod(),
              "uri", httpRequest.getRequestURI(),
              "status", String.valueOf(httpResponse.getStatus())
          )
          // 🔥 이게 중요
          .publishPercentileHistogram(true)
          .register(registry)
          .record(durationNs, TimeUnit.NANOSECONDS);
    }
  }

  private String normalizeUri(String uri) {
    return uri.replaceAll("/\\d+", "/{id}");
  }
}
