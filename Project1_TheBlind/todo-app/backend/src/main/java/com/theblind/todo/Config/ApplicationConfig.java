package com.theblind.todo.Config;

import com.theblind.todo.Repo.AccountRepo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/* 
* This class is used to configurate the application's security features, including:
*       - the password encoder, used to encode passwords before registering a user and decode passwords for user logins
*       - the authentication manager, the core api for filtering authentication requests
*       - the authentication provider, used to actually execute authentication of users (generating and receiving tokens)
* 
* It overrides basic authentication that comes with HTTP security by default
* in order to perform authentication through JSON Web Tokens.
*/
@Configuration
public class ApplicationConfig {
    private final AccountRepo accountRepository;

    public ApplicationConfig(AccountRepo accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}