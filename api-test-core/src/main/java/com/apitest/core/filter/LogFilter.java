package com.apitest.core.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

public class LogFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);
    
    private final boolean logRequest;
    private final boolean logResponse;
    
    public LogFilter() {
        this(true, true);
    }
    
    public LogFilter(boolean logRequest, boolean logResponse) {
        this.logRequest = logRequest;
        this.logResponse = logResponse;
    }
    
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                          FilterableResponseSpecification responseSpec, 
                          FilterContext ctx) {
        
        if (logRequest) {
            logRequest(requestSpec);
        }
        
        long startTime = System.currentTimeMillis();
        Response response = ctx.next(requestSpec, responseSpec);
        long duration = System.currentTimeMillis() - startTime;
        
        if (logResponse) {
            logResponse(response, duration);
        }
        
        return response;
    }
    //打印请求信息
    private void logRequest(FilterableRequestSpecification requestSpec) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("========== HTTP Request ==========\n");
        sb.append(String.format("%s %s\n", requestSpec.getMethod(), requestSpec.getURI()));
        
        String headers = requestSpec.getHeaders().asList().stream()
                .map(h -> String.format("  %s: %s", h.getName(), h.getValue()))
                .collect(Collectors.joining("\n"));
        if (!headers.isEmpty()) {
            sb.append("Headers:\n").append(headers).append("\n");
        }
        
        if (requestSpec.getQueryParams() != null && !requestSpec.getQueryParams().isEmpty()) {
            String queryParams = ((Map<?, ?>) requestSpec.getQueryParams()).entrySet().stream()
                    .map(e -> String.format("  %s=%s", e.getKey(), e.getValue()))
                    .collect(Collectors.joining("\n"));
            sb.append("Query Params:\n").append(queryParams).append("\n");
        }
        
        if (requestSpec.getBody() != null) {
            sb.append("Body:\n").append(requestSpec.getBody().toString()).append("\n");
        }
        
        sb.append("=================================");
        logger.info(sb.toString());
    }
    
    private void logResponse(Response response, long duration) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("========== HTTP Response ==========\n");
        sb.append(String.format("Status: %d %s\n", response.getStatusCode(), response.getStatusLine()));
        sb.append(String.format("Duration: %d ms\n", duration));
        
        String headers = response.getHeaders().asList().stream()
                .map(h -> String.format("  %s: %s", h.getName(), h.getValue()))
                .collect(Collectors.joining("\n"));
        if (!headers.isEmpty()) {
            sb.append("Headers:\n").append(headers).append("\n");
        }
        
        String body = response.getBody().asString();
        if (body != null && !body.isEmpty()) {
            sb.append("Body:\n").append(body).append("\n");
        }
        
        sb.append("==================================");
        
        if (response.getStatusCode() >= 400) {
            logger.error(sb.toString());
        } else {
            logger.info(sb.toString());
        }
    }
}
