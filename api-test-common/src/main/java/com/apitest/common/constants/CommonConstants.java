package com.apitest.common.constants;

public class CommonConstants {
    public static final String UTF_8 = "UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_COOKIE = "Cookie";
    
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String BASIC_PREFIX = "Basic ";
    
    public static final int DEFAULT_TIMEOUT = 30000;
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    public static final int DEFAULT_READ_TIMEOUT = 30000;
    
    public static final String CONFIG_FILE_NAME = "config.properties";
    public static final String ENV_CONFIG_PREFIX = "env.";
    
    private CommonConstants() {
    }
}
