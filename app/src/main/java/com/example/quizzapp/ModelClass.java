package com.example.quizzapp;

import java.io.Serializable;

/**
 * La clase ModelClass representa una pregunta de cuestionario con sus opciones y respuesta.
 */
public class ModelClass implements Serializable {

 int Id;
 String Question;
 String cA;
 String cB;
 String cC;
 String cD;
 String ans;  //respuesta correcta

 String[] categorias = {"Leyes", "Sistemas", "Programacion" , "Seguridad"};

 /**
  * Constructor de la clase ModelClass.
  * @param question la pregunta del cuestionario.
  * @param cA la primera opción de respuesta.
  * @param cB la segunda opción de respuesta.
  * @param cC la tercera opción de respuesta.
  * @param cD la cuarta opción de respuesta.
  * @param ans la respuesta correcta.
  */
 public ModelClass(String question, String cA, String cB, String cC, String cD, String ans, String categorias ) {
  Question = question;
  this.cA = cA;
  this.cB = cB;
  this.cC = cC;
  this.cD = cD;
  this.ans = ans;
  this.getCategorias();
 }

 public int getId() {
  return Id;
 }

 public void setId(int id) {
  Id = id;
 }

 /**
  * Método getter para obtener la pregunta del cuestionario.
  * @return la pregunta del cuestionario.
  */
 public String getQuestion() {
  return Question;
 }

 /**
  * Método setter para establecer la pregunta del cuestionario.
  * @param question la pregunta del cuestionario.
  */
 public void setQuestion(String question) {
  Question = question;
 }
 /**
  * Método getter para obtener la primera opción de respuesta.
  * @return la primera opción de respuesta.
  */
 public String getcA() {
  return cA;
 }

 /**
  * Método setter para establecer la primera opción de respuesta.
  * @param cA la primera opción de respuesta.
  */
 public void setcA(String cA) {
  this.cA = cA;
 }

 /**
  * Método getter para obtener la segunda opción de respuesta.
  * @return la segunda opción de respuesta.
  */
 public String getcB() {
  return cB;
 }

 /**
  * Método setter para establecer la segunda opción de respuesta.
  * @param cB la segunda opción de respuesta.
  */
 public void setcB(String cB) {
  this.cB = cB;
 }

 /**
  * Método getter para obtener la tercera opción de respuesta.
  * @return la tercera opción de respuesta.
  */
 public String getcC() {
  return cC;
 }

 /**
  * Método setter para establecer la tercera opción de respuesta.
  * @param cC la tercera opción de respuesta.
  */
 public void setcC(String cC) {
  this.cC = cC;
 }

 /**
  * Método getter para obtener la cuarta opción de respuesta.
  * @return la cuarta opción de respuesta.
  */
 public String getcD() {
  return cD;
 }

 /**
  * Método setter para establecer la cuarta opción de respuesta.
  * @param cD la cuarta opción de respuesta.
  */
 public void setcD(String cD) {
  this.cD = cD;
 }

 /**
  * Método getter para obtener la respuesta correcta.
  * @return la respuesta correcta.
  */
 public String getAns() {
  return ans;
 }

 /**
  * Método setter para establecer la respuesta correcta.
  * @param ans la respuesta correcta.
  */
 public void setAns(String ans) {
  this.ans = ans;
 }

 public String[] getCategorias() {
  return categorias;
 }

 public void setCategorias(String[] categorias) {
  this.categorias = categorias;
 }
}
