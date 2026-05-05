package com.apitest.common.exception;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private int statusCode;
    private String responseBody;
    
    public ApiException(String message) {
        super(message);
    }
    
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public ApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public ApiException(String message, int statusCode, String responseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
}
