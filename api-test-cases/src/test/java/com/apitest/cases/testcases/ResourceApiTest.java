package com.apitest.cases.testcases;

import com.apitest.cases.BaseTest;
import com.apitest.common.model.ApiResponse;
import com.apitest.core.assertion.ApiAssert;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("资源管理")
@Feature("资源接口")
public class ResourceApiTest extends BaseTest {
    
    @Test(description = "获取资源列表")
    @Story("查询资源")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试获取资源列表接口")
    public void testGetResourceList() {
        logStep("发送获取资源列表请求");
        ApiResponse response = apiClient.get("/unknown");
        
        logStep("验证响应状态码");
        ApiAssert.assertSuccess(response);
        
        logStep("验证响应数据结构");
        ApiAssert.assertJsonPathIsList(response, "data");
        ApiAssert.assertJsonPathListNotEmpty(response, "data");
    }
    
    @Test(description = "获取单个资源")
    @Story("查询资源")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试根据ID获取单个资源信息")
    public void testGetResourceById() {
        String resourceId = "2";
        
        logStep("发送获取资源请求: resourceId=" + resourceId);
        ApiResponse response = apiClient.get("/unknown/" + resourceId);
        
        logStep("验证响应状态码");
        ApiAssert.assertSuccess(response);
        
        logStep("验证资源数据");
        ApiAssert.assertJsonPathNotEmpty(response, "data.name");
        ApiAssert.assertJsonPathNotEmpty(response, "data.year");
        ApiAssert.assertJsonPathNotEmpty(response, "data.color");
    }
    
    @Test(description = "获取不存在的资源")
    @Story("查询资源")
    @Severity(SeverityLevel.MINOR)
    @Description("测试获取不存在的资源时的错误处理")
    public void testGetResourceNotFound() {
        String resourceId = "9999";
        
        logStep("发送获取资源请求: resourceId=" + resourceId);
        ApiResponse response = apiClient.get("/unknown/" + resourceId);
        
        logStep("验证响应状态码");
        ApiAssert.assertStatusCode(response, 404);
    }
}
