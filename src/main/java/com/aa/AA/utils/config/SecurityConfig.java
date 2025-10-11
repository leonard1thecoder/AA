package com.aa.AA.utils.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Filter jwtAuthFilterConfig;
    private final AuthenticationProvider authencticationProvider;

    public SecurityConfig(Filter jwtAuthFilterConfig, AuthenticationProvider authencticationProvider) {
        this.jwtAuthFilterConfig = jwtAuthFilterConfig;
        this.authencticationProvider = authencticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf
                        -> csrf.disable())
                .authorizeHttpRequests(auth
                        -> auth.requestMatchers("/dev/api/auth/**").permitAll().anyRequest().authenticated())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authencticationProvider)
                .addFilterBefore(jwtAuthFilterConfig, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
