package com.hardik.problemsservice.repository;

import com.hardik.problemsservice.model.Problem;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProblemRepository extends PagingAndSortingRepository<Problem, Integer>, ListCrudRepository<Problem, Integer> {
}