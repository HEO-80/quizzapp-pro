package com.example.Entity;

public class UserAnswer {
    private Long quizId;
    private Long questionId;
    private Long userId;
    private String userAnswer;
    private Boolean isCorrect;

    public UserAnswer(Long quizId, Long questionId, Long userId, String userAnswer, Boolean isCorrect) {
        this.quizId = quizId;
        this.questionId = questionId;
        this.userId = userId;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }

    // Getters y Setters
}
