package com.example.Entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

// Quiz realizados por el usuario, guarda el id del quiz,
// el id del usuario: userId, la categoria
// la hora y la puntuacion: score

public class QuizUser implements Serializable {
    private Long id;
    private Long userId;
    private String category;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String startTime = sdf.format(new Date()); // Fecha y hora actual
    String endTime = sdf.format(new Date());

    private Integer score;

    // Constructor sin argumentos
    public QuizUser() {}
    // Constructor
    public QuizUser(Long userId, String category, String startTime, String endTime, Integer score) {
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
