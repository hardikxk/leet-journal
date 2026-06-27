package com.hardik.problemsservice.repository;

import com.hardik.problemsservice.model.UserProblem;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserProblemRepository extends ListCrudRepository<UserProblem, Integer> {
    List<UserProblem> findByUsername(String username);
    Optional<UserProblem> findByUsernameAndProblemId(String username, int problemId);
}
