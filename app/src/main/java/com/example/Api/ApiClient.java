package com.example.Api;

//import com.example.Utils.Constant;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class ApiClient {
//    private static final String BASE_URL = Constant.BASE_URL;
//    //private static final String BASE_URL = "http://192.168.x.x:8081/api/";
//
//    private static Retrofit retrofit;
//
//    private ApiClient() {
//        // Constructor privado para prevenir la creación de instancias
//    }
//    public static Retrofit getRetrofitInstance() {
//        if (retrofit == null) {
//
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new HttpLoggingInterceptor()
//                            .setLevel(HttpLoggingInterceptor.Level.BODY)) // Logs para depuración
//                    .build();
//
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client) // Añade el cliente HTTP con el interceptor
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//}

import static com.example.Utils.Constant.BASE_URL;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Configurar el interceptor de logs con un tag personalizado
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
                Log.d("RetrofitLogs", message);
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // Añade el interceptor
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

//    public static Retrofit getRetrofitInstance() {
//        if (retrofit == null) {
//            // Configurar el interceptor de logs
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new HttpLoggingInterceptor()
//                            .setLevel(HttpLoggingInterceptor.Level.BODY)) // Logs para depuración
//                    .build();
//
//            // Crear instancia de Retrofit
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client) // Añade el cliente HTTP con el interceptor
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
}
