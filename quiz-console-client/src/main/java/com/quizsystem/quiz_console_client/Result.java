package com.quizsystem.quiz_console_client;

public class Result {
    private Long id;
    private Long quizId;
    private int score;
    private int totalQuestions;
    private String attemptedOn;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    public String getAttemptedOn() { return attemptedOn; }
    public void setAttemptedOn(String attemptedOn) { this.attemptedOn = attemptedOn; }

    @Override
    public String toString() {
        return "Result on Quiz ID " + quizId + ": Scored " + score + " out of " + totalQuestions + " on " + attemptedOn;
    }
}