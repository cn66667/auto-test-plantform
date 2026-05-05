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
public class EnvironmentConfig {
    private String name;
    private String baseUrl;
    private Integer timeout;
    private Integer connectTimeout;
    private Integer readTimeout;
    private Map<String, String> defaultHeaders;
    private Map<String, String> variables;
    
    public String getVariable(String key) {
        if (variables == null) {
            return null;
        }
        return variables.get(key);
    }
    
    public void setVariable(String key, String value) {
        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put(key, value);
    }
    
    public String getDefaultHeader(String key) {
        if (defaultHeaders == null) {
            return null;
        }
        return defaultHeaders.get(key);
    }
    
    public void addDefaultHeader(String key, String value) {
        if (defaultHeaders == null) {
            defaultHeaders = new HashMap<>();
        }
        defaultHeaders.put(key, value);
    }
}
