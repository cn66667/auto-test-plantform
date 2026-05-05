package com.apitest.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    
    private FileUtils() {
    }
    
    public static String readFileToString(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("读取文件失败: {}", filePath, e);
            throw new RuntimeException("读取文件失败: " + filePath, e);
        }
    }
    
    public static String readFileFromClasspath(String fileName) {
        try (InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("文件不存在: " + fileName);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            logger.error("从classpath读取文件失败: {}", fileName, e);
            throw new RuntimeException("读取文件失败: " + fileName, e);
        }
    }
    
    public static void writeStringToFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error("写入文件失败: {}", filePath, e);
            throw new RuntimeException("写入文件失败: " + filePath, e);
        }
    }
    
    public static void appendToFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8), 
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("追加写入文件失败: {}", filePath, e);
            throw new RuntimeException("追加写入文件失败: " + filePath, e);
        }
    }
    
    public static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    public static boolean isFile(String filePath) {
        return Files.isRegularFile(Paths.get(filePath));
    }
    
    public static boolean isDirectory(String filePath) {
        return Files.isDirectory(Paths.get(filePath));
    }
    
    public static void createDirectory(String dirPath) {
        try {
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) {
            logger.error("创建目录失败: {}", dirPath, e);
            throw new RuntimeException("创建目录失败: " + dirPath, e);
        }
    }
    
    public static void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("删除文件失败: {}", filePath, e);
            throw new RuntimeException("删除文件失败: " + filePath, e);
        }
    }
    
    public static void deleteDirectory(String dirPath) {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted((a, b) -> -a.compareTo(b))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            logger.error("删除失败: {}", p, e);
                        }
                    });
        } catch (IOException e) {
            logger.error("删除目录失败: {}", dirPath, e);
            throw new RuntimeException("删除目录失败: " + dirPath, e);
        }
    }
    
    public static List<String> listFiles(String dirPath, String extension) {
        try (Stream<Path> walk = Files.walk(Paths.get(dirPath))) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(p -> extension == null || p.toString().endsWith(extension))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("列出文件失败: {}", dirPath, e);
            throw new RuntimeException("列出文件失败: " + dirPath, e);
        }
    }
    
    public static String getFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }
    
    public static String getFileExtension(String filePath) {
        String fileName = getFileName(filePath);
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
    
    public static String getFileDirectory(String filePath) {
        Path parent = Paths.get(filePath).getParent();
        return parent != null ? parent.toString() : "";
    }
    
    public static long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("获取文件大小失败: {}", filePath, e);
            return -1;
        }
    }
}
