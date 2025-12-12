package com.kb.wallet.global.config;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
  @Bean
  public PrometheusMeterRegistry prometheusMeterRegistry() {
    PrometheusMeterRegistry registry = new PrometheusMeterRegistry(io.micrometer.prometheus.PrometheusConfig.DEFAULT);
    registry.config().commonTags(
        "application", "ticket-app"
    );
//    new ClassLoaderMetrics().bindTo(registry);
    new JvmMemoryMetrics().bindTo(registry);
    new JvmGcMetrics().bindTo(registry);
//    new ProcessorMetrics().bindTo(registry);
//    new UptimeMetrics().bindTo(registry);
    new JvmThreadMetrics().bindTo(registry);

    return registry;
  }
}
