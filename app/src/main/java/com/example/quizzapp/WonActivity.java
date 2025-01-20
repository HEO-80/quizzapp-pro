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
import com.google.gson.Gson;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    // Recibir las horas
    String startTimeString;
    String endTimeString;
    private ArrayList<UserAnswer> userAnswers; // Variable de instancia
    private ArrayList<QuizQuestion> quizQuestions;
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

        quizQuestions = (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("quizQuestions");

        // Ahora la guardas en la variable de clase
        userAnswers = (ArrayList<UserAnswer>) getIntent().getSerializableExtra("userAnswers");

        // En este momento, la Activity ya está creada y getIntent() no será null
        startTimeString = getIntent().getStringExtra("startTime");
        endTimeString   = getIntent().getStringExtra("endTime");

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
            handleQuizResults(startTimeString, endTimeString);
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
    private void handleQuizResults(String startTimeStr, String endTimeStr) {
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
        submitQuizUser(userId, category, correct, startTimeString,
                endTimeString, new QuizUserCallback() {
            @Override
            public void onSuccess(Long quizId) {

                // 1. Al obtener el quizId, ya podemos invocar a submitQuizQuestions y submitUserAnswers.
                //    Primero, recuperamos las listas con su clave correcta:

                // Suponiendo que la lista de QUIZQUESTION se guardó con la clave "quizQuestions"
                ArrayList<QuizQuestion> quizQuestionsList  = (ArrayList<QuizQuestion>)
                        getIntent().getSerializableExtra("quizQuestions");

                if (quizQuestionsList  != null && !quizQuestionsList.isEmpty()) {
                    submitQuizQuestions(quizId, userId, quizQuestionsList);
                } else {
                    Log.d("WonActivity", "No hay quizQuestions para enviar.");
                }

                // Suponiendo que la lista de USERANSWER se guardó con la clave "userAnswers"
                ArrayList<UserAnswer> userAnswersList = (ArrayList<UserAnswer>)
                        getIntent().getSerializableExtra("userAnswers");

                if (userAnswersList != null && !userAnswersList.isEmpty()) {
                    submitUserAnswers(quizId, userId,userAnswersList);
                } else {
                    Log.d("WonActivity", "No hay userAnswers para enviar.");
                }

                // Enviar las respuestas del usuario usando el quizId retornado
//                ArrayList<QuizQuestion> userAnswers = (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("userAnswers");
//                if (userAnswers != null && !userAnswers.isEmpty()) {
//                    submitQuizQuestions(quizId, userId, userAnswers);
//                } else {
//                    Toast.makeText(WonActivity.this, "No hay respuestas registradas.", Toast.LENGTH_SHORT).show();
//                }
//                submitUserAnswers(quizId, userId);

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("WonActivity", "Error al guardar el cuestionario: " + errorMessage);
                Toast.makeText(WonActivity.this, "Error al guardar el cuestionario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitQuizUser(Long userId, String category, int score,
                                String startTimeFormatted, String endTimeFormatted,
                                QuizUserCallback callback) {

        // Crear las fechas de inicio y fin
//        LocalDateTime startTime = LocalDateTime.now();
//        LocalDateTime endTime = LocalDateTime.now(); // Simula un final 30 minutos después
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);
        // Formatear las fechas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        String startTimeFormatted = now.format(formatter);
//        String endTimeFormatted = now.format(formatter);
//        String startTimeFormatted = startTime.format(formatter);
//        String endTimeFormatted = endTime.format(formatter);

        // Crear el objeto QuizUser
        QuizUser quizUser = new QuizUser();
        quizUser.setUserId(userId);
        quizUser.setCategory(category);
        quizUser.setScore(score);
        quizUser.setStartTime(startTimeFormatted);
        quizUser.setEndTime(endTimeFormatted);

        // Llamar al API
        QuizUserApi quizUserApi = ApiClient.getRetrofitInstance(WonActivity.this)
                .create(QuizUserApi.class);
        quizUserApi.createQuizUser(quizUser).enqueue(new Callback<QuizUser>() {
            @Override
            public void onResponse(Call<QuizUser> call, Response<QuizUser> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Long quizId = response.body().getId();
                    Log.d("submitQuizUser", "QuizUser guardado correctamente con ID: " + quizId);

                    // AÑADE ESTE LOG:
//                    Log.d("submitQuizUser", "LLAMANDO a submitQuizQuestions(...) con quizId=" + quizId);

                    // Llama al método para guardar las quizQuestions
                    // (Revisa que `userAnswers` no esté vacío.)
//                    submitQuizQuestions(quizId, userId, userAnswers);
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



    private void submitQuizQuestions(Long quizId, Long userId, List<QuizQuestion> quizQuestions) {
        QuizQuestionApi quizQuestionApi = ApiClient.getRetrofitInstance(WonActivity.this)
                .create(QuizQuestionApi.class);
        Gson gson = new Gson(); // o usa la instancia que tengas

        for (QuizQuestion question : quizQuestions) {
            question.setQuizId(quizId);
            question.setUserId(userId);

            // Muestra un log en JSON para ver EXACTAMENTE qué mandas:
            String json = gson.toJson(question);
            Log.d("submitQuizQuestions", "Enviando datos JSON: " + json);
            // Log detallado
            String jsonBody = gson.toJson(question);
            Log.d("submitQuizQuestions", "Enviando datos a /api/quiz-questions: " + jsonBody);

//            Log.d("submitQuizQuestions", "Enviando datos: " + answer);
//            QuizQuestionApi quizQuestionApi = ApiClient.getRetrofitInstance().create(QuizQuestionApi.class);
            quizQuestionApi.createQuizQuestion(question).enqueue(new Callback<QuizQuestion>() {
                @Override
                public void onResponse(Call<QuizQuestion> call, Response<QuizQuestion> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("submitQuizQuestions", "Respuesta guardada correctamente: " + question.getQuestionId());
                        Toast.makeText(WonActivity.this, "Respuesta guardada correctamente: " + question.getQuestionId(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("submitQuizQuestions", "Error al guardar respuesta: " + response.message());
                        Toast.makeText(WonActivity.this, "Error al guardar respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<QuizQuestion> call, Throwable t) {
                    Log.e("submitQuizQuestions", "Error de red: " + t.getMessage());
                    Toast.makeText(WonActivity.this, "Error de red al guardar respuesta.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    private void submitUserAnswers(Long quizId, Long userId, List<UserAnswer> answers) {
//        ArrayList<UserAnswer> answers = getUserAnswers(); // Método que obtiene la lista de respuestas
//        submitUserAnswers(quizId, userId, answers);
        Gson gson = new Gson(); // o usa la instancia que tengas
        if (answers == null || answers.isEmpty()) {
            Toast.makeText(this, "No hay respuestas para enviar.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserAnswerApi userAnswerApi = ApiClient.getRetrofitInstance(WonActivity.this)
                .create(UserAnswerApi.class);

        for (UserAnswer answer : userAnswers) {
            answer.setQuizId(quizId); // Asociar el quizId a cada respuesta
            answer.setUserId(userId);

            // Muestra un log en JSON para ver EXACTAMENTE qué mandas:
            String json = gson.toJson(answer);
            Log.d("submitUserAnswers", "Enviando datos JSON: " + json);
            // Log detallado
            String jsonBody = gson.toJson(answer);
            Log.d("submitUserAnswers", "Enviando datos a /api/user-answersArrayList<QuizQuestion> quizQuestionsList = (ArrayList<QuizQuestion>)\n" +
                    "            getIntent().getSerializableExtra(\"quizQuestions\");: " + jsonBody);

            userAnswerApi.createUserAnswer(answer).enqueue(new Callback<UserAnswer>() {
                @Override
                public void onResponse(Call<UserAnswer> call, Response<UserAnswer> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("submitUserAnswers", "Respuesta guardada correctamente: " + answer.getQuestionId());
                        Toast.makeText(WonActivity.this, "Respuesta correcta guardada: " + answer.getQuestionId(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("submitUserAnswers", "Error al guardar respuesta: " + response.message());
                        Toast.makeText(WonActivity.this, "Error al guardar respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserAnswer> call, Throwable t) {
                    Log.e("submitUserAnswers", "Error de red: " + t.getMessage());
                    Toast.makeText(WonActivity.this, "Error de red al guardar respuesta.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private ArrayList<QuizQuestion> getQuizQuestions() {
        return (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("quizQuestions");
        //return ArrayList<QuizQuestion> quizQuestions = (ArrayList<QuizQuestion>)
        //        getIntent().getSerializableExtra("quizQuestions");
    }

    private ArrayList<UserAnswer> getUserAnswers() {
        return (ArrayList<UserAnswer>) getIntent().getSerializableExtra("userAnswers");
    }

//    ArrayList<QuizQuestion> quizQuestions = (ArrayList<QuizQuestion>)
//            getIntent().getSerializableExtra("quizQuestions");
//    // Lista de userAnswers
//    ArrayList<UserAnswer> userAnswers = (ArrayList<UserAnswer>)
//            getIntent().getSerializableExtra("userAnswers");
}
