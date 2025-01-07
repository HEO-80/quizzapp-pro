package com.example.Api;

import com.example.Entity.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Endpoint para obtener todos los usuarios (puede ser restringido)
    @GET("/api/users")
    Call<List<User>> getAllUsers();

    // Endpoint para obtener un usuario por ID
    @GET("/api/users/{id}")
    Call<User> getUserById(@Path("id") Long id);

    // Endpoint para crear un nuevo usuario (registro)
    @POST("/api/users")
    Call<User> createUser(@Body User user);

    // Endpoint para actualizar un usuario existente
    @PUT("/api/users/{id}")
    Call<User> updateUser(@Path("id") Long id, @Body User user);

    // Endpoint para eliminar un usuario
    @DELETE("/api/users/{id}")
    Call<ResponseBody> deleteUser(@Path("id") Long id);

    /**
     * Realiza la solicitud POST al endpoint de login.
     * @param user Contiene las credenciales del usuario.
     * @return Respuesta del servidor.
     */
    @POST("/api/login")
    Call<ResponseBody> login(@Body User user);


}
