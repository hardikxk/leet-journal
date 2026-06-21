package com.hardik.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RegisteredClientRepository registeredClientRepository;

    @Autowired
    UserDetailsManager userDetailsManager;

    @Test
    void passwordEncoder_bean_created() {
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    void registeredClient_is_registered() {
        var client = registeredClientRepository.findByClientId("client");
        assertThat(client).isNotNull();
        assertThat(client.getClientId()).isEqualTo("client");
    }

    @Test
    void registeredClient_has_correct_grant_types() {
        var client = registeredClientRepository.findByClientId("client");
        assertThat(client).isNotNull();

        assertThat(client.getAuthorizationGrantTypes()).containsExactlyInAnyOrder(
                AuthorizationGrantType.AUTHORIZATION_CODE,
                AuthorizationGrantType.REFRESH_TOKEN,
                AuthorizationGrantType.CLIENT_CREDENTIALS
        );
    }

    @Test
    void registeredClient_has_correct_scopes() {
        var client = registeredClientRepository.findByClientId("client");
        assertThat(client).isNotNull();

        assertThat(client.getScopes()).containsExactlyInAnyOrder(
                "openid", "profile", "user.read", "user.write"
        );
    }

    @Test
    void registeredClient_has_correct_redirect_uris() {
        var client = registeredClientRepository.findByClientId("client");
        assertThat(client).isNotNull();

        assertThat(client.getRedirectUris()).containsExactlyInAnyOrder(
                "http://127.0.0.1:9000/login/oauth2/code/proxy-client-oidc",
                "http://127.0.0.1:9000/login/oauth2/code/messaging-client-authorization-code",
                "http://127.0.0.1:9000/authorized"
        );
    }

    @Test
    void registeredClient_has_correct_auth_method() {
        var client = registeredClientRepository.findByClientId("client");
        assertThat(client).isNotNull();

        assertThat(client.getClientAuthenticationMethods())
                .contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
    }

    @Test
    void registeredClient_has_correct_post_logout_redirect_uri() {
        var client = registeredClientRepository.findByClientId("client");
        assertThat(client).isNotNull();

        assertThat(client.getPostLogoutRedirectUris())
                .contains("http://127.0.0.1:9000/logged-out");
    }

    @Test
    void default_users_are_created() {
        assertThat(userDetailsManager.userExists("hardik")).isTrue();
        assertThat(userDetailsManager.userExists("xane")).isTrue();
        assertThat(userDetailsManager.userExists("sim")).isTrue();
    }

    @Test
    void login_with_valid_credentials_succeeds() throws Exception {
        mockMvc.perform(formLogin("/login")
                        .user("hardik")
                        .password("pass"))
                .andExpect(authenticated());
    }

    @Test
    void login_with_invalid_credentials_fails() throws Exception {
        mockMvc.perform(formLogin("/login")
                        .user("hardik")
                        .password("wrongpassword"))
                .andExpect(unauthenticated());
    }

    @Test
    void user_passwords_are_hashed() {
        var user = userDetailsManager.loadUserByUsername("hardik");
        String encoded = user.getPassword();

        assertThat(encoded).isNotEqualTo("pass");
        assertThat(passwordEncoder.matches("pass", encoded)).isTrue();
    }

    @Test
    void registeredClient_secret_is_noop_plaintext() {
        var client = registeredClientRepository.findByClientId("client");
        assertThat(client).isNotNull();

        // Currently stored as {noop} plaintext — should be replaced with BCrypt in production
        assertThat(client.getClientSecret()).startsWith("{noop}");
    }
}