package com.apitest.cases.testcases;

import com.apitest.cases.BaseTest;
import com.apitest.common.model.ApiRequest;
import com.apitest.common.model.ApiResponse;
import com.apitest.core.assertion.ApiAssert;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("用户管理")
@Feature("用户接口")
public class UserApiTest extends BaseTest {
    
    @Test(description = "获取用户列表")
    @Story("查询用户")
    @Severity(SeverityLevel.CRITICAL)
    @Description("测试获取用户列表接口，验证返回数据格式和状态码")
    public void testGetUserList() {
        logStep("发送获取用户列表请求");
        ApiResponse response = apiClient.get("/users");
        
        logStep("验证响应状态码");
        ApiAssert.assertSuccess(response);
        ApiAssert.assertBodyNotEmpty(response);
        
        logStep("验证响应数据结构");
        ApiAssert.assertJsonPathIsList(response, "data");
    }
    
    @Test(description = "获取单个用户")
    @Story("查询用户")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试根据ID获取单个用户信息")
    public void testGetUserById() {
        String userId = "2";
        
        logStep("发送获取用户请求: userId=" + userId);
        ApiResponse response = apiClient.get("/users/" + userId);
        
        logStep("验证响应状态码");
        ApiAssert.assertSuccess(response);
        
        logStep("验证用户数据");
        ApiAssert.assertJsonPathEquals(response, "data.id", 2);
        ApiAssert.assertJsonPathNotEmpty(response, "data.email");
        ApiAssert.assertJsonPathNotEmpty(response, "data.first_name");
    }
    
    @Test(description = "创建用户")
    @Story("用户管理")
    @Severity(SeverityLevel.CRITICAL)
    @Description("测试创建新用户接口")
    public void testCreateUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Test User");
        user.put("job", "QA Engineer");
        
        logStep("发送创建用户请求");
        ApiResponse response = apiClient.post("/users", user);
        
        logStep("验证响应状态码");
        ApiAssert.assertStatusCode(response, 201);
        
        logStep("验证创建的用户数据");
        ApiAssert.assertJsonPathNotEmpty(response, "id");
        ApiAssert.assertJsonPathEquals(response, "name", "Test User");
        ApiAssert.assertJsonPathEquals(response, "job", "QA Engineer");
    }
    
    @Test(description = "更新用户")
    @Story("用户管理")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试更新用户信息接口")
    public void testUpdateUser() {
        String userId = "2";
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Updated User");
        user.put("job", "Senior QA Engineer");
        
        logStep("发送更新用户请求: userId=" + userId);
        ApiResponse response = apiClient.put("/users/" + userId, user);
        
        logStep("验证响应状态码");
        ApiAssert.assertSuccess(response);
        
        logStep("验证更新后的用户数据");
        ApiAssert.assertJsonPathEquals(response, "name", "Updated User");
        ApiAssert.assertJsonPathEquals(response, "job", "Senior QA Engineer");
    }
    
    @Test(description = "删除用户")
    @Story("用户管理")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试删除用户接口")
    public void testDeleteUser() {
        String userId = "2";
        
        logStep("发送删除用户请求: userId=" + userId);
        ApiResponse response = apiClient.delete("/users/" + userId);
        
        logStep("验证响应状态码");
        ApiAssert.assertStatusCode(response, 204);
    }
    
    @Test(description = "用户登录成功")
    @Story("用户认证")
    @Severity(SeverityLevel.BLOCKER)
    @Description("测试用户登录接口，验证登录成功场景")
    public void testLoginSuccess() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "cityslicka");
        
        logStep("发送登录请求");
        ApiResponse response = apiClient.post("/login", credentials);
        
        logStep("验证响应状态码");
        ApiAssert.assertSuccess(response);
        
        logStep("验证返回token");
        ApiAssert.assertJsonPathNotEmpty(response, "token");
    }
    
    @Test(description = "用户登录失败-缺少密码")
    @Story("用户认证")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试用户登录接口，验证缺少密码时的错误处理")
    public void testLoginFailedWithoutPassword() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "eve.holt@reqres.in");
        
        logStep("发送登录请求(缺少密码)");
        ApiResponse response = apiClient.post("/login", credentials);
        
        logStep("验证响应状态码");
        ApiAssert.assertStatusCode(response, 400);
        
        logStep("验证错误信息");
        ApiAssert.assertJsonPathNotEmpty(response, "error");
    }
}
