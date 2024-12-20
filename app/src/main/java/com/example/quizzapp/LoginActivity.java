package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLoginSubmit;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa el SessionManager y las vistas
        sessionManager = new SessionManager(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);

        // Configura el botón para manejar el inicio de sesión
        btnLoginSubmit.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateCredentials(username, password)) {
                if ("guest".equals(username)) {
                    // No guarda las credenciales de invitados
                    Toast.makeText(LoginActivity.this, "Sesión iniciada como Invitado", Toast.LENGTH_SHORT).show();
                } else {
                    // Guarda las credenciales en SessionManager
                    sessionManager.saveUser(username, password);
                    Toast.makeText(LoginActivity.this, "Sesión iniciada como " + username, Toast.LENGTH_SHORT).show();
                }

                // Navega de regreso a la pantalla de inicio
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Valida las credenciales del usuario.
     * Si es el usuario invitado, devuelve true.
     * Si es otro usuario, verifica que el nombre no esté vacío.
     */
    private boolean validateCredentials(String username, String password) {
        return ("guest".equals(username) && "guest".equals(password)) || (!username.isEmpty() && !password.isEmpty());
    }
}
