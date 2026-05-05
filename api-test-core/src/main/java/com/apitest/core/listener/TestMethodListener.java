package com.apitest.core.listener;

import com.apitest.common.config.ConfigManager;
import com.apitest.core.api.ApiClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
//监听测试方法
public class TestMethodListener implements IInvokedMethodListener {
    private static final Logger logger = LoggerFactory.getLogger(TestMethodListener.class);
    
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String className = testResult.getTestClass().getName();
            String methodName = method.getTestMethod().getMethodName();
            logger.info("开始执行测试: {}#{}", className, methodName);
        }
    }
    
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String className = testResult.getTestClass().getName();
            String methodName = method.getTestMethod().getMethodName();
            long duration = testResult.getEndMillis() - testResult.getStartMillis();
            
            switch (testResult.getStatus()) {
                case ITestResult.SUCCESS:
                    logger.info("测试通过: {}#{} ({}ms)", className, methodName, duration);
                    break;
                case ITestResult.FAILURE:
                    logger.error("测试失败: {}#{} ({}ms) - {}", className, methodName, duration, 
                            testResult.getThrowable() != null ? testResult.getThrowable().getMessage() : "Unknown");
                    break;
                case ITestResult.SKIP:
                    logger.warn("测试跳过: {}#{} ({}ms)", className, methodName, duration);
                    break;
            }
            
            ApiClientFactory.reset();
        }
    }
}