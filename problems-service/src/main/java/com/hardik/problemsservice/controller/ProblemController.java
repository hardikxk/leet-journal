package com.hardik.problemsservice.controller;

import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ProblemController {

    private final ProblemService problemService;
    private static final int MAX_PAGE_SIZE = 500;
    private static final int DEFAULT_PAGE_SIZE = 50;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/hello")
    String hello() {
        return "Leet";
    }

    @GetMapping("/me")
    String me() {
        var jwt = (Jwt) Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                        .getPrincipal();
        if (jwt == null) {
            return "No user authentication found!";
        }
        return "ciao " + jwt.getSubject();
    }

    @GetMapping("/all")
    List<Problem> findAll() {
        return problemService.findAll();
    }

    @GetMapping("/paginated")
    Page<Problem> findAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = String.valueOf(DEFAULT_PAGE_SIZE)) int size) {
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        if (size <= 0) {
            size = DEFAULT_PAGE_SIZE;
        }
        Pageable pageable = PageRequest.of(page, size);
        return problemService.findAllPaginated(pageable);
    }

    @GetMapping("/find/{id}")
    Problem findById(@PathVariable int id) {
        return problemService.findProblem(id);
    }
}