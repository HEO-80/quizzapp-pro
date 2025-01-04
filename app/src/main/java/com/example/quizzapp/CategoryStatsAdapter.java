package com.example.quizzapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Entity.CategoryStats;

import java.util.List;

public class CategoryStatsAdapter extends RecyclerView.Adapter<CategoryStatsAdapter.CategoryStatsViewHolder> {

    private List<CategoryStats> categoryStatsList;

    public void setCategoryStats(List<CategoryStats> categoryStatsList) {
        this.categoryStatsList = categoryStatsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_stats, parent, false);
        return new CategoryStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryStatsViewHolder holder, int position) {
        CategoryStats stats = categoryStatsList.get(position);
        holder.tvCategoryName.setText("Categoría: " + stats.getCategory());
        holder.tvCorrectAnswers.setText("Correctas: " + stats.getCorrectAnswers());
        holder.tvIncorrectAnswers.setText("Incorrectas: " + stats.getIncorrectAnswers());
    }

    @Override
    public int getItemCount() {
        return categoryStatsList != null ? categoryStatsList.size() : 0;
    }

    class CategoryStatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvCorrectAnswers, tvIncorrectAnswers;

        public CategoryStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCorrectAnswers = itemView.findViewById(R.id.tvCorrectAnswers);
            tvIncorrectAnswers = itemView.findViewById(R.id.tvIncorrectAnswers);
        }
    }
}
