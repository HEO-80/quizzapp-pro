package com.example.quizzapp;

import java.util.Date;
import java.util.List;

public class User {

        private int Id;
        private String nombre;
        private String contraseña;
        private String correoElectronico;

        private Date    fechaNacimiento;


        private List<String> respuestasCorrectas;
        private List<String> respuestasIncorrectas;

        // Agrega más propiedades según tus necesidades

        public User(String nombre, String contraseña, String correoElectronico, Date fechaNacimiento) {
            this.nombre = nombre;
            this.contraseña = contraseña;
            this.correoElectronico = correoElectronico;
            this.fechaNacimiento = fechaNacimiento;
        }

        // Agrega getters y setters para las propiedades

    public int getId() {return Id; }

    public void setId() { this.Id = Id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

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
