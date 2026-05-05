package com.apitest.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
    
    private JsonUtils() {
    }
    
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("对象序列化为JSON失败: {}", e.getMessage());
            throw new RuntimeException("JSON序列化失败", e);
        }
    }
    
    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("对象序列化为格式化JSON失败: {}", e.getMessage());
            throw new RuntimeException("JSON序列化失败", e);
        }
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.error("JSON反序列化失败: {}", e.getMessage());
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
    
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            logger.error("JSON反序列化失败: {}", e.getMessage());
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
    
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            logger.error("JSON反序列化为List失败: {}", e.getMessage());
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
    
    public static Map<String, Object> fromJsonToMap(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            logger.error("JSON反序列化为Map失败: {}", e.getMessage());
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
    
    public static boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
    
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
