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

  private Timer timer;

  @Override
  public void init(FilterConfig filterConfig) {
    ServletContext context = filterConfig.getServletContext();
    WebApplicationContext springContext =
        WebApplicationContextUtils.getRequiredWebApplicationContext(context);

    HttpRequestTimer httpRequestTimer =
        springContext.getBean(HttpRequestTimer.class);

    this.timer = httpRequestTimer.getTimer();
  }
  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {

    Timer.Sample sample = Timer.start();

    try {
      chain.doFilter(request, response);
    } finally {
      sample.stop(timer);
    }
  }
}
