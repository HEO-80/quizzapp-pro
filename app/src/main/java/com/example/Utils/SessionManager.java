package com.example.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_ID = "user_id"; // Almacenar el ID del usuario

    private static final String KEY_TOKEN = "access_token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(String username, String password, Long userId) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putLong(KEY_USER_ID, userId); // Almacenar el ID del usuario
        editor.apply();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }
//    public void saveUserId(Long userId) {
//        editor.putLong(KEY_USER_ID, userId);
//        editor.apply();
//    }
    /**
     * Verifica si el usuario es "guest".
     */
    public boolean isGuest() {
        return "guest".equals(getUsername()) && "guest".equals(sharedPreferences.getString(KEY_PASSWORD, ""));
    }

    /**
     * Obtiene el ID del usuario almacenado.
     */
    public Long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1); // -1 indica que no hay ID
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "guest");
    }
    /**
     * Borra todos los datos de la sesión.
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
