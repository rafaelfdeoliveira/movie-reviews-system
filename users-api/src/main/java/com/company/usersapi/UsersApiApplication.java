package com.company.usersapi;

import com.company.usersapi.model.Authority;
import com.company.usersapi.model.AuthorityKey;
import com.company.usersapi.model.User;
import com.company.usersapi.repository.AuthorityRepository;
import com.company.usersapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class UsersApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersApiApplication.class, args);
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