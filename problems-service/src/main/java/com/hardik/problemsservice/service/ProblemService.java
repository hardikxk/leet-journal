package com.hardik.problemsservice.service;

import com.hardik.problemsservice.dto.PageResponse;
import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.repository.ProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public PageResponse<Problem> findAll(Pageable pageable) {
        Page<Problem> page = problemRepository.findAll(pageable);
        
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );
    }
    public Problem findProblem(int id){
        return problemRepository.findAll().stream().filter(
                p -> p.id() == id
        )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Problem not found!"));
    }
}