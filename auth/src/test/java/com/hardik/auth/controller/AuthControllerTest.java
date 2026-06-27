package com.hardik.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserDetailsManager userDetailsManager;

    @Test
    void register_with_valid_credentials_returns_201() throws Exception {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", username)
                        .param("password", "securepass"))
                .andExpect(status().isCreated());

        assertThat(userDetailsManager.userExists(username)).isTrue();
    }

    @Test
    void register_with_duplicate_username_returns_409() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "hardik")
                        .param("password", "anypassword"))
                .andExpect(status().isConflict());
    }

    @Test
    void register_with_blank_username_returns_400() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "  ")
                        .param("password", "securepass"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_with_blank_password_returns_400() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "validuser")
                        .param("password", "  "))
                .andExpect(status().isBadRequest());
    }
}
