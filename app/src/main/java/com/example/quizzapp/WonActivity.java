package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
    ImageView resultImage;
    int correct, wrong;
    LinearLayout btnShare, btnReset, btnInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        // Obtén los datos de las respuestas correctas y erróneas
        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);

        // Vinculación de vistas
        circularProgressBar = findViewById(R.id.circularProgressBar);
        resultText = findViewById(R.id.resultText);
        resultImage = findViewById(R.id.resultImage);
        btnShare = findViewById(R.id.bntShare);
        btnReset = findViewById(R.id.btnReset);
        btnInicial = findViewById(R.id.btnInicio); // Corregido

        // Configuración de la barra de progreso y el texto de resultados
        circularProgressBar.setProgress(correct);
        resultText.setText(correct + "/20");

        // Mostrar la imagen correcta según el resultado
        if (correct == 20) {
            resultImage.setImageResource(R.drawable.perfect);
        } else if (correct > 9) {
            resultImage.setImageResource(R.drawable.youwin);
        } else {
            resultImage.setImageResource(R.drawable.youlose);
        }

        // Acción para compartir los resultados
        btnShare.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage += "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Acción para reiniciar el cuestionario
        btnReset.setOnClickListener(v -> {
            Intent intent = new Intent(WonActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Acción para ir a la pantalla inicial
        if (btnInicial != null) {
            btnInicial.setOnClickListener(v -> {
                Intent intent = new Intent(WonActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("WonActivity", "btnInicial is null. Check the XML ID.");
        }
    }
}
