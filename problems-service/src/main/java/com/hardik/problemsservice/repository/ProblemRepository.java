package com.hardik.problemsservice.repository;

import com.hardik.problemsservice.model.Problem;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ProblemRepository extends ListCrudRepository<Problem, Integer> {
    Optional<Problem> findByTitle(String title);
}