package com.project.test.ordermanagement.config;

import com.project.test.ordermanagement.service.common.RequestTraceService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private RequestTraceService requestTraceService;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryPointcut() {
    }

    @Around("(controllerPointcut() || servicePointcut() || repositoryPointcut())")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String traceId = requestTraceService.getTraceId();

        logger.info("[TraceID: {}] Starting exection: {}.{}() with args: {}",
                traceId, className, methodName, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("[TraceID: {}] Finished execution: {}.{}() em {}ms",
                    traceId, className, methodName, executionTime);

            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("[TraceID: {}] Error during execution: {}.{}() after {}ms. Error: {}",
                    traceId, className, methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }

}
