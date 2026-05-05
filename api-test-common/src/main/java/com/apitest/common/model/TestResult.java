package com.apitest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {
    private String caseId;
    private String caseName;
    private boolean passed;
    private long duration;
    private String errorMessage;
    private String stackTrace;
    @Builder.Default
    private List<String> steps = new ArrayList<>();
    
    public void addStep(String step) {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        steps.add(step);
    }
    
    public static TestResult success(String caseId, String caseName, long duration) {
        return TestResult.builder()
                .caseId(caseId)
                .caseName(caseName)
                .passed(true)
                .duration(duration)
                .build();
    }
    
    public static TestResult failure(String caseId, String caseName, long duration, String errorMessage) {
        return TestResult.builder()
                .caseId(caseId)
                .caseName(caseName)
                .passed(false)
                .duration(duration)
                .errorMessage(errorMessage)
                .build();
    }
}
