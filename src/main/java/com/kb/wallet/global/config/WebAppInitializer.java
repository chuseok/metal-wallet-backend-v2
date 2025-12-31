package com.kb.wallet.global.config;


import com.kb.wallet.global.metrics.MetricsFilter;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  public WebAppInitializer() {
  }

  //인코딩 필터 설정
  @Override
  protected Filter[] getServletFilters() {
    CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
    encodingFilter.setEncoding("UTF-8");
    MetricsFilter metricsFilter = new MetricsFilter();

    return new Filter[]{encodingFilter, metricsFilter};
  }

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[]{SecurityConfig.class, AppConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[]{WebConfig.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    servletContext.setInitParameter("contextInitializerClasses",
        "com.kb.wallet.global.config.ProfileInitializer");
    super.onStartup(servletContext);
  }
}