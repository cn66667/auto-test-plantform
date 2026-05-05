package com.apitest.core.assertion;

import com.apitest.common.exception.ApiException;
import com.apitest.common.model.ApiResponse;
import io.qameta.allure.Step;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public final class ApiAssert {
    
    private ApiAssert() {
    }
    
    @Step("断言状态码: 期望 {expected}, 实际 {response.statusCode}")
    public static void assertStatusCode(ApiResponse response, int expected) {
        Assert.assertEquals(response.getStatusCode(), expected, 
                String.format("状态码不匹配, 期望: %d, 实际: %d", expected, response.getStatusCode()));
    }
    
    @Step("断言状态码在范围: {min} - {max}")
    public static void assertStatusCodeInRange(ApiResponse response, int min, int max) {
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode >= min && statusCode <= max,
                String.format("状态码不在范围内, 期望: %d-%d, 实际: %d", min, max, statusCode));
    }
    
    @Step("断言请求成功")
    public static void assertSuccess(ApiResponse response) {
        Assert.assertTrue(response.isSuccess(), 
                String.format("请求失败, 状态码: %d", response.getStatusCode()));
    }
    
    @Step("断言请求失败")
    public static void assertFailure(ApiResponse response) {
        Assert.assertFalse(response.isSuccess(), 
                String.format("请求成功, 状态码: %d", response.getStatusCode()));
    }
    
    @Step("断言响应包含Header: {headerName}")
    public static void assertHasHeader(ApiResponse response, String headerName) {
        Assert.assertNotNull(response.getHeader(headerName), 
                String.format("响应不包含Header: %s", headerName));
    }
    
    @Step("断言Header值: {headerName} = {expectedValue}")
    public static void assertHeaderEquals(ApiResponse response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        Assert.assertEquals(actualValue, expectedValue,
                String.format("Header值不匹配, %s: 期望=%s, 实际=%s", headerName, expectedValue, actualValue));
    }
    
    @Step("断言响应时间小于: {maxMs}ms")
    public static void assertResponseTimeLessThan(ApiResponse response, long maxMs) {
        Assert.assertTrue(response.getResponseTime() < maxMs,
                String.format("响应时间超过阈值, 期望<%dms, 实际=%dms", maxMs, response.getResponseTime()));
    }
    
    @Step("断言响应体不为空")
    public static void assertBodyNotEmpty(ApiResponse response) {
        Assert.assertNotNull(response.getBody(), "响应体为空");
        Assert.assertFalse(response.getBody().isEmpty(), "响应体为空字符串");
    }
    
    @Step("断言响应体包含: {expectedText}")
    public static void assertBodyContains(ApiResponse response, String expectedText) {
        Assert.assertNotNull(response.getBody(), "响应体为空");
        Assert.assertTrue(response.getBody().contains(expectedText),
                String.format("响应体不包含文本: %s", expectedText));
    }
    
    @Step("断言JSON路径值: {jsonPath} = {expected}")
    public static void assertJsonPathEquals(ApiResponse response, String jsonPath, Object expected) {
        String actualStr = response.jsonPathGetString(jsonPath);
        String expectedStr = expected != null ? String.valueOf(expected) : null;
        Assert.assertEquals(actualStr, expectedStr,
                String.format("JSON路径值不匹配, %s: 期望=%s, 实际=%s", jsonPath, expectedStr, actualStr));
    }
    
    @Step("断言JSON路径字符串值: {jsonPath} = {expected}")
    public static void assertJsonPathStringEquals(ApiResponse response, String jsonPath, String expected) {
        String actual = response.jsonPathGetString(jsonPath);
        Assert.assertEquals(actual, expected,
                String.format("JSON路径字符串值不匹配, %s: 期望=%s, 实际=%s", jsonPath, expected, actual));
    }
    
    @Step("断言JSON路径存在: {jsonPath}")
    public static void assertJsonPathExists(ApiResponse response, String jsonPath) {
        String value = response.jsonPathGetString(jsonPath);
        Assert.assertNotNull(value, String.format("JSON路径不存在: %s", jsonPath));
    }
    
    @Step("断言JSON路径不存在: {jsonPath}")
    public static void assertJsonPathNotExists(ApiResponse response, String jsonPath) {
        String value = response.jsonPathGetString(jsonPath);
        Assert.assertNull(value, String.format("JSON路径存在: %s", jsonPath));
    }
    
    @Step("断言JSON路径值不为空: {jsonPath}")
    public static void assertJsonPathNotEmpty(ApiResponse response, String jsonPath) {
        String value = response.jsonPathGetString(jsonPath);
        Assert.assertNotNull(value, String.format("JSON路径值为空: %s", jsonPath));
        Assert.assertFalse(value.isEmpty(), 
                String.format("JSON路径字符串为空: %s", jsonPath));
    }
    
    @Step("断言JSON路径匹配: {jsonPath}")
    public static void assertJsonPathMatches(ApiResponse response, String jsonPath, Matcher<String> matcher) {
        String value = response.jsonPathGetString(jsonPath);
        assertThat(String.format("JSON路径值不匹配: %s", jsonPath), value, matcher);
    }
    
    @Step("断言JSON路径是列表")
    public static void assertJsonPathIsList(ApiResponse response, String jsonPath) {
        if (response.getRawResponse() != null) {
            List<?> value = response.getRawResponse().jsonPath().getList(jsonPath);
            Assert.assertNotNull(value, String.format("JSON路径不是列表: %s", jsonPath));
        } else {
            Assert.fail("原始响应为空，无法验证列表类型");
        }
    }
    
    @Step("断言JSON路径列表大小: {jsonPath} = {expectedSize}")
    public static void assertJsonPathListSize(ApiResponse response, String jsonPath, int expectedSize) {
        if (response.getRawResponse() != null) {
            List<?> value = response.getRawResponse().jsonPath().getList(jsonPath);
            Assert.assertNotNull(value, String.format("JSON路径不是列表: %s", jsonPath));
            int actualSize = value.size();
            Assert.assertEquals(actualSize, expectedSize,
                    String.format("列表大小不匹配, %s: 期望=%d, 实际=%d", jsonPath, expectedSize, actualSize));
        } else {
            Assert.fail("原始响应为空，无法验证列表大小");
        }
    }
    
    @Step("断言JSON路径列表不为空: {jsonPath}")
    public static void assertJsonPathListNotEmpty(ApiResponse response, String jsonPath) {
        if (response.getRawResponse() != null) {
            List<?> value = response.getRawResponse().jsonPath().getList(jsonPath);
            Assert.assertNotNull(value, String.format("JSON路径不是列表: %s", jsonPath));
            Assert.assertFalse(value.isEmpty(),
                    String.format("JSON路径列表为空: %s", jsonPath));
        } else {
            Assert.fail("原始响应为空，无法验证列表");
        }
    }
    
    @Step("断言对象相等")
    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }
    
    @Step("断言条件为真: {message}")
    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }
    
    @Step("断言条件为假: {message}")
    public static void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }
    
    @Step("断言对象不为空: {message}")
    public static void assertNotNull(Object object, String message) {
        Assert.assertNotNull(object, message);
    }
    
    @Step("断言对象为空: {message}")
    public static void assertNull(Object object, String message) {
        Assert.assertNull(object, message);
    }
}
