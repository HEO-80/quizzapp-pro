package com.example.Api;

import com.example.Entity.QuizUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuizUserApi {

    @GET("quiz-users")
    Call<List<QuizUser>> getAllQuizUsers();

    @GET("quiz-users/{id}")
    Call<QuizUser> getQuizUserById(@Path("id") Long id);

    @GET("quiz-users/user/{userId}") // todos los quiz realizados por el usuario
    Call<List<QuizUser>> getUserQuizzes(@Path("userId") Long userId);

    @POST("quiz-users")
    Call<QuizUser> createQuizUser(@Body QuizUser quizUser);


}

