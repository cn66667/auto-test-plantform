package com.apitest.common.enums;

public enum Environment {
    DEV("dev"),
    TEST("test"),
    UAT("uat"),
    PROD("prod");
    
    private final String name;
    
    Environment(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static Environment fromString(String name) {
        if (name == null) {
            return TEST;
        }
        for (Environment env : Environment.values()) {
            if (env.name.equalsIgnoreCase(name)) {
                return env;
            }
        }
        return TEST;
    }
}
