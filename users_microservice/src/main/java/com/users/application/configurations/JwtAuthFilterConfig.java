package com.users.application.configurations;


import com.users.application.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.utils.application.ExceptionHandlerReporter.formatDateTime;

@Component
public class JwtAuthFilterConfig extends OncePerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(JwtAuthFilterConfig.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilterConfig(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        logger.info("jwt service : {}, userDetails : {}", jwtService, userDetailsService);

    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final var authHeader = request.getHeader("authorization");
        logger.info("token from header : {}", authHeader);
        final String jwt;
        final String userEmailAddress; // todo
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            userEmailAddress = jwtService.extractUsername(jwt);
            logger.info("jwt config  : {}", jwt);
        } catch (ExpiredJwtException e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            String msg = "[{" + '"' + "error " + '"' + ":" + '"' + "Expired Token" + '"' +
                    ", " + '"' + "message " + '"' + ":" + '"' + "log in again " + '"' +
                    "," + '"' + "timestamp" + '"' + ":" + '"' + formatDateTime(LocalDateTime.now()) + '"' + "}]";
            logger.warn("User tried using expired jwt : {} ", msg);
            response.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
            return;
        } catch (SignatureException e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            String msg = "[{" + '"' + "error " + '"' + ":" + '"' + "Token invalid" + '"' +
                    ", " + '"' + "message " + '"' + ":" + '"' + "Contact AA Administrator " + '"' +
                    "," + '"' + "timestamp" + '"' + ":" + '"' + formatDateTime(LocalDateTime.now()) + '"' + "}]";
            logger.warn("User tried using invalid jwt : {} ", msg);
            response.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
            return;
        }
        if (userEmailAddress != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("email config  : {}", userEmailAddress);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmailAddress);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        filterChain.doFilter(request, response);

    }
}
