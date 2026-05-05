package com.apitest.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {
    private static final Logger logger = LoggerFactory.getLogger(RegexUtils.class);
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
    private static final Pattern IP_PATTERN = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    
    private RegexUtils() {
    }
    
    public static boolean matches(String input, String regex) {
        if (input == null || regex == null) {
            return false;
        }
        return Pattern.matches(regex, input);
    }
    
    public static boolean find(String input, String regex) {
        if (input == null || regex == null) {
            return false;
        }
        return Pattern.compile(regex).matcher(input).find();
    }
    
    public static String extractFirst(String input, String regex) {
        if (input == null || regex == null) {
            return null;
        }
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
    
    public static String extractGroup(String input, String regex, int groupIndex) {
        if (input == null || regex == null) {
            return null;
        }
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }
    
    public static String replaceAll(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return input.replaceAll(regex, replacement);
    }
    
    public static String replaceFirst(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return input.replaceFirst(regex, replacement);
    }
    
    public static boolean isEmail(String input) {
        return input != null && EMAIL_PATTERN.matcher(input).matches();
    }
    
    public static boolean isPhone(String input) {
        return input != null && PHONE_PATTERN.matcher(input).matches();
    }
    
    public static boolean isIdCard(String input) {
        return input != null && ID_CARD_PATTERN.matcher(input).matches();
    }
    
    public static boolean isIp(String input) {
        return input != null && IP_PATTERN.matcher(input).matches();
    }
    
    public static boolean isUrl(String input) {
        return input != null && URL_PATTERN.matcher(input).matches();
    }
    
    public static String extractVariable(String input) {
        if (input == null) {
            return null;
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
