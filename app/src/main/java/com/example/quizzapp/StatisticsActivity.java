package com.example.quizzapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Api.ApiClient;
import com.example.Api.QuizUserApi;
import com.example.Entity.CategoryStats;
import com.example.Entity.QuizUser;
import com.example.Utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsActivity extends AppCompatActivity {

    private TextView userNameDisplay, tvStatisticsTitle, tvUserTests, tvCategorySummary,tvTotalTests,tvPassedTests, tvFailedTests;
    private RecyclerView recyclerViewQuizzes, recyclerViewCategoryStats;
    private QuizzesAdapter quizzesAdapter;
    private CategoryStatsAdapter categoryStatsAdapter;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Inicializar vistas
        userNameDisplay = findViewById(R.id.user_name_display);
        tvStatisticsTitle = findViewById(R.id.tvStatisticsTitle);
        tvTotalTests = findViewById(R.id.tvTotalTests);
        tvPassedTests = findViewById(R.id.tvPassedTests);
        tvFailedTests = findViewById(R.id.tvFailedTests);
        tvUserTests = findViewById(R.id.tvUserTests);
        tvCategorySummary = findViewById(R.id.tvCategorySummary);
        recyclerViewQuizzes = findViewById(R.id.recyclerViewQuizzes);
        recyclerViewCategoryStats = findViewById(R.id.recyclerViewCategoryStats);

        // Inicializar SessionManager
        sessionManager = new SessionManager(this);

        // Mostrar usuario actual
        String currentUser = sessionManager.getUsername();
        userNameDisplay.setText("Usuario actual: " + currentUser);

        // Configurar RecyclerViews
        setupRecyclerViews();
//// Dentro del método onCreate
//        Button btnExit = findViewById(R.id.btn_exit);
//        btnExit.setOnClickListener(v -> finish());

        // Dentro del método onCreate
        TextView icExit = findViewById(R.id.ic_exit);
        icExit.setOnClickListener(v -> finish());

        // Configurar botón de salida en la barra de herramientas
//        Button btnExit = findViewById(R.id.btn_exit); // Asegúrate de usar btn_exit si usaste Button
        // Si usaste TextView, cambia a:
        // TextView icExit = findViewById(R.id.ic_exit);
//        btnExit.setOnClickListener(v -> finish());

        // Obtener y mostrar datos de estadísticas
        fetchUserQuizzes();
//        fetchCategoryStatistics();
    }

    private void setupRecyclerViews() {
        // Configurar RecyclerView de Quizzes
        recyclerViewQuizzes.setLayoutManager(new LinearLayoutManager(this));
        quizzesAdapter = new QuizzesAdapter(this, quiz -> {
            // Acción al hacer clic en un quiz
            Toast.makeText(this, "Quiz ID: " + quiz.getId(), Toast.LENGTH_SHORT).show();
            // Puedes implementar la apertura de detalles del quiz aquí
        });
        recyclerViewQuizzes.setAdapter(quizzesAdapter);

        // Configurar RecyclerView de Estadísticas por Categoría
        recyclerViewCategoryStats.setLayoutManager(new LinearLayoutManager(this));
        categoryStatsAdapter = new CategoryStatsAdapter();
        recyclerViewCategoryStats.setAdapter(categoryStatsAdapter);
    }

    private void fetchUserQuizzes(){
        Long userId = sessionManager.getUserId();
        if(userId == -1){
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        QuizUserApi quizUserApi = ApiClient.getRetrofitInstance(StatisticsActivity.this).create(QuizUserApi.class);
        Call<List<QuizUser>> call = quizUserApi.getUserQuizzes(userId);
        call.enqueue(new Callback<List<QuizUser>>() {
            @Override
            public void onResponse(Call<List<QuizUser>> call, Response<List<QuizUser>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<QuizUser> quizzes = response.body();
                    quizzesAdapter.setQuizzes(quizzes);
                    // 1) Cálculo de estadísticas generales (pasados/fallados)
                    calculateAndDisplayStatistics(quizzes);
                    // 2) Cálculo de estadísticas por categoría en la app
                    List<CategoryStats> stats = calculateCategoryStatsLocally(quizzes);
                    categoryStatsAdapter.setCategoryStats(stats);

                } else {
                    Toast.makeText(StatisticsActivity.this, "Error al obtener los quizzes", Toast.LENGTH_SHORT).show();
                    Log.e("StatisticsActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<QuizUser>> call, Throwable t) {
                Toast.makeText(StatisticsActivity.this, "Fallo de red al obtener los quizzes", Toast.LENGTH_SHORT).show();
                Log.e("StatisticsActivity", "Fallo: " + t.getMessage());
            }
        });
    }

    /**
     * Calcula y muestra las estadísticas generales del usuario.
     */
    private void calculateAndDisplayStatistics(List<QuizUser> quizzes) {
        int totalTests = quizzes.size();
        int passedTests = 0;
        int failedTests = 0;

        for(QuizUser quiz : quizzes){
            if(quiz.getScore() >= 10){
                passedTests++;
            } else {
                failedTests++;
            }
        }

        // Asignar los valores a los TextViews
        tvTotalTests.setText("Tests Realizados: " + totalTests);
        tvPassedTests.setText("Tests Aprobados: " + passedTests);
        tvFailedTests.setText("Tests Suspendidos: " + failedTests);
    }

    /**
     * Calcula las estadisticas por Category
     */
    private List<CategoryStats> calculateCategoryStatsLocally(List<QuizUser> quizzes) {
        // Mapa: categoría -> CategoryStats
        Map<String, CategoryStats> mapStats = new HashMap<>();

        for (QuizUser quiz : quizzes) {
            String category = quiz.getCategory();
            // Si no existe la categoría en el mapa, la creamos
            if (!mapStats.containsKey(category)) {
                CategoryStats catStat = new CategoryStats();
                catStat.setCategory(category);
                catStat.setCorrectAnswers(0);
                catStat.setIncorrectAnswers(0);
                mapStats.put(category, catStat);
            }

            // Aumentamos contadores según score
            if (quiz.getScore() != null && quiz.getScore() >= 10) {
                // correctAnswers++
                CategoryStats cs = mapStats.get(category);
                cs.setCorrectAnswers(cs.getCorrectAnswers() + 1);
            } else {
                // incorrectAnswers++
                CategoryStats cs = mapStats.get(category);
                cs.setIncorrectAnswers(cs.getIncorrectAnswers() + 1);
            }
        }

        return new ArrayList<>(mapStats.values());
    }
}
