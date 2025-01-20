package com.example.quizzapp;

import static org.junit.Assert.assertEquals;

import com.example.Entity.CategoryStats;
import com.example.Entity.QuizUser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivityTest {

    @Test
    public void testCalculateCategoryStatsLocally() {
        // lista de quizzes
        List<QuizUser> quizzes = new ArrayList<>();

        quizzes.add(makeQuizUser("Programacion", 12));
        quizzes.add(makeQuizUser("Programacion", 5));
        quizzes.add(makeQuizUser("Sistemas", 10));
        quizzes.add(makeQuizUser("Sistemas", 9));
        quizzes.add(makeQuizUser("Matematicas", 15));

        List<CategoryStats> stats = calculateCategoryStatsLocally(quizzes);

        // Verificamos
        assertEquals(3, stats.size());

        CategoryStats programacion = findCategory("Programacion", stats);
        assertEquals(Integer.valueOf(1), programacion.getCorrectAnswers());
        assertEquals(Integer.valueOf(1), programacion.getIncorrectAnswers());

        CategoryStats sistemas = findCategory("Sistemas", stats);
        assertEquals(Integer.valueOf(1), sistemas.getCorrectAnswers());
        assertEquals(Integer.valueOf(1), sistemas.getIncorrectAnswers());

        CategoryStats matematicas = findCategory("Matematicas", stats);
        assertEquals(Integer.valueOf(1), matematicas.getCorrectAnswers());
        assertEquals(Integer.valueOf(0), matematicas.getIncorrectAnswers());
    }

    private List<CategoryStats> calculateCategoryStatsLocally(List<QuizUser> quizzes) {

        java.util.Map<String, CategoryStats> mapStats = new java.util.HashMap<>();

        for (QuizUser quiz : quizzes) {
            String category = quiz.getCategory();
            if (!mapStats.containsKey(category)) {
                CategoryStats catStat = new CategoryStats();
                catStat.setCategory(category);
                catStat.setCorrectAnswers(0);
                catStat.setIncorrectAnswers(0);
                mapStats.put(category, catStat);
            }
            if (quiz.getScore() != null && quiz.getScore() >= 10) {
                mapStats.get(category).setCorrectAnswers(
                        mapStats.get(category).getCorrectAnswers() + 1
                );
            } else {
                mapStats.get(category).setIncorrectAnswers(
                        mapStats.get(category).getIncorrectAnswers() + 1
                );
            }
        }

        return new ArrayList<>(mapStats.values());
    }

    // Método auxiliar para crear QuizUser
    private QuizUser makeQuizUser(String category, int score) {
        QuizUser quizUser = new QuizUser();
        quizUser.setCategory(category);
        quizUser.setScore(score);
        return quizUser;
    }

    // Método auxiliar para encontrar la categoría
    private CategoryStats findCategory(String category, List<CategoryStats> statsList) {
        for (CategoryStats cs : statsList) {
            if (cs.getCategory().equals(category)) {
                return cs;
            }
        }
        return null;
    }
}
