package com.example.Entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuizUser implements Serializable {
    private Long id; // Añade este campo
    private Long userId;
    private String category;
    // Si decides usar LocalDateTime en lugar de String:

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String startTime = sdf.format(new Date()); // Fecha y hora actual
    String endTime = sdf.format(new Date()); // Cambia por el tiempo correcto

    private int score;

    // Constructor sin argumentos
    public QuizUser() {}
    // Constructor
    public QuizUser(Long userId, String category, String startTime, String endTime, int score) {
        this.userId = userId;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
    }

    // Getters y Setters

    public Long getId() { // Añade este getter
        return id;
    }

    public void setId(Long id) { // Añade este setter si es necesario
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
