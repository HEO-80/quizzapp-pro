package com.example.Api;

import com.example.Entity.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuestionApi {
    @GET("questions")
    Call<List<Question>> getAllQuestions();

    @GET("questions/{id}")
    Call<Question> getQuestionById(@Path("id") Long id);
}
