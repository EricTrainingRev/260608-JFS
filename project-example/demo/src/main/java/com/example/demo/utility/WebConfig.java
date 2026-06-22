package com.example.demo.utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global web configuration for the application.
 * Implements WebMvcConfigurer to customize Spring MVC behavior.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures Cross-Origin Resource Sharing (CORS) rules for the application.
     * CORS is a browser security feature that blocks requests from one origin
     * (e.g. http://localhost:4200) to a different origin (e.g. http://localhost:8080)
     * unless the server explicitly allows it.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS rules to all endpoints in the API
                .allowedOrigins("http://localhost:4200") // Only allow requests from our frontend
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // HTTP methods the frontend can use
                .allowedHeaders("*") // Allow any headers (e.g. Authorization, Content-Type)
                .allowCredentials(true); // Allow cookies/auth headers to be sent with requests
    }
}
