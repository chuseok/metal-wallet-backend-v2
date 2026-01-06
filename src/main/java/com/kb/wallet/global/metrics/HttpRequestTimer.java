//package com.kb.wallet.global.metrics;
//
//import io.micrometer.core.instrument.Timer;
//import io.micrometer.prometheus.PrometheusMeterRegistry;
//import java.time.Duration;
//import org.springframework.stereotype.Component;
//
//@Component
//public class HttpRequestTimer {
//
//  private final Timer timer;
//
//  public HttpRequestTimer(PrometheusMeterRegistry registry) {
//    this.timer = Timer.builder("http.server.requests")
//        .description("HTTP server request latency")
//        .publishPercentileHistogram(true)
//        .serviceLevelObjectives(
//            Duration.ofMillis(100),
//            Duration.ofMillis(300),
//            Duration.ofMillis(500),
//            Duration.ofSeconds(1),
//            Duration.ofSeconds(2),
//            Duration.ofSeconds(5)
//        )
//        .register(registry);
//  }
//
//  public Timer getTimer() {
//    return timer;
//  }
//}
