package com.company.usersapi;

import com.company.usersapi.model.Authority;
import com.company.usersapi.model.AuthorityKey;
import com.company.usersapi.model.User;
import com.company.usersapi.repository.AuthorityRepository;
import com.company.usersapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@SpringBootApplication

public class UsersApiApplication {

    public static void main(String[] args) { SpringApplication.run(UsersApiApplication.class, args); }

    @Value("${reviewsApiUrl}")
    private String reviewsApiUrl;

    @Bean
    public WebClient reviewsApiClient() {
        return WebClient.create(reviewsApiUrl);
    }

    @Bean
    public CommandLineRunner createDefaultUsers(UserRepository userRepository,
                                  AuthorityRepository authorityRepository,
                                  PasswordEncoder encoder) {
        return (args) -> {

            User user = new User();
            user.setUserName("MODERADOR1");
            user.setPassword(encoder.encode("123"));
            user.setEnabled(true);
            user.setPoints(1000);
            userRepository.save(user);

            List<String> authorityNamesList = List.of("LEITOR", "BÁSICO", "AVANÇADO", "MODERADOR");
            authorityNamesList.forEach((authorityName) -> {
                Authority authority = new Authority();
                authority.setAuthorityKey(new AuthorityKey(user.getUserName(), authorityName));
                authority.setUser(user);
                authorityRepository.save(authority);
            });
        };
    }
}