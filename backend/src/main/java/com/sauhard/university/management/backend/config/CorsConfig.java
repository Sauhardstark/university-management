package com.sauhard.university.management.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry reg) {
    reg.addMapping("/api/**")
       .allowedOrigins("http://localhost:8081")
       .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
       .allowedHeaders("*")
       .exposedHeaders("Location")
       .allowCredentials(true)
       .maxAge(3600);
  }
}