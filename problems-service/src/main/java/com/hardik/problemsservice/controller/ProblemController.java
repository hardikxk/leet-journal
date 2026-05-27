package com.hardik.problemsservice.controller;

import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.service.ProblemService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this. problemService = problemService;
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

    @GetMapping("/find/{id}")
    ResponseEntity<Problem> findById(@PathVariable int id) {
        Problem prob=problemService.findProblem(id);
        if(prob==null)
        {
            throw new IllegalArgumentException("Not found any Problem with this id");
        }
        return ResponseEntity.ok(prob);
    }

    @GetMapping("/title/{title}")
    Problem findByTitle(@PathVariable String title) {
        return problemService.findProblemByTitle(title);
    }
}