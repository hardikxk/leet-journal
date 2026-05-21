package com.hardik.problemsservice.service;

import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.repository.ProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Problem> findAllPaginated(Pageable pageable) {
        return problemRepository.findAll(pageable);
    }

    public Problem findProblem(int id){
        return problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found!"));
    }
}