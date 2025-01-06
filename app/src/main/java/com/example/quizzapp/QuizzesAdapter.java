package com.example.quizzapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Entity.QuizUser;
import com.example.Utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.QuizViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(QuizUser quiz);
    }

    private List<QuizUser> quizzes;
    private Context context;
    private OnItemClickListener listener;
    private SessionManager sessionManager; // Añadido

    public QuizzesAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.sessionManager = new SessionManager(context);
    }

    public void setQuizzes(List<QuizUser> quizzes) {
        this.quizzes = quizzes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {

//        // Dentro de onBindViewHolder
//        String formattedDate = "";
//        try {
//            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
//            Date date = inputFormat.parse(quiz.getStartTime());
//            formattedDate = outputFormat.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        QuizUser quiz = quizzes.get(position);
        holder.tvQuizCategory.setText("Categoría: " + quiz.getCategory());
        // Asignar el Nombre del Usuario desde SessionManager
        String username = sessionManager.getUsername();
//        holder.tvUsername.setText("Usuario: " + username);


        // Determinar si el quiz fue aprobado o suspendido
        String result = quiz.getScore() >= 10 ? "Aprobado" : "Suspendido";
        holder.tvQuizResult.setText("Resultado: " + result);

        holder.tvQuizScore.setText("Respuestas Correctas: " + quiz.getScore());
        holder.tvTestDate.setText("Fecha: " + quiz.getStartTime()); // Asumiendo que startTime es la fecha del test

        // Formatear la fecha
        String formattedDate = "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = inputFormat.parse(quiz.getStartTime());
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            formattedDate = quiz.getStartTime(); // Usar el valor original si hay error
        }

        holder.tvTestDate.setText("Fecha: " + formattedDate);

//        holder.tvTestDate.setText("Fecha: " + formattedDate);

        // Configurar el ImageView basado en el puntaje
//        if (quiz.getScore() >= 10) {
//            holder.ivCorrectIndicator.setImageResource(R.drawable.ic_correct); // Drawable para correcto
//        } else {
//            holder.ivCorrectIndicator.setImageResource(R.drawable.ic_incorrect); // Drawable para incorrecto
//        }

        holder.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.onItemClick(quiz);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzes != null ? quizzes.size() : 0;
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuizCategory, tvQuizResult, tvQuizScore, tvTestDate;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvQuizCategory = itemView.findViewById(R.id.tvQuizCategory);
            tvQuizResult = itemView.findViewById(R.id.tvQuizResult);
            tvTestDate = itemView.findViewById(R.id.tvTestDate);
            tvQuizScore = itemView.findViewById(R.id.tvQuizScore);
        }
    }
}
