package com.hardik.problemsservice.controller;

import com.hardik.problemsservice.repository.UserProblemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserProblemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserProblemRepository userProblemRepository;

    @Test
    void get_all_returns_empty_list_for_new_user() throws Exception {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(get("/user-problems")
                        .with(jwt().jwt(j -> j.subject(username))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void track_creates_entry_and_returns_201() throws Exception {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(post("/user-problems")
                        .with(jwt().jwt(j -> j.subject(username)))
                        .with(csrf())
                        .param("problemId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.problemId").value(1))
                .andExpect(jsonPath("$.isDone").value(false))
                .andExpect(jsonPath("$.isFlagged").value(false));
    }

    @Test
    void track_duplicate_problem_returns_400() throws Exception {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(post("/user-problems")
                        .with(jwt().jwt(j -> j.subject(username)))
                        .with(csrf())
                        .param("problemId", "2"));

        mockMvc.perform(post("/user-problems")
                        .with(jwt().jwt(j -> j.subject(username)))
                        .with(csrf())
                        .param("problemId", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_changes_isDone_and_isFlagged() throws Exception {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(post("/user-problems")
                        .with(jwt().jwt(j -> j.subject(username)))
                        .with(csrf())
                        .param("problemId", "3"))
                .andExpect(status().isCreated());

        int id = userProblemRepository.findByUsername(username).get(0).id();

        mockMvc.perform(patch("/user-problems/" + id)
                        .with(jwt().jwt(j -> j.subject(username)))
                        .with(csrf())
                        .param("isDone", "true")
                        .param("isFlagged", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDone").value(true))
                .andExpect(jsonPath("$.isFlagged").value(true));
    }
}
