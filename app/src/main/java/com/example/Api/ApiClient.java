package com.example.Api;

import static com.example.Utils.Constant.BASE_URL;

import android.content.Context;
import android.util.Log;

import com.example.Utils.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            // Configurar el interceptor de logs con un tag personalizado
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
                Log.d("RetrofitLogs", message);
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Interceptor para añadir el "Authorization" header
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(chain -> {
                        // Obtenemos el token
                        SessionManager sessionManager = new SessionManager(context);
                        String token = sessionManager.getToken();

                        // Construir la request con el header Authorization
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        if (token != null && !token.isEmpty()) {
                            // Ejemplo: Authorization: Bearer <token>
                            builder.header("Authorization", "Bearer " + token);
                        }

                        // Construir request final
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    })
                    .build();

            // Crear instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Añade el cliente HTTP con el interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
