package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends AppCompatActivity {

    Button categoriaLeyesBtn;
    Button categoriaProgramacionBtn;
    Button categoriaSistemasBtn;
    Button categoriaSeguridadBtn;

    private String selectedCategoria; // Variable de instancia para la categoría seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);


        categoriaLeyesBtn = findViewById(R.id.categoriaLeyesBtn);
        categoriaSistemasBtn = findViewById(R.id.categoriaSistemasBtn);
        categoriaProgramacionBtn = findViewById(R.id.categoriaProgramacionBtn);
        categoriaSeguridadBtn = findViewById(R.id.categoriaSeguridadBtn);


        categoriaLeyesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoria = "Leyes";

                List<ModelClass> filteredQuestionsList = filterQuestionsByCategory(selectedCategoria);

                Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
                intent.putExtra("questionsList", (ArrayList<ModelClass>) filteredQuestionsList);
                startActivity(intent);
            }
        });

        categoriaSistemasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoria = "Sistemas";

                List<ModelClass> filteredQuestionsList = filterQuestionsByCategory(selectedCategoria);

                Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
                intent.putExtra("questionsList", (ArrayList<ModelClass>) filteredQuestionsList);
                startActivity(intent);
            }
        });


        categoriaProgramacionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoria = "Programacion";

                List<ModelClass> filteredQuestionsList = filterQuestionsByCategory(selectedCategoria);

                Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
                intent.putExtra("categoria", selectedCategoria);
                startActivity(intent);
            }
        });

        categoriaSeguridadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoria = "Seguridad";

                List<ModelClass> filteredQuestionsList = filterQuestionsByCategory(selectedCategoria);

                Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
                intent.putExtra("categoria", selectedCategoria);
                startActivity(intent);
            }
        });
    }

    private List<ModelClass> filterQuestionsByCategory(String category) {
        List<ModelClass> allQuestionsList = SplashActivity.getList();
        List<ModelClass> filteredQuestionsList = new ArrayList<>();

        for (ModelClass question : allQuestionsList) {
            for (String categoria : question.categorias) {
                if (categoria.equals(category)) {
                    filteredQuestionsList.add(question);
                    break;
                }
            }
        }

        return filteredQuestionsList;
    }

    // Método para iniciar SplashActivity con la categoría seleccionada
    private void startSplashActivity(String selectedCategoria) {
        Intent intent = new Intent(CategoryActivity.this, SplashActivity.class);
        intent.putExtra("categoria", selectedCategoria);
        startActivity(intent);
    }
}
