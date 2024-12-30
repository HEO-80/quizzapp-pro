package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Api.ApiClient;
import com.example.Api.QuestionApi;
import com.example.Entity.Question;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

//    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa MobileAds.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Referencia al AdView.
//        mAdView = findViewById(R.id.adView);
//        // Crea un AdRequest.
//        AdRequest adRequest = new AdRequest.Builder().build();
//        // Carga el anuncio en el AdView.
//        mAdView.loadAd(adRequest);

        // Cambiado para cargar preguntas desde la API y enviarlas a DashboardActivity
        new Handler().postDelayed(() -> fetchQuestionsForQuickTest(), 1500); // Tiempo de espera.
    }

    // Nuevo método para cargar preguntas de la API
    private void fetchQuestionsForQuickTest() {
        QuestionApi questionApi = ApiClient.getRetrofitInstance().create(QuestionApi.class);

        questionApi.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Question> questionsList = new ArrayList<>(response.body());

                    Log.d("MainActivity", "Preguntas enviadas a DashboardActivity: " + questionsList.size());
                    System.out.println("preguntas enviadas a DashboardActivity");

                    // Enviar preguntas a DashboardActivity
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    intent.putExtra("questionsList", questionsList);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, "No se pudieron cargar las preguntas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red al obtener las preguntas.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
