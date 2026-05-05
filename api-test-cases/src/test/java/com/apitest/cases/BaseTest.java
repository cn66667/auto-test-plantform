package com.apitest.cases;

import com.apitest.common.config.ConfigManager;
import com.apitest.core.api.ApiClient;
import com.apitest.core.api.ApiClientFactory;
import com.apitest.core.handler.VariableHandler;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public abstract class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected ApiClient apiClient;
    
    @BeforeMethod
    public void setUp(Method method) {
        logger.info("初始化测试: {}", method.getName());
        String apiKey = getConfig("env.test.apiKey");
        logger.info("当前读取到的API Key: {}", apiKey);
        apiClient = ApiClientFactory.getClient();
    }
    
    @AfterMethod
    public void tearDown(Method method) {
        logger.info("清理测试: {}", method.getName());
        ApiClientFactory.reset();
    }
    
    @Step("{stepName}")
    protected void step(String stepName, Runnable action) {
        action.run();
    }
    
    protected void logStep(String stepName) {
        logger.info("执行步骤: {}", stepName);
        Allure.step(stepName);
    }
    //添加文本附件
    protected void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }
    //添加JSON附件
    protected void attachJson(String name, String json) {
        Allure.addAttachment(name, "application/json", json);
    }
    
    protected String resolveVariable(String input) {
        return VariableHandler.resolve(input);
    }
    
    protected String getConfig(String key) {
        return ConfigManager.getInstance().getProperty(key);
    }
    
    protected String getConfig(String key, String defaultValue) {
        return ConfigManager.getInstance().getProperty(key, defaultValue);
    }
    
    protected void setGlobalVariable(String key, String value) {
        VariableHandler.setGlobalVariable(key, value);
    }
    
    protected String getGlobalVariable(String key) {
        return VariableHandler.getGlobalVariable(key);
    }
    
    protected String getBaseUrl() {
        return ConfigManager.getInstance().getBaseUrl();
    }
}
