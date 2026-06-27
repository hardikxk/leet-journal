package com.hardik.problemsservice.model;

import org.springframework.data.annotation.Id;

public record UserProblem(@Id Integer id, String username, int problemId, boolean isDone, boolean isFlagged) {
}
