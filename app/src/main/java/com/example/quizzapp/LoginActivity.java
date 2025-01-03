package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Api.ApiClient;
import com.example.Api.ApiService;
import com.example.Entity.User;
import com.example.Utils.SessionManager;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLoginSubmit;
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

        btnLoginSubmit.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                loginUser(username, password); // Llamar a la función de login
            } else {
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Realiza la autenticación del usuario utilizando Retrofit.
     */
    private void loginUser(String username, String password) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Crear un objeto User con las propiedades relevantes
        User user = new User(username, password);


        // Llamar al endpoint de login con Retrofit
        apiService.login(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Convertir la respuesta a JSON y obtener el nombre de usuario
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String username = jsonResponse.getString("username");
                        long userId = jsonResponse.getLong("userId"); // Asegúrate de que esto está disponible en el servidor

                        // Guardar el nombre de usuario en el SessionManager
                        sessionManager.saveUser(username, ""); // La contraseña puede estar vacía si no se necesita
                        sessionManager.saveUserId(userId); // Guarda el userId aquí

                        // Procesar la respuesta en caso de éxito
                    Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                    // Aquí puedes guardar la sesión del usuario si es necesario

                        // Regresar a la pantalla inicial
                        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Error procesando la respuesta", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    // Manejar el caso de error en la respuesta
                    Toast.makeText(LoginActivity.this, "Login fallido: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {

                    Toast.makeText(LoginActivity.this, "Error: ",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                // Manejar errores de red o en la llamada
                Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
                }
        });
    }
}
