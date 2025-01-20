package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.Entity.Question;
import com.example.Entity.QuizQuestion;
import com.example.Entity.UserAnswer;
import com.example.Utils.SessionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<Question> allQuestionsList;
    private Question currentQuestion;
    private int index = 0;
    private int correctCount = 0;
    private int wrongCount = 0;

    private LinearLayout nextBtn_question;
    private TextView card_question, optionA, optionB, optionC, optionD, questionNumber;
    private CardView cardOA, cardOB, cardOC, cardOD;

    private LocalDateTime quizStartTime;  // <--- Para guardar la hora de inicio

    private final int questionLimit = 20;
    private boolean isOptionSelected = false;

    // Dentro de DashboardActivity
    private List<QuizQuestion> quizQuestions = new ArrayList<>(); // Lista para almacenar las respuestas
    private List<UserAnswer> userAnswers = new ArrayList<>();
    private String selectedCategory = "General"; // Define una categoría predeterminada

    TextView userNameDisplay;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Hooks();

        sessionManager = new SessionManager(this);

        // Mostrar usuario actual
        userNameDisplay = findViewById(R.id.user_name_display);
        userNameDisplay.setText("Usuario: " + sessionManager.getUsername());

        // Validación de preguntas recibidas
        Intent intent = getIntent();
        selectedCategory = intent.getStringExtra("category");
        if (selectedCategory == null) {
            selectedCategory = "General"; // Valor predeterminado
        }

        if (intent != null && intent.hasExtra("questionsList")) {
            // Obtener la lista de preguntas filtradas por categoría
            ArrayList<Question> questionsList = (ArrayList<Question>) intent.getSerializableExtra("questionsList");

            if (questionsList != null && !questionsList.isEmpty()) {
                allQuestionsList = questionsList;
                Log.d("DashboardActivity", "Preguntas recibidas: " + allQuestionsList.size());

                System.out.println("preguntas recibidas de dhasboardactivity");

                ensureEnoughQuestions(); // Asegurar suficientes preguntas
                Collections.shuffle(allQuestionsList); // Mezclar preguntas
                currentQuestion = allQuestionsList.get(index);
                setAllData(); // Configurar la primera pregunta

                // Registramos la hora de inicio del test
                quizStartTime = LocalDateTime.now();
                Log.d("DashboardActivity", "Hora de inicio del test: " + quizStartTime.toString());

            } else {
                Toast.makeText(this, "No hay preguntas disponibles para esta categoría.", Toast.LENGTH_SHORT).show();
                finish(); // Salir si no hay preguntas
            }
        } else {
            Toast.makeText(this, "Error al cargar preguntas.", Toast.LENGTH_SHORT).show();
            finish(); // Salir si no se reciben preguntas
        }

        nextBtn_question.setOnClickListener(view -> navigateToNextQuestion());
    }


    private void Hooks() {
        card_question = findViewById(R.id.card_question_text); // TextView dentro del CardView
        optionA = findViewById(R.id.card_optionA); // TextView
        optionB = findViewById(R.id.card_optionB); // TextView
        optionC = findViewById(R.id.card_optionC); // TextView
        optionD = findViewById(R.id.card_optionD); // TextView

        cardOA = findViewById(R.id.cardA); // CardView
        cardOB = findViewById(R.id.cardB); // CardView
        cardOC = findViewById(R.id.cardC); // CardView
        cardOD = findViewById(R.id.cardD); // CardView

        nextBtn_question = findViewById(R.id.nextBtn_question); // LinearLayout
        questionNumber = findViewById(R.id.question_number); // TextView
    }


    private void setAllData() {
        shuffleOptions(currentQuestion); // Mezclar las opciones antes de mostrarlas

        questionNumber.setText(String.format("%d/%d", index + 1, questionLimit));
        card_question.setText(currentQuestion.getQuestionText());
        optionA.setText(currentQuestion.getOption1());
        optionB.setText(currentQuestion.getOption2());
        optionC.setText(currentQuestion.getOption3());
        optionD.setText(currentQuestion.getOption4());
    }

    private void navigateToNextQuestion() {
        if (++index < questionLimit && index < allQuestionsList.size()) {
            currentQuestion = allQuestionsList.get(index);
            resetColor();
            setAllData();
            isOptionSelected = false;
        } else {
            finishQuiz();
//            GameWon();
        }
    }

    private void ensureEnoughQuestions() {
        int originalSize = allQuestionsList.size();

        // Si hay menos preguntas que el límite, duplicarlas hasta llegar al límite
        while (allQuestionsList.size() < questionLimit) {
            for (int i = 0; i < originalSize && allQuestionsList.size() < questionLimit; i++) {
                allQuestionsList.add(allQuestionsList.get(i)); // Añadir duplicados
            }
        }
    }

    // Al finalizar el test, pasa las respuestas a WonActivity
    private void finishQuiz() {
        // 1. Tomar la hora de fin
        LocalDateTime quizEndTime = LocalDateTime.now();

        // 2. Formatear las fechas (si deseas enviarlas como String)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startTimeFormatted = quizStartTime.format(formatter);
        String endTimeFormatted = quizEndTime.format(formatter);

        // 3. Pasar esos Strings a WonActivity
        Intent intent = new Intent(DashboardActivity.this, WonActivity.class);
        intent.putExtra("startTime", startTimeFormatted);
        intent.putExtra("endTime", endTimeFormatted);

        // Además, envía la lista de respuestas, correctas e incorrectas, categoría...

        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);
        intent.putExtra("category", selectedCategory);

        intent.putExtra("quizQuestions", new ArrayList<>(quizQuestions));

        intent.putExtra("userAnswers", new ArrayList<>(userAnswers));

        startActivity(intent);
        finish();
    }



    private void shuffleOptions(Question question) {
        List<String> options = new ArrayList<>();
        options.add(question.getOption1());
        options.add(question.getOption2());
        options.add(question.getOption3());
        options.add(question.getOption4());

        Collections.shuffle(options);

        question.setOption1(options.get(0));
        question.setOption2(options.get(1));
        question.setOption3(options.get(2));
        question.setOption4(options.get(3));
    }




    public void Correct(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
    }

    public void Wrong(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
    }

    private void GameWon() {
        Intent intent = new Intent(this, WonActivity.class);
        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);
        startActivity(intent);
    }

    public void resetColor() {
        cardOA.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOB.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOC.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOD.setCardBackgroundColor(getResources().getColor(R.color.white));
    }

    public void OptionAclick(View view) {
        if (!isOptionSelected) {
            processAnswer(currentQuestion.getOption1(), cardOA);
            isOptionSelected = true;
        }
    }

    public void OptionBclick(View view) {
        if (!isOptionSelected) {
            processAnswer(currentQuestion.getOption2(), cardOB);
        }
    }

    public void OptionCclick(View view) {
        if (!isOptionSelected) {
            processAnswer(currentQuestion.getOption3(), cardOC);
        }
    }

    public void OptionDclick(View view) {
        if (!isOptionSelected) {
            processAnswer(currentQuestion.getOption4(), cardOD);
//            isOptionSelected = true;
//            if (currentQuestion.getOption4().equals(currentQuestion.getCorrectAnswer())) {
//                Correct(cardOD);
//                correctCount++;
//            } else {
//                Wrong(cardOD);
//                wrongCount++;
//            }
        }
    }

    private void processAnswer(String userAnswer, CardView selectedCard) {
        if (!isOptionSelected) {
            isOptionSelected = true;
            boolean isCorrect = currentQuestion.getCorrectAnswer().equals(userAnswer);
            if (isCorrect) {
                Correct(selectedCard);
                correctCount++;
            } else {
                Wrong(selectedCard);
                wrongCount++;
            }
            submitAnswer(currentQuestion.getId(), userAnswer, isCorrect);
        }
    }

    private void submitAnswer(Long questionId, String userAnswer, boolean isCorrect) {
        Long userId = sessionManager.getUserId();
        Long quizId = null;// Ficticio por ahora, se sobrescribirá en WonActivity

        // Construye un UserAnswer en vez de un QuizQuestion
        UserAnswer userAnswerObj = new UserAnswer(quizId, questionId, userId, userAnswer, isCorrect);
        userAnswers.add(userAnswerObj);

        QuizQuestion quizQ = new QuizQuestion(quizId, questionId, userId, userAnswer, isCorrect);
        quizQuestions.add(quizQ);
    }

}
