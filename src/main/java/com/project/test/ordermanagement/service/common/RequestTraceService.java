package com.project.test.ordermanagement.service.common;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class RequestTraceService {

    private static final String MDC_TRACE_ID_KEY = "traceId";
    private static final ThreadLocal<String> traceIdHolder = new ThreadLocal<>();

    public void setTraceId(String traceId) {
        if (traceId != null && !traceId.isEmpty()) {
            traceIdHolder.set(traceId);
            MDC.put(MDC_TRACE_ID_KEY, traceId);
        }
    }

    public String getTraceId() {
        return traceIdHolder.get();
    }

    public void clearTraceId() {
        traceIdHolder.remove();
        MDC.remove(MDC_TRACE_ID_KEY);
    }
}
