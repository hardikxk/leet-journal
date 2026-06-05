package com.hardik.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles user registration.
 *
 * <p>Duplicate-submission protection is provided by {@link com.hardik.auth.filter.IdempotencyFilter}
 * at the HTTP layer. This controller additionally guards against duplicate
 * usernames at the data layer so both mechanisms work together.</p>
 */
@RestController
class AuthController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    AuthController(UserDetailsManager userDetailsManager,
                   PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * <p>Clients must send an {@code Idempotency-Key} header to prevent
     * accidental duplicate registrations when users double-click or the
     * network retries the request. See {@link com.hardik.auth.filter.IdempotencyFilter}.</p>
     *
     * @param username desired username
     * @param password plain-text password (encoded before storage)
     * @return 201 Created on success, 409 Conflict if username is taken,
     *         400 Bad Request for blank inputs
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password) {

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username must not be blank.");
        }
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body("Password must not be blank.");
        }

        if (userDetailsManager.userExists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username '" + username + "' is already taken.");
        }

        var user = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();

        userDetailsManager.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User '" + username + "' registered successfully.");
    }
}
