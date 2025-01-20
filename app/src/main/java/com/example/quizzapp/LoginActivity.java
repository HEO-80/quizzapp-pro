package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Api.ApiClient;
import com.example.Api.ApiService;
import com.example.Entity.LoginResponseDTO;
import com.example.Entity.User;
import com.example.Utils.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLoginSubmit, btnRegisterSubmit;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        sessionManager = new SessionManager(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);

        btnLoginSubmit.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();


            if (!username.isEmpty() && !password.isEmpty()) {
                loginUser(username, password); // Llamar a la función de login
            } else {
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón de registro para navegar a RegisterActivity
        btnRegisterSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Realiza la autenticación del usuario utilizando Retrofit.
     */
    private void loginUser(String username, String password) {
        ApiService apiService = ApiClient.getRetrofitInstance(LoginActivity.this)
                                            .create(ApiService.class);

        // Crear un objeto User con las propiedades relevantes
        User user = new User(username, password);


        // Llamar al endpoint de login con Retrofit
        apiService.login(user).enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                if (response.isSuccessful()) {

                        LoginResponseDTO loginResponse = response.body();

                        if (loginResponse != null) {


                            // (1) Tomamos datos devueltos por el servidor
                            String tokenDelServidor = loginResponse.getAccessToken();
                            String usernameDelServidor = loginResponse.getUsername();
                            Long userIdDelServidor = loginResponse.getUserId();
                            // (1) Tomar el token que viene desde el servidor
                            String token = loginResponse.getAccessToken(); // <- NUEVO
                            // (2) Guardamos el token en SessionManager
                            sessionManager.saveToken(tokenDelServidor);

                            // (3) Guardamos la info del usuario en SessionManager (el email es un placeholder)
                            sessionManager.saveUser(usernameDelServidor, "[email protected]", userIdDelServidor);


                            Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                            // Navegar a la pantalla inicial
                            Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Error: Respuesta vacía del servidor", Toast.LENGTH_SHORT)
                                    .show();
                        }
                } else {
                    // Respuesta HTTP fuera del rango 2xx (400, 401, 500, etc.)
                    try {
                        // Si el servidor envía algo en el body de error
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessage = jsonObject.optString("message", "Error desconocido");

                        Toast.makeText(LoginActivity.this,
                                "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this,
                                "Error durante el login", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
