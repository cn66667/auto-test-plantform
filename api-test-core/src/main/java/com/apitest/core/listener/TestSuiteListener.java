package com.apitest.core.listener;

import com.apitest.common.config.ConfigManager;
import com.apitest.common.enums.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
//监听测试套件
public class TestSuiteListener implements ISuiteListener {
    private static final Logger logger = LoggerFactory.getLogger(TestSuiteListener.class);
    
    @Override
    public void onStart(ISuite suite) {
        logger.info("========================================");
        logger.info("测试套件开始: {}", suite.getName());
        logger.info("========================================");
        
        String env = suite.getParameter("environment");
        if (env != null && !env.isEmpty()) {
            Environment environment = Environment.fromString(env);
            ConfigManager.getInstance().setCurrentEnvironment(environment);
        }
        
        logger.info("当前环境: {}", ConfigManager.getInstance().getCurrentEnvironment().getName());
        logger.info("基础URL: {}", ConfigManager.getInstance().getBaseUrl());
    }
    
    @Override
    public void onFinish(ISuite suite) {
        logger.info("========================================");
        logger.info("测试套件结束: {}", suite.getName());
        logger.info("========================================");
    }
}
