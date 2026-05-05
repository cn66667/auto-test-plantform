package com.apitest.common.config;

import com.apitest.common.constants.CommonConstants;
import com.apitest.common.enums.Environment;
import com.apitest.common.exception.ConfigException;
import com.apitest.common.model.EnvironmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    
    private static final ConfigManager INSTANCE = new ConfigManager();
    
    private Properties properties;
    private Environment currentEnvironment;
    private EnvironmentConfig environmentConfig;
    private Map<String, String> globalVariables;
    
    private ConfigManager() {
        this.properties = new Properties();
        this.globalVariables = new HashMap<>();
        loadProperties();
        initEnvironment();
    }
    
    public static ConfigManager getInstance() {
        return INSTANCE;
    }
    
    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CommonConstants.CONFIG_FILE_NAME)) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("配置文件加载成功: {}", CommonConstants.CONFIG_FILE_NAME);
            } else {
                logger.warn("配置文件不存在，使用默认配置");
            }
        } catch (IOException e) {
            logger.warn("加载配置文件失败: {}", e.getMessage());
        }
    }
    
    private void initEnvironment() {
        String envName = getProperty("environment", "test");
        this.currentEnvironment = Environment.fromString(envName);
        this.environmentConfig = buildEnvironmentConfig();
        logger.info("当前环境: {}", currentEnvironment.getName());
    }
    
    private EnvironmentConfig buildEnvironmentConfig() {
        String envPrefix = CommonConstants.ENV_CONFIG_PREFIX + currentEnvironment.getName() + ".";
        
        EnvironmentConfig config = EnvironmentConfig.builder()
                .name(currentEnvironment.getName())
                .baseUrl(getProperty(envPrefix + "baseUrl", ""))
                .timeout(getIntProperty(envPrefix + "timeout", CommonConstants.DEFAULT_TIMEOUT))
                .connectTimeout(getIntProperty(envPrefix + "connectTimeout", CommonConstants.DEFAULT_CONNECT_TIMEOUT))
                .readTimeout(getIntProperty(envPrefix + "readTimeout", CommonConstants.DEFAULT_READ_TIMEOUT))
                .defaultHeaders(new HashMap<>())
                .variables(new HashMap<>())
                .build();
        
        return config;
    }
    
    public String getProperty(String key) {
        //先从JVM系统属性拿
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        //再从环境变量拿
        value = System.getenv(key);
        if (value != null) {
            return value;
        }
        //最后从配置文件拿
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.warn("配置项 {} 的值 {} 不是有效的整数，使用默认值 {}", key, value, defaultValue);
            }
        }
        return defaultValue;
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    public Environment getCurrentEnvironment() {
        return currentEnvironment;
    }
    
    public void setCurrentEnvironment(Environment environment) {
        this.currentEnvironment = environment;
        this.environmentConfig = buildEnvironmentConfig();
        logger.info("切换环境: {}", environment.getName());
    }
    
    public EnvironmentConfig getEnvironmentConfig() {
        return environmentConfig;
    }
    
    public String getBaseUrl() {
        return environmentConfig.getBaseUrl();
    }
    
    public void setGlobalVariable(String key, String value) {
        globalVariables.put(key, value);
        logger.debug("设置全局变量: {} = {}", key, value);
    }
    
    public String getGlobalVariable(String key) {
        return globalVariables.get(key);
    }
    
    public void removeGlobalVariable(String key) {
        globalVariables.remove(key);
    }
    
    public void clearGlobalVariables() {
        globalVariables.clear();
    }
    
    public Map<String, String> getGlobalVariables() {
        return new HashMap<>(globalVariables);
    }
    
    public void reload() {
        properties.clear();
        loadProperties();
        initEnvironment();
        logger.info("配置重新加载完成");
    }
}
