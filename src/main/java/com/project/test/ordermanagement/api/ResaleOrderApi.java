package com.project.test.ordermanagement.api;

import com.project.test.ordermanagement.constants.CircuitBreakerConstants;
import com.project.test.ordermanagement.model.dto.ResaleOrderDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
public class ResaleOrderApi {

    /**
     * Simulates an unstable API that randomly fails
     */
    @CircuitBreaker(name = CircuitBreakerConstants.BACKEND_SERVICE, fallbackMethod = "fallbackMethod")
    @Retry(name = CircuitBreakerConstants.BACKEND_SERVICE)
    public ResaleOrderDTO sendResaleOrders() {
        Random random = new Random();

        // Simulate latency
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 70% chance of failure
        if (random.nextInt(100) < 70) {
            throw new RuntimeException("API is currently unavailable");
        }

        return ResaleOrderDTO.builder().orderNumber(UUID.randomUUID()).build();
    }

    // Fallback method could use some cache tool (e.g. Redis) to return an object
    public ResaleOrderDTO fallbackMethod(Throwable e) {
        log.error("Fallback method executed. Error: {}", e.getMessage());
        return ResaleOrderDTO.builder().build();
    }

}