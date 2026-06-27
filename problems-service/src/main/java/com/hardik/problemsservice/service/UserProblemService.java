package com.hardik.problemsservice.service;

import com.hardik.problemsservice.model.UserProblem;
import com.hardik.problemsservice.repository.UserProblemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserProblemService {

    private final UserProblemRepository userProblemRepository;

    public UserProblemService(UserProblemRepository userProblemRepository) {
        this.userProblemRepository = userProblemRepository;
    }

    public List<UserProblem> findByUsername(String username) {
        return userProblemRepository.findByUsername(username);
    }

    public UserProblem track(String username, int problemId, boolean isDone, boolean isFlagged) {
        userProblemRepository.findByUsernameAndProblemId(username, problemId).ifPresent(_ -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem " + problemId + " is already tracked.");
        });
        return userProblemRepository.save(new UserProblem(null, username, problemId, isDone, isFlagged));
    }

    public UserProblem update(int id, String username, boolean isDone, boolean isFlagged) {
        var existing = userProblemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found."));
        if (!existing.username().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to modify this entry.");
        }
        return userProblemRepository.save(new UserProblem(id, username, existing.problemId(), isDone, isFlagged));
    }
}
