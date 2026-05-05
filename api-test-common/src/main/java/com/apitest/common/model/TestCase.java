package com.apitest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
    private String id;
    private String name;
    private String description;
    private String feature;
    private String story;
    private String severity;
    private String[] tags;
    private Boolean enabled;
    
    public static TestCase of(String id, String name) {
        return TestCase.builder()
                .id(id)
                .name(name)
                .enabled(true)
                .build();
    }
    
    public static TestCase of(String id, String name, String description) {
        return TestCase.builder()
                .id(id)
                .name(name)
                .description(description)
                .enabled(true)
                .build();
    }
}
