package com.apitest.core.api;

import com.apitest.common.config.ConfigManager;
import com.apitest.common.constants.CommonConstants;
import com.apitest.common.enums.ContentType;
import com.apitest.common.enums.HttpMethod;
import com.apitest.common.exception.ApiException;
import com.apitest.common.model.ApiRequest;
import com.apitest.common.model.ApiResponse;
import com.apitest.core.filter.AllureFilter;
import com.apitest.core.filter.LogFilter;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    
    private String baseUrl;
    private int timeout;
    private int connectTimeout;
    private boolean logRequest = true;
    private boolean logResponse = true;
    
    public ApiClient() {
        this.baseUrl = ConfigManager.getInstance().getBaseUrl();
        this.timeout = ConfigManager.getInstance().getEnvironmentConfig().getTimeout();
        this.connectTimeout = ConfigManager.getInstance().getEnvironmentConfig().getConnectTimeout();
    }
    
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.timeout = CommonConstants.DEFAULT_TIMEOUT;
        this.connectTimeout = CommonConstants.DEFAULT_CONNECT_TIMEOUT;
    }
    
    public ApiClient(String baseUrl, int timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.connectTimeout = CommonConstants.DEFAULT_CONNECT_TIMEOUT;
    }
    //Allure核心注解，用于记录测试方式和URL
    @Step("发送HTTP请求: {request.method} {request.path}")
    public ApiResponse execute(ApiRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            RequestSpecification spec = buildRequestSpecification(request);
            Response response = executeRequest(spec, request);
            
            long duration = System.currentTimeMillis() - startTime;
            return buildApiResponse(response, duration);
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("API请求执行失败: {} - {}", request.getPath(), e.getMessage());
            throw new ApiException("API请求执行失败: " + e.getMessage(), e);
        }
    }
    
    private RequestSpecification buildRequestSpecification(ApiRequest request) {
        RequestSpecification spec = RestAssured.given()
                .config(buildRestAssuredConfig())
                .filters(new AllureFilter(), new LogFilter());
        
        String fullUrl = buildFullUrl(request);
        spec.baseUri(fullUrl);
        //设置请求头
        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
            spec.headers(request.getHeaders());
        }
        //自动添加密钥

        String apiKey = ConfigManager.getInstance().getProperty("env.test.apiKey");
        if (apiKey != null && !apiKey.isEmpty() && !"YOUR_API_KEY_HERE".equals(apiKey)) {
            spec.header("x-api-key", apiKey);
        }
        //设置查询参数
        if (request.getQueryParams() != null && !request.getQueryParams().isEmpty()) {
            spec.queryParams(request.getQueryParams());
        }
        //设置路径参数
        if (request.getPathParams() != null && !request.getPathParams().isEmpty()) {
            spec.pathParams(request.getPathParams());
        }
        //设置请求体内容类型
        String contentType = request.getContentType();
        if (contentType != null) {
            spec.contentType(contentType);
        } else {
            spec.contentType(CommonConstants.CONTENT_TYPE_JSON);
        }
        //设置请求体内容
        if (request.getBody() != null) {
                spec.body(request.getBody());
        }
        
        return spec;
    }
    //获取完整的URL
    private String buildFullUrl(ApiRequest request) {
        if (request.getUrl() != null && !request.getUrl().isEmpty()) {
            return request.getUrl();
        }
        
        String path = request.getPath();
        if (path == null || path.isEmpty()) {
            return baseUrl;
        }
        
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        
        if (baseUrl.endsWith("/") && path.startsWith("/")) {
            return baseUrl + path.substring(1);
        } else if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
            return baseUrl + "/" + path;
        } else {
            return baseUrl + path;
        }
    }
    
    private RestAssuredConfig buildRestAssuredConfig() {
        return RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", connectTimeout)
                        .setParam("http.socket.timeout", timeout));
    }
    
    private Response executeRequest(RequestSpecification spec, ApiRequest request) {
        String method = request.getMethod();
        if (method == null || method.isEmpty()) {
            method = HttpMethod.GET.name();
        }
        
        switch (method.toUpperCase()) {
            case "GET":
                return spec.get();
            case "POST":
                return spec.post();
            case "PUT":
                return spec.put();
            case "DELETE":
                return spec.delete();
            case "PATCH":
                return spec.patch();
            case "HEAD":
                return spec.head();
            case "OPTIONS":
                return spec.options();
            default:
                throw new ApiException("不支持的HTTP方法: " + method);
        }
    }
    
    private ApiResponse buildApiResponse(Response response, long duration) {
        Map<String, String> headers = response.getHeaders().asList().stream()
                .collect(java.util.stream.Collectors.toMap(
                        h -> h.getName(),
                        h -> h.getValue(),
                        (v1, v2) -> v1
                ));
        
        return ApiResponse.builder()
                .statusCode(response.getStatusCode())
                .statusLine(response.getStatusLine())
                .body(response.getBody().asString())
                .headers(headers)
                .contentType(response.getContentType())
                .responseTime(duration)
                .rawResponse(response)
                .build();
    }
    
    public ApiResponse get(String path) {
        return execute(ApiRequest.builder()
                .path(path)
                .method(HttpMethod.GET.name())
                .build());
    }
    
    public ApiResponse get(String path, Map<String, String> queryParams) {
        return execute(ApiRequest.builder()
                .path(path)
                .method(HttpMethod.GET.name())
                .queryParams(queryParams)
                .build());
    }
    
    public ApiResponse post(String path, Object body) {
        return execute(ApiRequest.builder()
                .path(path)
                .method(HttpMethod.POST.name())
                .body(body)
                .build());
    }
    
    public ApiResponse put(String path, Object body) {
        return execute(ApiRequest.builder()
                .path(path)
                .method(HttpMethod.PUT.name())
                .body(body)
                .build());
    }
    
    public ApiResponse delete(String path) {
        return execute(ApiRequest.builder()
                .path(path)
                .method(HttpMethod.DELETE.name())
                .build());
    }
    
    public ApiResponse delete(String path, Object body) {
        return execute(ApiRequest.builder()
                .path(path)
                .method(HttpMethod.DELETE.name())
                .body(body)
                .build());
    }
    
    public ApiResponse patch(String path, Object body) {
        return execute(ApiRequest.builder()
                .path(path)
                .method(HttpMethod.PATCH.name())
                .body(body)
                .build());
    }
    
    public ApiClient setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    
    public ApiClient setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
    
    public ApiClient setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
    
    public ApiClient setLogRequest(boolean logRequest) {
        this.logRequest = logRequest;
        return this;
    }
    
    public ApiClient setLogResponse(boolean logResponse) {
        this.logResponse = logResponse;
        return this;
    }
}