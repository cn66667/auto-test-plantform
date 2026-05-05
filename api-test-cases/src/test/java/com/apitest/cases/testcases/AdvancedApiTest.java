package com.apitest.cases.testcases;

import com.apitest.cases.BaseTest;
import com.apitest.common.enums.HttpMethod;
import com.apitest.common.model.ApiRequest;
import com.apitest.common.model.ApiResponse;
import com.apitest.core.assertion.ApiAssert;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("API测试示例")
@Feature("高级用法")
public class AdvancedApiTest extends BaseTest {
    
    @Test(description = "使用ApiRequest对象发送请求")
    @Story("请求构建")
    @Severity(SeverityLevel.NORMAL)
    @Description("演示使用ApiRequest对象构建和发送请求")
    public void testWithApiRequest() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        
        ApiRequest request = ApiRequest.builder()
                .path("/users")
                .method(HttpMethod.GET.name())
                .queryParams(queryParams)
                .build();
        
        logStep("发送自定义ApiRequest请求");
        ApiResponse response = apiClient.execute(request);
        
        logStep("验证响应");
        ApiAssert.assertSuccess(response);
        ApiAssert.assertJsonPathEquals(response, "page", 2);
    }
    
    @Test(description = "带请求头的请求")
    @Story("请求构建")
    @Severity(SeverityLevel.NORMAL)
    @Description("演示发送带自定义请求头的请求")
    public void testWithCustomHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Custom-Header", "TestValue");
        headers.put("Accept", "application/json");
        
        ApiRequest request = ApiRequest.builder()
                .path("/users")
                .method(HttpMethod.GET.name())
                .headers(headers)
                .build();
        
        logStep("发送带自定义请求头的请求");
        ApiResponse response = apiClient.execute(request);
        
        logStep("验证响应");
        ApiAssert.assertSuccess(response);
    }
    
    @Test(description = "响应时间测试")
    @Story("性能测试")
    @Severity(SeverityLevel.NORMAL)
    @Description("验证接口响应时间")
    public void testResponseTime() {
        logStep("发送请求并验证响应时间");
        ApiResponse response = apiClient.get("/users");
        
        ApiAssert.assertSuccess(response);
        ApiAssert.assertResponseTimeLessThan(response, 5000);
        
        logStep("响应时间: " + response.getResponseTime() + "ms");
    }
    
    @Test(description = "分页查询测试")
    @Story("数据查询")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试分页查询功能")
    public void testPagination() {
        int page = 1;
        
        logStep("发送第" + page + "页请求");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));
        
        ApiResponse response = apiClient.get("/users", queryParams);
        
        ApiAssert.assertSuccess(response);
        ApiAssert.assertJsonPathEquals(response, "page", page);
        ApiAssert.assertJsonPathIsList(response, "data");
    }
}
