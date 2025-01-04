package com.example.quizzapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Api.ApiClient;
import com.example.Api.CategoryStatsApi;
import com.example.Api.QuizUserApi;
import com.example.Entity.CategoryStats;
import com.example.Entity.QuizUser;
import com.example.Utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsActivity extends AppCompatActivity {

    private TextView userNameDisplay, tvStatisticsTitle, tvUserTests, tvCategorySummary;
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
        fetchCategoryStatistics();
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
        if(userId == null){
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        QuizUserApi quizUserApi = ApiClient.getRetrofitInstance().create(QuizUserApi.class);
        Call<List<QuizUser>> call = quizUserApi.getUserQuizzes(userId);
        call.enqueue(new Callback<List<QuizUser>>() {
            @Override
            public void onResponse(Call<List<QuizUser>> call, Response<List<QuizUser>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<QuizUser> quizzes = response.body();
                    quizzesAdapter.setQuizzes(quizzes);
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

    private void fetchCategoryStatistics(){
        Long userId = sessionManager.getUserId();
        if(userId == null){
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        CategoryStatsApi categoryStatsApi = ApiClient.getRetrofitInstance().create(CategoryStatsApi.class);
        Call<List<CategoryStats>> call = categoryStatsApi.getCategoryStatistics(userId);
        call.enqueue(new Callback<List<CategoryStats>>() {
            @Override
            public void onResponse(Call<List<CategoryStats>> call, Response<List<CategoryStats>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<CategoryStats> stats = response.body();
                    categoryStatsAdapter.setCategoryStats(stats);
                } else {
                    Toast.makeText(StatisticsActivity.this, "Error al obtener las estadísticas por categoría", Toast.LENGTH_SHORT).show();
                    Log.e("StatisticsActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryStats>> call, Throwable t) {
                Toast.makeText(StatisticsActivity.this, "Fallo de red al obtener las estadísticas por categoría", Toast.LENGTH_SHORT).show();
                Log.e("StatisticsActivity", "Fallo: " + t.getMessage());
            }
        });
    }
}
