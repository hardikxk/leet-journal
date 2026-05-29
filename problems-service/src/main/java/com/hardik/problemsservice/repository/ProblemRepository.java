package com.hardik.problemsservice.repository;

import com.hardik.problemsservice.model.Problem;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends ListCrudRepository<Problem, Integer> {

    Optional<Problem> findByTitle(String title);

    List<Problem> findByDifficultyIgnoreCase(String difficulty);

    List<Problem> findByTitleContainingIgnoreCase(String title);

    List<Problem> findByDifficultyIgnoreCaseAndTitleContainingIgnoreCase(String difficulty, String title);
}