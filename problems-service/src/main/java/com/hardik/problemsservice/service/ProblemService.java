package com.hardik.problemsservice.service;

import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.repository.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return problemRepository.findById(id).orElse(null);
    }
}