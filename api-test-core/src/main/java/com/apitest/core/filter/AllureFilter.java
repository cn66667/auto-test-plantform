package com.apitest.core.filter;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AllureFilter implements Filter {
    
    private static final String REQUEST_ATTACHMENT_NAME = "Request";
    private static final String RESPONSE_ATTACHMENT_NAME = "Response";
    
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                          FilterableResponseSpecification responseSpec, 
                          FilterContext ctx) {
        
        attachRequest(requestSpec);
        
        Response response = ctx.next(requestSpec, responseSpec);
        
        attachResponse(response);
        
        return response;
    }
    
    private void attachRequest(FilterableRequestSpecification requestSpec) {
        StringBuilder sb = new StringBuilder();
        sb.append("Method: ").append(requestSpec.getMethod()).append("\n");
        sb.append("URI: ").append(requestSpec.getURI()).append("\n");
        
        String headers = requestSpec.getHeaders().asList().stream()
                .map(h -> h.getName() + ": " + h.getValue())
                .collect(Collectors.joining("\n"));
        if (!headers.isEmpty()) {
            sb.append("Headers:\n").append(headers).append("\n");
        }
        
        if (requestSpec.getBody() != null) {
            sb.append("Body:\n").append(requestSpec.getBody().toString()).append("\n");
        }
        
        Allure.addAttachment(REQUEST_ATTACHMENT_NAME, "text/plain", sb.toString());
    }
    
    private void attachResponse(Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ").append(response.getStatusCode()).append(" ").append(response.getStatusLine()).append("\n");
        
        String headers = response.getHeaders().asList().stream()
                .map(h -> h.getName() + ": " + h.getValue())
                .collect(Collectors.joining("\n"));
        if (!headers.isEmpty()) {
            sb.append("Headers:\n").append(headers).append("\n");
        }
        
        String body = response.getBody().asString();
        if (body != null && !body.isEmpty()) {
            sb.append("Body:\n").append(body).append("\n");
        }
        
        Allure.addAttachment(RESPONSE_ATTACHMENT_NAME, "text/plain", sb.toString());
    }
}
