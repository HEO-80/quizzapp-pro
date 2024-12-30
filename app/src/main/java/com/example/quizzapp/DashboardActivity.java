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
import com.example.Utils.SessionManager;

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

    private final int questionLimit = 20;
    private boolean isOptionSelected = false;

    // Dentro de DashboardActivity
    private List<QuizQuestion> userAnswers = new ArrayList<>(); // Lista para almacenar las respuestas
    private String selectedCategory = "General"; // Define una categoría predeterminada o configúrala dinámicamente

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
        selectedCategory = intent.getStringExtra("selectedCategory");
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
        Intent intent = new Intent(DashboardActivity.this, WonActivity.class);
        intent.putExtra("userAnswers", new ArrayList<>(userAnswers)); // Envía la lista como Serializable
        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);
        intent.putExtra("category", selectedCategory);
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
            isOptionSelected = true;
            if (currentQuestion.getOption1().equals(currentQuestion.getCorrectAnswer())) {
                Correct(cardOA);
                correctCount++;
            } else {
                Wrong(cardOA);
                wrongCount++;
            }
        }
    }

    public void OptionBclick(View view) {
        if (!isOptionSelected) {
            isOptionSelected = true;
            if (currentQuestion.getOption2().equals(currentQuestion.getCorrectAnswer())) {
                Correct(cardOB);
                correctCount++;
            } else {
                Wrong(cardOB);
                wrongCount++;
            }
        }
    }

    public void OptionCclick(View view) {
        if (!isOptionSelected) {
            isOptionSelected = true;
            if (currentQuestion.getOption3().equals(currentQuestion.getCorrectAnswer())) {
                Correct(cardOC);
                correctCount++;
            } else {
                Wrong(cardOC);
                wrongCount++;
            }
        }
    }

    public void OptionDclick(View view) {
        if (!isOptionSelected) {
            isOptionSelected = true;
            if (currentQuestion.getOption4().equals(currentQuestion.getCorrectAnswer())) {
                Correct(cardOD);
                correctCount++;
            } else {
                Wrong(cardOD);
                wrongCount++;
            }
        }
    }


    // Método donde se maneja la selección de respuesta
//    private void submitAnswer(Long questionId, String userAnswer, boolean isCorrect) {
//        Long userId = sessionManager.getUserId(); // Obtén el userId del usuario actual
//        Long quizId = 1L; // ID del test actual (puedes generarlo dinámicamente)
//
//        QuizQuestion quizQuestion = new QuizQuestion(quizId, questionId, userId, userAnswer, isCorrect);
//        userAnswers.add(quizQuestion); // Almacena la respuesta en la lista
//    }

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
        Long quizId = 1L;

        QuizQuestion quizQuestion = new QuizQuestion(quizId, questionId, userId, userAnswer, isCorrect);
        userAnswers.add(quizQuestion);
    }

}
