package com.example.billingcollections.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Adds a request ID to MDC and response headers for traceable logging.
 */
@Slf4j
@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String HEADER_NAME = "X-Request-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

          String requestId = Optional.ofNullable(request.getHeader(HEADER_NAME))
                 .filter(header -> !header.isBlank())
                .orElse(UUID.randomUUID().toString());

        MDC.put(REQUEST_ID, requestId); //MDC allows logs to put automatically put value
        response.setHeader(HEADER_NAME, requestId);

        log.info("Incoming request: method={}, uri={}", request.getMethod(), request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear(); 
        }
    }
}