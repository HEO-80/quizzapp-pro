package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Utils.SessionManager;

public class StartActivity extends AppCompatActivity {

    private Button btnStart, btnCategory, btnLogin, btnLogout,btnStatistics;
    private ImageButton btnInfo;
    private TextView userNameDisplay;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Inicializar vistas
        btnStart = findViewById(R.id.btnStart);
        btnCategory = findViewById(R.id.btnCategory);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        btnInfo = findViewById(R.id.btnInfo);
        userNameDisplay = findViewById(R.id.user_name_display); // Inicializar correctamente el TextView

        btnStatistics = findViewById(R.id.btnStatistics);

        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        sessionManager = new SessionManager(this);

        // Obtener el usuario actual o "guest" por defecto
        String currentUser = sessionManager.getUsername();
        userNameDisplay.setText("Usuario actual: " + currentUser);

        // Controlar visibilidad del botón Logout
        if (sessionManager.isGuest()) {
            btnLogout.setVisibility(View.GONE); // Ocultar el botón si el usuario es "guest"
        } else {
            btnLogout.setVisibility(View.VISIBLE); // Mostrar el botón si es otro usuario
        }

        // Listeners de botones
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnCategory.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(this, "Cerraste sesión. Usuario invitado activado.", Toast.LENGTH_SHORT).show();
            recreate(); // Reinicia la actividad para mostrar el usuario "guest".
        });

        // Listener para el botón de información
        btnInfo.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, InfoActivity.class);
            startActivity(intent);
        });
    }
}
