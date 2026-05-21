package com.hardik.problemsservice.service;

import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.repository.ProblemRepository;
import org.apache.el.stream.Optional;
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
    public Optional<Problem> findProblem(int id){
        return problemRepository.findAll().stream().filter(
                p -> p.id() == id
        )
                .findFirst();
    }
}