package com.apitest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest {
    private String url;
    private String path;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Map<String, String> pathParams;
    private Object body;
    private String contentType;
    private Integer timeout;
    private Boolean logRequest;
    private Boolean logResponse;
    
    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }
    
    public void addQueryParam(String key, String value) {
        if (queryParams == null) {
            queryParams = new HashMap<>();
        }
        queryParams.put(key, value);
    }
    
    public void addPathParam(String key, String value) {
        if (pathParams == null) {
            pathParams = new HashMap<>();
        }
        pathParams.put(key, value);
    }
}
