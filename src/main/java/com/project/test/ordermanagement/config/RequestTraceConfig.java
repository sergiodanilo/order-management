package com.project.test.ordermanagement.config;

import com.project.test.ordermanagement.service.common.RequestTraceService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Configuration
public class RequestTraceConfig {

    @Autowired
    private RequestTraceService requestTraceService;

    @Bean
    public Filter traceIdFilter() {
        return new OncePerRequestFilter() {
            private static final String TRACE_ID_HEADER_NAME = "requestTraceId";

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                try {
                    String traceId = request.getHeader(TRACE_ID_HEADER_NAME);
                    if (traceId == null || traceId.isEmpty()) {
                        traceId = UUID.randomUUID().toString();
                    }

                    requestTraceService.setTraceId(traceId);
                    response.addHeader(TRACE_ID_HEADER_NAME, traceId);

                    filterChain.doFilter(request, response);
                } finally {
                    requestTraceService.clearTraceId();
                }
            }
        };
    }
}
