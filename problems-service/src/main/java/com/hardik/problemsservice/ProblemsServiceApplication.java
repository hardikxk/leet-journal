package com.hardik.problemsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public Problem findProblem(int id){
    // ... stream filters ...
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Problem not found!"));
}


@SpringBootApplication
public class ProblemsServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(ProblemsServiceApplication.class, args);
    }

}
