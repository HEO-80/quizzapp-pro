package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

/**
 * Clase MainActivity que hereda de AppCompatActivity.
 * Muestra una pantalla de bienvenida y redirige a StartActivity.
 */
public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

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
        mAdView = findViewById(R.id.adView);
        // Crea un AdRequest.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Carga el anuncio en el AdView.
        mAdView.loadAd(adRequest);

        // Crear un Handler para cargar DashboardActivity después de un tiempo de espera.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500); // Tiempo de espera para cargar DashboardActivity.
    }
}