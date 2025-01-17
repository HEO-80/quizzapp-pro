package com.example.quizzapp;

import android.app.ProgressDialog;
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

    Button categoriaLeyesBtn, categoriaProgramacionBtn, categoriaSistemasBtn, categoriaSeguridadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Inicializar los botones
        categoriaLeyesBtn = findViewById(R.id.categoriaLeyesBtn);
        categoriaSistemasBtn = findViewById(R.id.categoriaSistemasBtn);
        categoriaProgramacionBtn = findViewById(R.id.categoriaProgramacionBtn);
        categoriaSeguridadBtn = findViewById(R.id.categoriaSeguridadBtn);

        // Asignar listeners a los botones
        categoriaLeyesBtn.setOnClickListener(v -> fetchQuestionsByCategory("Leyes"));
        categoriaSistemasBtn.setOnClickListener(v -> fetchQuestionsByCategory("Sistemas"));
        categoriaProgramacionBtn.setOnClickListener(v -> fetchQuestionsByCategory("Programacion"));
        categoriaSeguridadBtn.setOnClickListener(v -> fetchQuestionsByCategory("Seguridad"));
    }

    /**
     * Método para obtener preguntas por categoría desde la API.
     * @param category Categoría seleccionada por el usuario.
     */
    private void fetchQuestionsByCategory(String category) {
        // Mostrar un progress bar (opcional)
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando preguntas...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        QuestionApi questionApi = ApiClient.getRetrofitInstance(CategoryActivity.this)
                                            .create(QuestionApi.class);

        questionApi.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                progressDialog.dismiss(); // Cerrar el progress bar

                if (response.isSuccessful() && response.body() != null) {
                    List<Question> filteredQuestions = filterQuestionsByCategory(response.body(), category);

                    if (filteredQuestions.isEmpty()) {
                        Toast.makeText(CategoryActivity.this, "No hay preguntas para esta categoría.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
                    intent.putExtra("questionsList", new ArrayList<>(filteredQuestions));
                    intent.putExtra("category", category); // Pasar la categoría seleccionada
                    startActivity(intent);
                } else {
                    Toast.makeText(CategoryActivity.this, "Error al obtener preguntas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                progressDialog.dismiss(); // Cerrar el progress bar
                Toast.makeText(CategoryActivity.this, "Error de conexión. Inténtalo más tarde.", Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // Registrar el error en logs
            }
        });
    }

    /**
     * Método para filtrar preguntas por categoría.
     * @param questions Lista completa de preguntas.
     * @param category Categoría a filtrar.
     * @return Lista de preguntas filtradas.
     */
    private List<Question> filterQuestionsByCategory(List<Question> questions, String category) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (question.getCategory().equalsIgnoreCase(category)) {
                filteredQuestions.add(question);
            }
        }
        return filteredQuestions;
    }
}
