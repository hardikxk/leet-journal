package com.hardik.problemsservice.repository;

import com.hardik.problemsservice.model.Problem;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;

public interface ProblemRepository extends PagingAndSortingRepository<Problem, Integer>, ListCrudRepository<Problem, Integer> {
    
    Optional<Problem> findByTitle(String title);
    
}
