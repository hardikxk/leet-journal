package com.hardik.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcUserDetailsManager userDetailsManager;

    @Test
    void contextLoads() {
    }

    @Test
    void registerNewUser_returns201() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        .param("password", "testpass"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Registration successful"));

        userDetailsManager.deleteUser("testuser");
    }

    @Test
    void registerDuplicateUser_returns409() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "duplicate")
                        .param("password", "testpass"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/register")
                        .param("username", "duplicate")
                        .param("password", "testpass"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Username already taken"));

        userDetailsManager.deleteUser("duplicate");
    }
}