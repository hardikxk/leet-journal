package com.hardik.problemsservice.exception;

public class ProblemNotFoundException extends RuntimeException {

    public ProblemNotFoundException(String message) {
        super(message);
    }

    public ProblemNotFoundException(Long id) {
        super("Problem not found with id: " + id);
    }
}