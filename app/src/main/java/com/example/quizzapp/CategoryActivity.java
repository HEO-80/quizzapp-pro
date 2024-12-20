package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Api.ApiClient;
import com.example.Api.QuestionApi;
import com.example.Entity.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    Button categoriaLeyesBtn;
    Button categoriaProgramacionBtn;
    Button categoriaSistemasBtn;
    Button categoriaSeguridadBtn;

    private String selectedCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoriaLeyesBtn = findViewById(R.id.categoriaLeyesBtn);
        categoriaSistemasBtn = findViewById(R.id.categoriaSistemasBtn);
        categoriaProgramacionBtn = findViewById(R.id.categoriaProgramacionBtn);
        categoriaSeguridadBtn = findViewById(R.id.categoriaSeguridadBtn);

        categoriaLeyesBtn.setOnClickListener(v -> fetchQuestionsByCategory("Leyes"));
        categoriaSistemasBtn.setOnClickListener(v -> fetchQuestionsByCategory("Sistemas"));
        categoriaProgramacionBtn.setOnClickListener(v -> fetchQuestionsByCategory("Programacion"));
        categoriaSeguridadBtn.setOnClickListener(v -> fetchQuestionsByCategory("Seguridad"));
    }

    private void fetchQuestionsByCategory(String category) {
        QuestionApi questionApi = ApiClient.getRetrofitInstance().create(QuestionApi.class);

        questionApi.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> filteredQuestions = filterQuestionsByCategory(response.body(), category);

                    Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
//                    intent.putExtra("questionsList", (ArrayList<Question>) filteredQuestions);
                    intent.putExtra("questionsList", new ArrayList<>(filteredQuestions));

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "Error al obtener preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Question> filterQuestionsByCategory(List<Question> questions, String category) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (question.getCategory().equals(category)) {
                filteredQuestions.add(question);
            }
        }
        return filteredQuestions;
    }

    /**
     * Método para filtrar preguntas localmente (para pruebas locales).
     * Depende de la lista estática SplashActivity.getList().
     *
     * Este método está comentado para mantener compatibilidad con pruebas locales.
     * Puedes descomentar este código si necesitas trabajar sin conexión a la API.
     */
    /*
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
    */
}