package com.example.quizzapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Api.ApiClient;
import com.example.Api.ApiService;
import com.example.Entity.User;
import com.example.Utils.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etConfirmEmail;
    private Button btnRegisterSubmit;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        sessionManager = new SessionManager(this);
        etUsername = findViewById(R.id.etUsernameRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        etEmail = findViewById(R.id.etEmailRegister);
        etConfirmEmail = findViewById(R.id.etConfirmEmailRegister);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);

        btnRegisterSubmit.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String confirmEmail = etConfirmEmail.getText().toString().trim();

            if (validateInput(username, password, email, confirmEmail)) {
                registerUser(username, password, email);
            }
        });
    }

    /**
     * Validar los campos de entrada del usuario.
     */
    private boolean validateInput(String username, String password, String email, String confirmEmail) {
        boolean isValid = true;

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("El nombre de usuario es obligatorio");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("La contraseña es obligatoria");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("El correo electrónico es obligatorio");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Ingresa un correo electrónico válido");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmEmail)) {
            etConfirmEmail.setError("Confirma tu correo electrónico");
            isValid = false;
        } else if (!email.equals(confirmEmail)) {
            etConfirmEmail.setError("Los correos electrónicos no coinciden");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Realizar la solicitud de registro al backend.
     */
    private void registerUser(String username, String password, String email) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Crear un objeto User con las propiedades relevantes
        User user = new User(username, password, email);

        // Llamar al endpoint de registro con Retrofit
        apiService.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registro exitoso. Puedes iniciar sesión ahora.", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad y volver al login
                } else {
                    try {
                        // Obtener el mensaje de error
                        String errorBody = response.errorBody().string();

                        try {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String errorMessage = jsonObject.getString("message");
                            Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception jsonEx){
                            // Si falla, asumir que es texto plano
                            Toast.makeText(RegisterActivity.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                        Toast.makeText(RegisterActivity.this, "Error desconocido durante el registro.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
