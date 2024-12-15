package com.example.Entity;

public class Question {
    private Long id;
    private String questionText;
    private String category;
    private String correctAnswer;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    // Getters y Setters

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
