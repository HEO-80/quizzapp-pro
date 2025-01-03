package com.example.Api;

import com.example.Entity.Question;
import com.example.Entity.UserAnswer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAnswerApi {

    @GET("user-answers")
    Call<List<UserAnswer>> getAllUserAnswers();


    @GET("user-answers/{id}")
    Call<Question> getQuestionById(@Path("id") Long id);


    // Crear una nueva respuesta de usuario
    @POST("user-answers")
    Call<UserAnswer> createUserAnswer(@Body UserAnswer userAnswer);

    // Actualizar una respuesta de usuario existente
    @PUT("user-answers/{id}")
    Call<UserAnswer> updateUserAnswer(@Path("id") Long id, @Body UserAnswer userAnswer);

    // Eliminar una respuesta de usuario por ID
    @DELETE("user-answers/{id}")
    Call<Void> deleteUserAnswer(@Path("id") Long id);

}
