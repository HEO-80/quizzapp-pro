package com.example.Utils;

/**
 * Clase Constant para definir las constantes de la aplicación.
 */
public class Constant {

    // Base URL de la API
    public static final String BASE_URL = "http://192.168.1.136:8081/api/"; // Asegúrate de usar la IP de tu servidor local o dominio en producción.

    // Endpoints
    public static final String USERS_ENDPOINT = "users";
    public static final String QUESTIONS_ENDPOINT = "questions";
    public static final String QUIZ_USERS_ENDPOINT = "quiz-users";
    public static final String QUIZ_QUESTIONS_ENDPOINT = "quiz-questions";
    public static final String USER_ANSWERS_ENDPOINT = "user-answers";

    // Otros valores constantes
    public static final int TIMEOUT = 30; // Timeout para las solicitudes en segundos
    public static final String DEFAULT_CATEGORY = "Programacion";

    // Mensajes comunes
    public static final String ERROR_NETWORK = "Error de red, por favor intente nuevamente.";
    public static final String ERROR_SERVER = "Error en el servidor, contacte al administrador.";
    public static final String SUCCESS_MESSAGE = "Operación realizada con éxito.";

    private Constant() {
        // Constructor privado para evitar la instanciación de esta clase
    }
}
