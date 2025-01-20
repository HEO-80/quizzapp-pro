package com.example.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class User {

    private Long  id;
    private String username;
    private String password;
    @SerializedName("email")
    private String email;

    private Date    fechaNacimiento;


    private List<String> respuestasCorrectas;
    private List<String> respuestasIncorrectas;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public  User(){

    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long  getId() {return id; }

    public void setId(Long  id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    public void agregarRespuestaCorrecta(String respuestaCorrecta) {
        respuestasCorrectas.add(respuestaCorrecta);
    }

    public void agregarRespuestaIncorrecta(String respuestaIncorrecta) {
        respuestasIncorrectas.add(respuestaIncorrecta);
    }

    public List<String> getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public List<String> getRespuestasIncorrectas() {
        return respuestasIncorrectas;
    }


}
