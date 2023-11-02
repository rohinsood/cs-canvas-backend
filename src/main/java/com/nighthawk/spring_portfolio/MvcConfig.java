package com.nighthawk.spring_portfolio;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("https://cs-canvas.stu.nighthawkcodingsociety.com",
        "http://localhost:4200").allowedOrigins("*") // Allow requests from any origin
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*");
  }

}