package com.example.Entity;

import java.io.Serializable;

public class UserAnswer implements Serializable {
    private Long quizId;
    private Long questionId;
    private Long userId;
    private String userAnswer;
    private boolean isCorrect;

    // Constructor
    public UserAnswer(Long quizId, Long questionId, Long userId, String userAnswer, boolean isCorrect) {
        this.quizId = quizId;
        this.questionId = questionId;
        this.userId = userId;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }

    // Getters y Setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}

//    private Long quizId;
//    private Long questionId;
//    private Long userId;
//    private String userAnswer;
//    private Boolean isCorrect;
//
//    public UserAnswer(Long quizId, Long questionId, Long userId, String userAnswer, Boolean isCorrect) {
//        this.quizId = quizId;
//        this.questionId = questionId;
//        this.userId = userId;
//        this.userAnswer = userAnswer;
//        this.isCorrect = isCorrect;
//    }
//
//    // Getters y Setters
//}
