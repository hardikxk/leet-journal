package com.hardik.problemsservice.controller;

import com.hardik.problemsservice.service.LocalExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Execution API", description = "Operations related to code execution")
@RestController
public class ExecutionController {

    private final LocalExecutorService localExecutorService;

    public ExecutionController(LocalExecutorService localExecutorService) {
        this.localExecutorService = localExecutorService;
    }

    @Operation(summary = "Run Java code locally")
    @GetMapping("/run")
    public String run() {
        return localExecutorService.executeJava();
    }
}