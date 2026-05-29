package com.hardik.problemsservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.repository.ProblemRepository;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public List<Problem> findAll() {
        return problemRepository.findAll();
    }

    public Problem findProblem(int id){
        return problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found!"));
    }

    public Problem findProblemByTitle(String title){
        return problemRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found!"));
    }

    // ADD THIS METHOD
    public List<Problem> searchProblems(String difficulty, String search) {

        boolean hasDifficulty = difficulty != null && !difficulty.isBlank();
        boolean hasSearch = search != null && !search.isBlank();

        if (hasDifficulty && hasSearch) {
            return problemRepository
                    .findByDifficultyIgnoreCaseAndTitleContainingIgnoreCase(
                            difficulty,
                            search
                    );
        }

        if (hasDifficulty) {
            return problemRepository.findByDifficultyIgnoreCase(difficulty);
        }

        if (hasSearch) {
            return problemRepository.findByTitleContainingIgnoreCase(search);
        }

        return problemRepository.findAll();
    }
}