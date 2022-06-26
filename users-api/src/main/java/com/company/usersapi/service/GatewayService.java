package com.company.usersapi.service;

import com.company.usersapi.config.JwtTokenUtil;
import com.company.usersapi.dto.GradeDTO;
import com.company.usersapi.dto.MovieDTO;
import com.company.usersapi.model.Authority;
import com.company.usersapi.model.User;
import com.company.usersapi.repository.AuthorityRepository;
import com.company.usersapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatewayService {
    private final WebClient reviewsApiClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public MovieDTO getMovie(String title, String year) {
        MovieDTO movieDTO = reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie")
                        .queryParam("title", title)
                        .queryParam("year", year)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block();
        if (movieDTO == null || movieDTO.Response == null || movieDTO.Response.equals("False")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE NOT FOUND");
        }
        return  movieDTO;
    }

    public GradeDTO registerMovieGrade(String accessToken, GradeDTO gradeDTO) {
        String token = accessToken.substring(7);
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        this.giveOnePointToUser(userName);
        gradeDTO.setUserName(userName);
        return reviewsApiClient
                .post()
                .uri("/movie/grade")
                .body(BodyInserters.fromValue(gradeDTO))
                .retrieve()
                .bodyToMono(GradeDTO.class)
                .block();
    }

    private void giveOnePointToUser(String userName) {
        User user = userRepository.getById(userName);
        int points = user.getPoints() + 1;
        user.setPoints(points);
        List<String> userRoles = user.getRoles();
        if (points >= 20 && !userRoles.contains("BÁSICO")) {
            this.addAuthorityToUser(user, "BÁSICO");
        }
        if (points >= 100 && !userRoles.contains("AVANÇADO")) {
            this.addAuthorityToUser(user, "AVANÇADO");
        }
        if (points >= 1000 && !userRoles.contains("MODERADOR")) {
            this.addAuthorityToUser(user, "MODERADOR");
        }

        userRepository.save(user);
    }

    private void addAuthorityToUser(User user, String role) {
        Authority authority = authorityRepository.save(Authority.convert(user, role));
        List<Authority> userAuthorities = user.getAuthorities();
        userAuthorities.add(authority);
        user.setAuthorities(userAuthorities);
    }
}