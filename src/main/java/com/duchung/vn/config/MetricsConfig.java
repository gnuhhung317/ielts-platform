//package com.duchung.vn.config;
//
//import io.micrometer.core.aop.TimedAspect;
//import io.micrometer.core.instrument.MeterRegistry;
//import io.micrometer.core.instrument.config.MeterFilter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MetricsConfig {
//
//    @Value("${spring.application.name}")
//    private String applicationName;
//
//    @Bean
//    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
//        return registry -> registry
//                .config()
//                .meterFilter(MeterFilter.deny(id -> {
//                    String uri = id.getTag("uri");
//                    return uri != null && (uri.contains("/actuator") || uri.contains("/health"));
//                }))
//                .commonTags("application", applicationName);
//    }
//
//    @Bean
//    public TimedAspect timedAspect(MeterRegistry registry) {
//        return new TimedAspect(registry);
//    }
//}