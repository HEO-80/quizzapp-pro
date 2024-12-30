package com.example.Entity;

import java.util.Date;
import java.util.List;

public class User {

    private int Id;
    private String username;
    private String password;

    private String correoElectronico;

    private Date    fechaNacimiento;


    private List<String> respuestasCorrectas;
    private List<String> respuestasIncorrectas;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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


        // Agrega más propiedades según tus necesidades

    public  User(){

    }




        // Agrega getters y setters para las propiedades

    public int getId() {return Id; }

    public void setId() { this.Id = Id; }


    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
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

    // Agrega métodos adicionales según tus necesidades

}
