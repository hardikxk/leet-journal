package com.hardik.batchservice;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProblemItemProcessor implements ItemProcessor<Problem, Problem> {

    @Override
    public Problem process(Problem item) {
        if (item.title() == null || item.title().isBlank()) return null;
        if (!isValidDifficulty(item.difficulty())) return null;

        String normalizedDifficulty = capitalize(item.difficulty());
        String acceptance = item.acceptance() == null ? "" : item.acceptance().trim();

        return new Problem(item.id(), item.title().trim(), acceptance, normalizedDifficulty);
    }

    private boolean isValidDifficulty(String difficulty) {
        return difficulty != null &&
                (difficulty.equalsIgnoreCase("Easy")
                 || difficulty.equalsIgnoreCase("Medium")
                 || difficulty.equalsIgnoreCase("Hard"));
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) return value;
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
