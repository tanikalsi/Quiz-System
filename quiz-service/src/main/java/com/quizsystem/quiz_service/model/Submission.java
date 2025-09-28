package com.quizsystem.quiz_service.model;

import java.util.Map;

// This is a Data Transfer Object (DTO), not an entity for the database.
public class Submission {
    private Long userId;
    private Map<Long, String> answers; // Key: Question ID, Value: Submitted Answer

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
}