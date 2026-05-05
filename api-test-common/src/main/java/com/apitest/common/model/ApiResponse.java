package com.apitest.common.model;

import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private int statusCode;
    private String statusLine;
    private String body;
    private Map<String, String> headers;
    private String contentType;
    private long responseTime;
    private Response rawResponse;
    
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
    
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }
    
    public boolean isServerError() {
        return statusCode >= 500 && statusCode < 600;
    }
    
    public String getHeader(String name) {
        if (headers == null) {
            return null;
        }
        return headers.get(name);
    }
    
    public <T> T getBodyAs(Class<T> clazz) {
        if (rawResponse != null) {
            return rawResponse.as(clazz);
        }
        return null;
    }
    
    public String jsonPathGetString(String path) {
        if (rawResponse != null) {
            return rawResponse.jsonPath().getString(path);
        }
        return null;
    }
    
    public <T> T jsonPathGet(String path, Class<T> type) {
        if (rawResponse != null) {
            return rawResponse.jsonPath().getObject(path, type);
        }
        return null;
    }
}
