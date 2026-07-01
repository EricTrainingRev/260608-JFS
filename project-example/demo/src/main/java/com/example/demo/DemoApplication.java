package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication is a convenience annotation that combines @Configuration,
// @EnableAutoConfiguration, and @ComponentScan — it marks this class as the entry point
// for Spring Boot's auto-configuration and component scanning.
@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}
