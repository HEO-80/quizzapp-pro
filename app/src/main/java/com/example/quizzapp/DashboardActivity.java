package com.example.quizzapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.Api.ApiClient;
import com.example.Api.UserAnswerApi;
import com.example.Entity.UserAnswer;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * La actividad DashboardActivity es la pantalla principal del juego de preguntas y respuestas.
 * Muestra las preguntas y las opciones de respuesta, y lleva la cuenta de las respuestas correctas e incorrectas.
 */

public class DashboardActivity extends AppCompatActivity {

    // Variables de instancia
    CountDownTimer countDownTimer;
    int timerValue = 20;
    private ModelClass modelClass;
    ArrayList<ModelClass> allQuestionsList;
    int index = 0;
    LinearLayout nextBtn_question;
    TextView card_question, optionA, optionB, optionC, optionD;
    CardView cardOA, cardOB, cardOC, cardOD;
    int correctCount = 0;
    int wrongCount = 0;
    private TextView questionNumber;
    // Limitar el número de preguntas a 20
    private int questionLimit = 20;
    boolean isOptionSelected = false;

    /**
     * Método onCreate se llama cuando se crea la actividad.
     * Inicializa las vistas y carga las preguntas.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Hooks();

        allQuestionsList = SplashActivity.getList();
        Collections.shuffle(allQuestionsList);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("questionsList")) {
            List<ModelClass> filteredQuestionsList = (List<ModelClass>) intent.getSerializableExtra("questionsList");

            // Verifica si hay preguntas filtradas disponibles
            if (filteredQuestionsList.size() > 0) {
                modelClass = filteredQuestionsList.get(index);

                nextBtn_question.setClickable(false);

                setAllData();
            } else {
                Toast.makeText(DashboardActivity.this, "No hay preguntas disponibles para esta categoría.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // No se proporcionó ninguna lista de preguntas filtradas
            Toast.makeText(DashboardActivity.this, "No se seleccionó ninguna categoría.", Toast.LENGTH_SHORT).show();
        }

        TextView ic_exit = findViewById(R.id.ic_exit);

        ic_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue--;
            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(DashboardActivity.this, R.style.Dialog);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setContentView(R.layout.time_out_dialog);

                nextBtn_question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isOptionSelected) {
                            isOptionSelected = false;

                            if (index < allQuestionsList.size() - 1) {
                                index++;
                                modelClass = allQuestionsList.get(index);
                                setAllData();
                                resetColor();
                                enableButton();
                                nextBtn_question.setClickable(false);
                            } else {
                                GameWon();
                            }
                        }
                    }
                });
                dialog.show(); // Mostrar el diálogo
            }
        }.start();

        enableButton();
        cardOA.setEnabled(false);
        cardOB.setEnabled(false);
        cardOC.setEnabled(false);
        cardOD.setEnabled(false);
    }


    /**
     * Establece los datos de las preguntas y respuestas en las vistas.
     */
    private void setAllData() {
        updateQuestionNumber();
        card_question.setText(modelClass.getQuestion());
        optionA.setText(modelClass.getcA());
        optionB.setText(modelClass.getcB());
        optionC.setText(modelClass.getcC());
        optionD.setText(modelClass.getcD());
    }

    /**
     * Inicializa las vistas con sus identificadores de recursos.
     */
    private void Hooks() {

        card_question = findViewById(R.id.card_question);
        optionA = findViewById(R.id.card_optionA);
        optionB = findViewById(R.id.card_optionB);
        optionC = findViewById(R.id.card_optionC);
        optionD = findViewById(R.id.card_optionD);

        cardOA = findViewById(R.id.cardA);
        cardOB = findViewById(R.id.cardB);
        cardOC = findViewById(R.id.cardC);
        cardOD = findViewById(R.id.cardD);

        nextBtn_question = findViewById(R.id.nextBtn_question);
        questionNumber = findViewById(R.id.question_number);
    }

    private void updateQuestionNumber() {
        questionNumber.setText(String.format("%d/%d", index + 1, questionLimit));
    }
    /**
     * Marca la opción seleccionada como correcta y actualiza el contador de respuestas correctas.
     *
     * @param cardView La tarjeta que contiene la opción seleccionada.
     */
    public void Correct(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.green));

        nextBtn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctCount++;
                index++;
                if (index < questionLimit) {
                    modelClass = allQuestionsList.get(index);
                    resetColor();
                    setAllData();
                    isOptionSelected = false;
                    enableButton();
                } else {
                    GameWon();
                }
            }
        });
    }

    /**
     * Marca la opción seleccionada como incorrecta y actualiza el contador de respuestas incorrectas.
     *
     * @param cardView La tarjeta que contiene la opción seleccionada.
     */
    public void Wrong(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.red));

        nextBtn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrongCount++;
                index++;
                if (index < questionLimit) {
                    modelClass = allQuestionsList.get(index);
                    setAllData();
                    resetColor();
                    isOptionSelected = false;
                    enableButton();
                } else {
                    GameWon();
                }
            }
        });
    }

    /**
     * Navega a la actividad WonActivity al finalizar el juego.
     */
    private void GameWon() {
        Intent intent = new Intent(DashboardActivity.this, WonActivity.class);
        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);

        startActivity(intent);
    }

    /**
     * Habilita las tarjetas de opciones para que sean clicables.
     */
    public void enableButton() {
        cardOA.setClickable(true);
        cardOB.setClickable(true);
        cardOC.setClickable(true);
        cardOD.setClickable(true);
    }

    /**
     * Deshabilita las tarjetas de opciones para que no sean clicables.
     */
    public void disableButton() {
        cardOA.setClickable(false);
        cardOB.setClickable(false);
        cardOC.setClickable(false);
        cardOD.setClickable(false);
    }

    /**
     * Restablece el color de fondo de las tarjetas de opciones a blanco.
     */
    public void resetColor() {
        cardOA.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOB.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOC.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOD.setCardBackgroundColor(getResources().getColor(R.color.white));
    }

    // Métodos para manejar los clics en las opciones A, B, C y D.
    public void OptionAclick(View view) {
        disableButton();
        nextBtn_question.setClickable(true);

        if (!isOptionSelected) {
            isOptionSelected = true;
            if (modelClass.getcA().equals(modelClass.getAns())) {
                Correct(cardOA);
            } else {
                Wrong(cardOA);
            }
        }
    }

    public void OptionBclick(View view) {
        disableButton();
        nextBtn_question.setClickable(true);

        if (!isOptionSelected) {
            isOptionSelected = true;
            if (modelClass.getcB().equals(modelClass.getAns())) {
                Correct(cardOB);
            } else {
                Wrong(cardOB);
            }
        }
    }

    public void OptionCclick(View view) {
        disableButton();
        nextBtn_question.setClickable(true);

        if (!isOptionSelected) {
            isOptionSelected = true;
            if (modelClass.getcC().equals(modelClass.getAns())) {
                Correct(cardOC);
            } else {
                Wrong(cardOC);
            }
        }
    }

    public void OptionDclick(View view) {
        disableButton();
        nextBtn_question.setClickable(true);

        if (!isOptionSelected) {
            isOptionSelected = true;
            if (modelClass.getcD().equals(modelClass.getAns())) {
                Correct(cardOD);
            } else {
                Wrong(cardOD);
            }
        }
    }

    private void submitAnswer(Long quizId, Long questionId, String userAnswer, boolean isCorrect) {
        UserAnswerApi userAnswerApi = ApiClient.getRetrofitInstance().create(UserAnswerApi.class);

        // Suponiendo que tienes el ID del usuario autenticado
        Long userId = getCurrentUserId(); // Implementa este método para obtener el ID del usuario actual
        UserAnswer answer = new UserAnswer(quizId, questionId, userId, userAnswer, isCorrect);

        userAnswerApi.createUserAnswer(answer).enqueue(new Callback<UserAnswer>() {
            @Override
            public void onResponse(Call<UserAnswer> call, Response<UserAnswer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DashboardActivity.this, "Respuesta registrada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Error al registrar la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAnswer> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error de red al registrar la respuesta", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
