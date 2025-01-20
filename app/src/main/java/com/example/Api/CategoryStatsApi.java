package com.example.Api;


import com.example.Entity.CategoryStats;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryStatsApi {

    // Endpoint para obtener estadísticas por categoría
    @GET("quiz-users/user/{userId}/statistics")
    Call<List<CategoryStats>> getCategoryStatistics(@Path("userId") Long userId);
}
