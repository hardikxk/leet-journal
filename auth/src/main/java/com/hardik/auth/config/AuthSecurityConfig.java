package com.hardik.auth.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Configuration
@EnableWebSecurity
class AuthSecurityConfig {

    @Bean
    @org.springframework.core.annotation.Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());

        http
            .exceptionHandling((exceptions) -> exceptions
                .defaultAuthenticationEntryPointFor(
                    new org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint("/login"),
                    new org.springframework.security.web.util.matcher.MediaTypeRequestMatcher(org.springframework.http.MediaType.TEXT_HTML)
                )
            )
            .oauth2ResourceServer((resourceServer) -> resourceServer
                .jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    @org.springframework.core.annotation.Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    ApplicationRunner clientRunner(RegisteredClientRepository registeredClientRepository) {
        return _ -> {
            var clientId = "client";
            if(registeredClientRepository.findByClientId(clientId) == null){
                registeredClientRepository.save(
                        RegisteredClient
                                .withId(UUID.randomUUID().toString())
                                .clientId(clientId)
                                .clientSecret("{noop}secret")
                                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                                .authorizationGrantTypes(agt -> agt.addAll(Set.of(
                                        AuthorizationGrantType.AUTHORIZATION_CODE,
                                        AuthorizationGrantType.REFRESH_TOKEN,
                                        AuthorizationGrantType.CLIENT_CREDENTIALS
                                )))
                                .redirectUris(uri -> uri.addAll(Set.of(
                                        "http://127.0.0.1:9000/login/oauth2/code/proxy-client-oidc",
                                        "http://127.0.0.1:9000/login/oauth2/code/messaging-client-authorization-code",
                                        "http://127.0.0.1:9000/authorized"
                                )))
                                .postLogoutRedirectUri("http://127.0.0.1:9000/logged-out")
                                .scopes(s -> s.addAll(Set.of(
                                        "openid", "profile", "user.read", "user.write"
                                )))
                                .build()
                );
            }
        };
    }

    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    ApplicationRunner userRunner(UserDetailsManager  userDetailsManager){
        return _ -> {

            var users = Map.of(
                    "xane", "pass",
                    "sim", "pass",
                    "hardik","pass"
            );
            users.forEach((un, pw) -> {
                if(!userDetailsManager.userExists(un)){
                    var user = User
                            .withUsername(un)
                            .password(passwordEncoder().encode(pw))
                            .roles("USER")
                            .build();
                    userDetailsManager.createUser(user);
                }
            });
        };
    }
}
