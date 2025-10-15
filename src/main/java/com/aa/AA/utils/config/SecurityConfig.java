package com.aa.AA.utils.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
        http.cors(Customizer.withDefaults())
                // CSRF protection enabled by default
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth
                        -> auth.requestMatchers("/dev/api/auth/**").permitAll().anyRequest().authenticated())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authencticationProvider)
                .addFilterBefore(jwtAuthFilterConfig, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:5517"));
        corsConfig.setAllowedHeaders(List.of("Authorized"));
        corsConfig.setAllowedMethods(List.of("GET","POST"));
        UrlBasedCorsConfigurationSource baseSource = new UrlBasedCorsConfigurationSource();
        baseSource.registerCorsConfiguration("/**",corsConfig);
        return baseSource;
    }
}
