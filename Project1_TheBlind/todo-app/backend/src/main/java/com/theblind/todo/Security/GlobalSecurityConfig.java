package com.theblind.todo.Security;


import com.theblind.todo.Repo.AccountRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.cors.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
@EnableWebSecurity
public class GlobalSecurityConfig {
    @Autowired
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public GlobalSecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter,  AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Disabling CSRF in order to permit API requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }
}