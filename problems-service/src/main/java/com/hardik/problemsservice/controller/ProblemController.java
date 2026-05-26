package com.hardik.problemsservice.controller;

import com.hardik.problemsservice.model.Problem;
import com.hardik.problemsservice.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Tag(name = "Problem API", description = "Operations related to problems")
@RestController
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Operation(summary = "Test endpoint")
    @GetMapping("/hello")
    String hello() {
        return "Leet";
    }

    @Operation(summary = "Get authenticated user")
    @GetMapping("/me")
    String me() {
        var jwt = (Jwt) Objects.requireNonNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal();

        if (jwt == null) {
            return "No user authentication found!";
        }

        return "ciao " + jwt.getSubject();
    }

    @Operation(summary = "Get all problems")
    @GetMapping("/all")
    List<Problem> findAll() {
        return problemService.findAll();
    }

    @Operation(summary = "Find problem by ID")
    @GetMapping("/find/{id}")
    Problem findById(@PathVariable int id) {
        return problemService.findProblem(id);
    }

    @GetMapping("/title/{title}")
    Problem findByTitle(@PathVariable String title) {
        return problemService.findProblemByTitle(title);
    }
}