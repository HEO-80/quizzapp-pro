package com.example.Api;

import com.example.Entity.QuizQuestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuizQuestionApi {

    @GET("quiz-questions")
    Call<List<QuizQuestion>> getAllQuizQuestions();

    @GET("quiz-questions/{id}")
    Call<QuizQuestion> getQuizQuestionById(@Path("id") Long id);

    @POST("quiz-questions")
    Call<QuizQuestion> createQuizQuestion(@Body QuizQuestion quizQuestion);

    @POST("quiz-questions")
    Call<QuizQuestion> createAllQuizQuestion(@Body QuizQuestion quizQuestion);



}

