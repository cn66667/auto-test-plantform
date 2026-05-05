package com.apitest.cases.testcases;

import com.apitest.cases.BaseTest;
import com.apitest.common.model.ApiResponse;
import com.apitest.core.assertion.ApiAssert;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("用户管理")
@Feature("注册接口")
public class RegisterApiTest extends BaseTest {
    
    @Test(description = "用户注册成功")
    @Story("用户注册")
    @Severity(SeverityLevel.BLOCKER)
    @Description("测试用户注册接口，验证注册成功场景")
    public void testRegisterSuccess() {
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");
        
        logStep("发送注册请求");
        ApiResponse response = apiClient.post("/register", user);
        
        logStep("验证响应状态码");
        ApiAssert.assertStatusCode(response, 200);
        
        logStep("验证返回数据");
        ApiAssert.assertJsonPathNotEmpty(response, "id");
        ApiAssert.assertJsonPathNotEmpty(response, "token");
    }
    
    @Test(description = "用户注册失败-缺少密码")
    @Story("用户注册")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试用户注册接口，验证缺少密码时的错误处理")
    public void testRegisterFailedWithoutPassword() {
        Map<String, String> user = new HashMap<>();
        user.put("email", "sydney@fife");
        
        logStep("发送注册请求(缺少密码)");
        ApiResponse response = apiClient.post("/register", user);
        
        logStep("验证响应状态码");
        ApiAssert.assertStatusCode(response, 400);
        
        logStep("验证错误信息");
        ApiAssert.assertJsonPathNotEmpty(response, "error");
    }
    
    @Test(description = "用户注册失败-缺少邮箱")
    @Story("用户注册")
    @Severity(SeverityLevel.NORMAL)
    @Description("测试用户注册接口，验证缺少邮箱时的错误处理")
    public void testRegisterFailedWithoutEmail() {
        Map<String, String> user = new HashMap<>();
        user.put("password", "pistol");
        
        logStep("发送注册请求(缺少邮箱)");
        ApiResponse response = apiClient.post("/register", user);
        
        logStep("验证响应状态码");
        ApiAssert.assertStatusCode(response, 400);
        
        logStep("验证错误信息");
        ApiAssert.assertJsonPathNotEmpty(response, "error");
    }
}
