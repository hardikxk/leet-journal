package com.hardik.problemsservice.controller;

import com.hardik.problemsservice.model.UserProblem;
import com.hardik.problemsservice.service.UserProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-problems")
public class UserProblemController {

    private final UserProblemService userProblemService;

    public UserProblemController(UserProblemService userProblemService) {
        this.userProblemService = userProblemService;
    }

    @GetMapping
    public List<UserProblem> getAll(@AuthenticationPrincipal Jwt jwt) {
        return userProblemService.findByUsername(jwt.getSubject());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProblem track(@AuthenticationPrincipal Jwt jwt,
                             @RequestParam int problemId,
                             @RequestParam(defaultValue = "false") boolean isDone,
                             @RequestParam(defaultValue = "false") boolean isFlagged) {
        return userProblemService.track(jwt.getSubject(), problemId, isDone, isFlagged);
    }

    @PatchMapping("/{id}")
    public UserProblem update(@AuthenticationPrincipal Jwt jwt,
                              @PathVariable int id,
                              @RequestParam boolean isDone,
                              @RequestParam boolean isFlagged) {
        return userProblemService.update(id, jwt.getSubject(), isDone, isFlagged);
    }
}
