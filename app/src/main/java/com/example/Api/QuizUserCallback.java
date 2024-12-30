package com.example.Api;

public interface QuizUserCallback {
    void onSuccess(Long quizId);
    void onFailure(String errorMessage);
}
