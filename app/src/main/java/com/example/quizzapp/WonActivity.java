package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Api.ApiClient;
import com.example.Api.QuizQuestionApi;
import com.example.Api.QuizUserApi;
import com.example.Api.QuizUserCallback;
import com.example.Api.UserAnswerApi;
import com.example.Entity.QuizQuestion;
import com.example.Entity.QuizUser;
import com.example.Entity.UserAnswer;
import com.example.Utils.SessionManager;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase WonActivity: Maneja la lógica al finalizar un test.
 */
public class WonActivity extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView resultText;
    ImageView resultImage;
    int correct, wrong;
    LinearLayout btnShare, btnReset, btnInicial;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        // Inicialización de vistas y sesión
        sessionManager = new SessionManager(this);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        resultText = findViewById(R.id.resultText);
        resultImage = findViewById(R.id.resultImage);
        btnShare = findViewById(R.id.bntShare);
        btnReset = findViewById(R.id.btnReset);
        btnInicial = findViewById(R.id.btnInicio);

        // Obtener respuestas correctas e incorrectas
        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);

        Log.d("WonActivity", "Correctas: " + correct + ", Incorrectas: " + wrong);

        // Configurar la barra de progreso y el texto de resultados
        circularProgressBar.setProgress(correct);
        resultText.setText(correct + "/20");

        // Configurar la imagen según el resultado
        if (correct == 20) {
            resultImage.setImageResource(R.drawable.perfect);
        } else if (correct > 9) {
            resultImage.setImageResource(R.drawable.youwin);
        } else {
            resultImage.setImageResource(R.drawable.youlose);
        }

        // Manejar resultados solo si no es un invitado
        if (!sessionManager.isGuest()) {
            handleQuizResults();
        } else {
            Toast.makeText(this, "Los datos no se guardan para usuarios invitados.", Toast.LENGTH_SHORT).show();
        }

        // Botón para compartir resultados
        btnShare.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage += "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Botón para reiniciar el cuestionario
        btnReset.setOnClickListener(v -> {
            Intent intent = new Intent(WonActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Botón para volver al inicio
        if (btnInicial != null) {
            btnInicial.setOnClickListener(v -> {
                Intent intent = new Intent(WonActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("WonActivity", "btnInicial is null. Check the XML ID.");
        }
    }

    /**
     * Maneja los resultados del cuestionario.
     */
    private void handleQuizResults() {
        Long userId = sessionManager.getUserId();

        // Verificar si el usuario es invitado
        if (sessionManager.isGuest()) {
            Log.d("WonActivity", "El usuario es invitado, no se guardan datos.");
            Toast.makeText(this, "Los datos no se guardan para usuarios invitados.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el ID del usuario
        if (userId == null || userId <= 0) {
            Log.e("WonActivity", "ID de usuario inválido: " + userId);
            Toast.makeText(this, "Error: Usuario no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la categoría
        String category = getIntent().getStringExtra("category");
        category = category != null ? category : "General";

        // Guardar el cuestionario y manejar las respuestas después
        submitQuizUser(userId, category, correct, new QuizUserCallback() {
            @Override
            public void onSuccess(Long quizId) {
                // Enviar las respuestas del usuario usando el quizId retornado
                ArrayList<QuizQuestion> userAnswers = (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("userAnswers");
                if (userAnswers != null && !userAnswers.isEmpty()) {
                    submitQuizQuestions(quizId, userId, userAnswers);
                } else {
                    Toast.makeText(WonActivity.this, "No hay respuestas registradas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("WonActivity", "Error al guardar el cuestionario: " + errorMessage);
                Toast.makeText(WonActivity.this, "Error al guardar el cuestionario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitQuizUser(Long userId, String category, int score, QuizUserCallback callback) {
        // Crear el objeto QuizUser
        QuizUser quizUser = new QuizUser();
        quizUser.setUserId(userId);
        quizUser.setCategory(category);
        quizUser.setScore(score);
        quizUser.setStartTime("2024-12-05T11:00:00"); // Simula un inicio
        quizUser.setEndTime("2024-12-05T11:30:00");   // Simula un final

        // Llamar al API
        QuizUserApi quizUserApi = ApiClient.getRetrofitInstance().create(QuizUserApi.class);
        quizUserApi.createQuizUser(quizUser).enqueue(new Callback<QuizUser>() {
            @Override
            public void onResponse(Call<QuizUser> call, Response<QuizUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Long quizId = response.body().getId();
                    Log.d("submitQuizUser", "QuizUser guardado correctamente con ID: " + quizId);
                    callback.onSuccess(quizId);
                } else {
                    callback.onFailure("Error en la respuesta del servidor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<QuizUser> call, Throwable t) {
                callback.onFailure("Error de red: " + t.getMessage());
            }
        });
    }


    /**
     * Envía los datos del usuario al backend.
     */
//    private void submitQuizUser(Long userId, String category, int score) {
//        // Crear las fechas de inicio y fin
//        LocalDateTime startTime = LocalDateTime.now(); // Fecha y hora actuales
//        LocalDateTime endTime = startTime.plusMinutes(30); // Simular 30 minutos después
//
//        // Formatear las fechas
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        String startTimeFormatted = startTime.format(formatter);
//        String endTimeFormatted = endTime.format(formatter);
//
//        // Crear el objeto QuizUser con los datos
//        QuizUser quizUser = new QuizUser(userId, category, startTimeFormatted, endTimeFormatted, score);
//
//        Log.d("submitQuizUser", "Enviando datos del usuario: " + quizUser);
//
//        QuizUserApi quizUserApi = ApiClient.getRetrofitInstance().create(QuizUserApi.class);
//        quizUserApi.createQuizUser(quizUser).enqueue(new Callback<QuizUser>() {
//            @Override
//            public void onResponse(Call<QuizUser> call, Response<QuizUser> response) {
//                if (response.isSuccessful()) {
//                    Log.d("submitQuizUser", "Datos guardados correctamente.");
//                } else {
//                    Log.e("submitQuizUser", "Error al guardar datos: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<QuizUser> call, Throwable t) {
//                Log.e("submitQuizUser", "Error de red: " + t.getMessage());
//            }
//        });
//    }

    /**
     * Envía cada respuesta al backend.
     */
//    private void submitQuizQuestion(Long quizId, Long questionId, Long userId, String userAnswer, boolean isCorrect) {
//        QuizQuestion quizQuestion = new QuizQuestion(quizId, questionId, userId, userAnswer, isCorrect);
//        Log.d("submitQuizQuestion", "Enviando respuesta: " + quizQuestion);
//
//        // Llamar al API para guardar los datos
//        QuizQuestionApi quizQuestionApi = ApiClient.getRetrofitInstance().create(QuizQuestionApi.class);
//        quizQuestionApi.createQuizQuestion(quizQuestion).enqueue(new Callback<QuizQuestion>() {
//            @Override
//            public void onResponse(Call<QuizQuestion> call, Response<QuizQuestion> response) {
//                if (response.isSuccessful()) {
//                    Log.d("submitQuizQuestion", "Respuesta registrada correctamente.");
//                } else {
//                    Log.e("submitQuizQuestion", "Error al registrar respuesta: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<QuizQuestion> call, Throwable t) {
//                Log.e("submitQuizQuestion", "Error de red: " + t.getMessage());
//            }
//        });
//    }

    private void submitQuizQuestions(Long quizId, Long userId, List<QuizQuestion> userAnswers) {
        for (QuizQuestion answer : userAnswers) {
            answer.setQuizId(quizId);
            answer.setUserId(userId);

            QuizQuestionApi quizQuestionApi = ApiClient.getRetrofitInstance().create(QuizQuestionApi.class);
            quizQuestionApi.createQuizQuestion(answer).enqueue(new Callback<QuizQuestion>() {
                @Override
                public void onResponse(Call<QuizQuestion> call, Response<QuizQuestion> response) {
                    if (response.isSuccessful()) {
                        Log.d("submitQuizQuestions", "Respuesta guardada correctamente.");
                    } else {
                        Log.e("submitQuizQuestions", "Error al guardar respuesta: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<QuizQuestion> call, Throwable t) {
                    Log.e("submitQuizQuestions", "Error de red: " + t.getMessage());
                }
            });
        }
    }

    /**
     * Envía respuestas al backend, ya sea una sola respuesta o una lista de respuestas.
     */
//    private void submitQuizQuestion(Long quizId, Long userId, List<QuizQuestion> quizQuestions) {
//        QuizQuestionApi quizQuestionApi = ApiClient.getRetrofitInstance().create(QuizQuestionApi.class);
//
//        for (QuizQuestion question : quizQuestions) {
//            // Asociar el quizId y userId a cada pregunta antes de enviarla
//            question.setQuizId(quizId);
//            question.setUserId(userId);
//
//            quizQuestionApi.createQuizQuestion(question).enqueue(new Callback<QuizQuestion>() {
//                @Override
//                public void onResponse(Call<QuizQuestion> call, Response<QuizQuestion> response) {
//                    if (response.isSuccessful()) {
//                        Log.d("submitQuizQuestions", "Pregunta guardada correctamente: " + question.getQuestionId());
//                    } else {
//                        Log.e("submitQuizQuestions", "Error al guardar pregunta: " + response.message());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<QuizQuestion> call, Throwable t) {
//                    Log.e("submitQuizQuestions", "Error de red: " + t.getMessage());
//                }
//            });
//        }
//    }

    private void submitUserAnswers(Long quizId, Long userId) {
        ArrayList<UserAnswer> userAnswers = getUserAnswers(); // Método que obtiene la lista de respuestas

        UserAnswerApi userAnswerApi = ApiClient.getRetrofitInstance().create(UserAnswerApi.class);

        for (UserAnswer answer : userAnswers) {
            answer.setQuizId(quizId); // Asociar el quizId a cada respuesta
            answer.setUserId(userId);

            userAnswerApi.createUserAnswer(answer).enqueue(new Callback<UserAnswer>() {
                @Override
                public void onResponse(Call<UserAnswer> call, Response<UserAnswer> response) {
                    if (response.isSuccessful()) {
                        Log.d("submitUserAnswers", "Respuesta guardada correctamente: " + answer.getQuestionId());
                    } else {
                        Log.e("submitUserAnswers", "Error al guardar respuesta: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<UserAnswer> call, Throwable t) {
                    Log.e("submitUserAnswers", "Error de red: " + t.getMessage());
                }
            });
        }
    }

    private ArrayList<QuizQuestion> getQuizQuestions() {
        return (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("quizQuestions");
    }

    private ArrayList<UserAnswer> getUserAnswers() {
        return (ArrayList<UserAnswer>) getIntent().getSerializableExtra("userAnswers");
    }

}
