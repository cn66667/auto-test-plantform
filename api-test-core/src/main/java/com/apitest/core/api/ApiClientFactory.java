package com.apitest.core.api;

import com.apitest.common.config.ConfigManager;
import com.apitest.common.model.ApiRequest;
import com.apitest.common.model.ApiResponse;

public final class ApiClientFactory {
    
    private static final ThreadLocal<ApiClient> CLIENT_CACHE = new ThreadLocal<>();
    
    private ApiClientFactory() {
    }
    
    public static ApiClient getClient() {
        ApiClient client = CLIENT_CACHE.get();
        if (client == null) {
            client = new ApiClient();
            CLIENT_CACHE.set(client);
        }
        return client;
    }
    
    public static ApiClient getClient(String baseUrl) {
        return new ApiClient(baseUrl);
    }
    
    public static ApiClient getClient(String baseUrl, int timeout) {
        return new ApiClient(baseUrl, timeout);
    }
    
    public static void reset() {
        CLIENT_CACHE.remove();
    }
    
    public static ApiResponse execute(ApiRequest request) {
        return getClient().execute(request);
    }
    
    public static ApiResponse get(String path) {
        return getClient().get(path);
    }
    
    public static ApiResponse post(String path, Object body) {
        return getClient().post(path, body);
    }
    
    public static ApiResponse put(String path, Object body) {
        return getClient().put(path, body);
    }
    
    public static ApiResponse delete(String path) {
        return getClient().delete(path);
    }
    
    public static ApiResponse patch(String path, Object body) {
        return getClient().patch(path, body);
    }
}
