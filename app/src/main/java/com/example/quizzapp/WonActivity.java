package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

/**
 * Clase WonActivity que hereda de AppCompatActivity.
 * Muestra los resultados de un cuestionario y permite compartir los resultados.
 */
public class WonActivity extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView resultText;

    int correct, wrong;
    LinearLayout btnShare;

    LinearLayout btnReset;

    /**
     * Método onCreate que se llama cuando se crea la actividad.
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);

        circularProgressBar = findViewById(R.id.circularProgressBar);
        resultText = findViewById(R.id.resultText);
        btnShare = findViewById(R.id.bntShare);
        btnReset = findViewById(R.id.btnReset);
        circularProgressBar.setProgress(correct);
        resultText.setText(correct + "/20");

        btnShare.setOnClickListener(new View.OnClickListener() {
            /**
             * Método onClick que se llama cuando se hace clic en el botón de compartir.
             * @param v View en la que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\n Let me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar DashboardActivity (o MainActivity si el juego comienza allí)
                Intent intent = new Intent(WonActivity.this, DashboardActivity.class);
                startActivity(intent);

                // Finalizar WonActivity para evitar que se acumule en la pila de actividades
                finish();
            }
        });
    }
}
