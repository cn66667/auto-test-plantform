package com.apitest.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class DataUtils {
    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);
    
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter COMPACT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private DataUtils() {
    }
    
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String generateUUIDWithDash() {
        return UUID.randomUUID().toString();
    }
    
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }
    
    public static String getCurrentDateTimeCompact() {
        return LocalDateTime.now().format(COMPACT_FORMATTER);
    }
    
    public static String getCurrentDate() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }
    
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    public static long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
    
    public static double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble() * (max - min);
    }
    
    public static String randomNumeric(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    public static String randomString(int length) {
        if (length <= 0) {
            return "";
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    public static String randomEmail() {
        return "test_" + generateUUID().substring(0, 8) + "@test.com";
    }
    
    public static String randomPhone() {
        String[] prefixes = {"138", "139", "150", "151", "152", "158", "159", "186", "187", "188"};
        String prefix = prefixes[ThreadLocalRandom.current().nextInt(prefixes.length)];
        return prefix + randomNumeric(8);
    }
    
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
    
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }
    
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }
    
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }
    
    public static String defaultIfEmpty(String str, String defaultValue) {
        return StringUtils.defaultIfEmpty(str, defaultValue);
    }
    
    public static String defaultIfBlank(String str, String defaultValue) {
        return StringUtils.defaultIfBlank(str, defaultValue);
    }
    
    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength);
    }
    
    public static String mask(String str, int start, int end, char maskChar) {
        if (str == null || start < 0 || end > str.length() || start >= end) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = start; i < end; i++) {
            sb.setCharAt(i, maskChar);
        }
        return sb.toString();
    }
    
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return mask(phone, 3, 7, '*');
    }
    
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 2) {
            return email;
        }
        return mask(email, 2, atIndex, '*');
    }
}
