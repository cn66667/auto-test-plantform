package com.apitest.core.handler;

import com.apitest.common.config.ConfigManager;
import com.apitest.common.utils.RegexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableHandler {
    private static final Logger logger = LoggerFactory.getLogger(VariableHandler.class);
    
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\$\\{env\\.([^}]+)\\}");
    private static final Pattern GLOBAL_VAR_PATTERN = Pattern.compile("\\$\\{global\\.([^}]+)\\}");
    private static final Pattern CONFIG_VAR_PATTERN = Pattern.compile("\\$\\{config\\.([^}]+)\\}");
    
    public static String resolve(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String result = input;
        result = resolveConfigVariables(result);
        result = resolveGlobalVariables(result);
        result = resolveEnvVariables(result);
        result = resolveSystemVariables(result);
        
        return result;
    }
    
    public static String resolveConfigVariables(String input) {
        if (input == null) {
            return null;
        }
        
        Matcher matcher = CONFIG_VAR_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = ConfigManager.getInstance().getProperty(varName, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    public static String resolveGlobalVariables(String input) {
        if (input == null) {
            return null;
        }
        
        Matcher matcher = GLOBAL_VAR_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = ConfigManager.getInstance().getGlobalVariable(varName);
            if (value == null) {
                value = "";
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    public static String resolveEnvVariables(String input) {
        if (input == null) {
            return null;
        }
        
        Matcher matcher = ENV_VAR_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = System.getenv(varName);
            if (value == null) {
                value = "";
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    public static String resolveSystemVariables(String input) {
        if (input == null) {
            return null;
        }
        
        Matcher matcher = VARIABLE_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String varName = matcher.group(1);
            
            if (varName.startsWith("env.") || varName.startsWith("global.") || varName.startsWith("config.")) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement("${" + varName + "}"));
                continue;
            }
            
            String value = ConfigManager.getInstance().getGlobalVariable(varName);
            if (value == null) {
                value = ConfigManager.getInstance().getProperty(varName, "");
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    public static Map<String, String> resolveMap(Map<String, String> input) {
        if (input == null) {
            return null;
        }
        
        java.util.Map<String, String> result = new java.util.HashMap<>();
        for (Map.Entry<String, String> entry : input.entrySet()) {
            result.put(entry.getKey(), resolve(entry.getValue()));
        }
        return result;
    }
    
    public static void setGlobalVariable(String name, String value) {
        ConfigManager.getInstance().setGlobalVariable(name, value);
        logger.debug("设置全局变量: {} = {}", name, value);
    }
    
    public static String getGlobalVariable(String name) {
        return ConfigManager.getInstance().getGlobalVariable(name);
    }
    
    public static void removeGlobalVariable(String name) {
        ConfigManager.getInstance().removeGlobalVariable(name);
    }
    
    public static void clearGlobalVariables() {
        ConfigManager.getInstance().clearGlobalVariables();
    }
}
