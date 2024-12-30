package com.example.Api;

import com.example.Entity.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    /**
     * Realiza la solicitud POST al endpoint de login.
     * @param user Contiene las credenciales del usuario.
     * @return Respuesta del servidor.
     */
    @POST("/api/login")
    Call<ResponseBody> login(@Body User user);
}
