package com.company.usersapi.service;

import com.company.usersapi.config.JwtTokenUtil;
import com.company.usersapi.dto.*;
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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatewayService {
    private final WebClient reviewsApiClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public Mono<MovieDTO> getMovieByTitle(String title, String year) {
        return reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/title")
                        .queryParam("title", title)
                        .queryParam("year", year)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE NOT FOUND");
                })
                .map(this::checkIfMovieWasFound);
    }

    public Mono<MovieDTO> getMovieById(String movieId) {
        return reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/id")
                        .queryParam("movieId", movieId)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE NOT FOUND");
                })
                .map(this::checkIfMovieWasFound);
    }

    public Mono<MovieSearchDTO> getMovieSearch(String title, String year, Integer page) {
        return reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("movie/search")
                        .queryParam("title", title)
                        .queryParam("year", year)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(MovieSearchDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NO MOVIES FOUND");
                })
                .map(this::checkIfMovieSearchFailed);
    }

    public Mono<GradeDTO> registerMovieGrade(String accessToken, GradeDTO gradeDTO) {
        String userName = jwtTokenUtil.getUsernameFromToken(accessToken.substring(7));
        gradeDTO.setUserName(userName);
        return reviewsApiClient
                .post()
                .uri("/grade")
                .body(BodyInserters.fromValue(gradeDTO))
                .retrieve()
                .bodyToMono(GradeDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(grade -> {
                    this.giveUserOnePoint(grade.getUserName());
                    return grade;
                });
    }

    public Mono<CommentaryDTO> registerMovieCommentary(String accessToken, CommentaryDTO commentaryDTO) {
        String userName = jwtTokenUtil.getUsernameFromToken(accessToken.substring(7));
        commentaryDTO.setUserName(userName);
        return reviewsApiClient
                .post()
                .uri("/commentary")
                .body(BodyInserters.fromValue(commentaryDTO))
                .retrieve()
                .bodyToMono(CommentaryDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(commentary -> {
                    this.giveUserOnePoint(commentary.getUserName());
                    return commentary;
                });

    }

    public Mono<CommentaryReplyDTO> registerCommentaryReply(String accessToken, CommentaryReplyDTO commentaryReplyDTO) {
        String userName = jwtTokenUtil.getUsernameFromToken(accessToken.substring(7));
        commentaryReplyDTO.setUserName(userName);
        return reviewsApiClient
                .post()
                .uri("/commentary/reply")
                .body(BodyInserters.fromValue(commentaryReplyDTO))
                .retrieve()
                .bodyToMono(CommentaryReplyDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CommentaryId");
                })
                .map(commentaryReply -> {
                    commentaryReply.setCommentaryId(commentaryReplyDTO.getCommentaryId());
                    this.giveUserOnePoint(commentaryReply.getUserName());
                    return commentaryReply;
                });
    }

    public Mono<CommentaryEvaluationDTO> registerCommentaryEvaluation(String accessToken, CommentaryEvaluationDTO commentaryEvaluationDTO) {
        String userName = jwtTokenUtil.getUsernameFromToken(accessToken.substring(7));
        commentaryEvaluationDTO.setUserName(userName);
        return reviewsApiClient
                .post()
                .uri("/commentary/evaluation")
                .body(BodyInserters.fromValue(commentaryEvaluationDTO))
                .retrieve()
                .bodyToMono(CommentaryEvaluationDTO.class)
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CommentaryId");
                })
                .map(commentaryEvaluation -> {
                    commentaryEvaluation.setCommentaryId(commentaryEvaluationDTO.getCommentaryId());
                    return commentaryEvaluation;
                });
    }

    private MovieDTO checkIfMovieWasFound(MovieDTO movieDTO) {
        if (movieDTO == null || movieDTO.Response == null || movieDTO.Response.equals("False")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE NOT FOUND");
        }
        return  movieDTO;
    }

    private MovieSearchDTO checkIfMovieSearchFailed(MovieSearchDTO movieSearchDTO) {
        if (movieSearchDTO == null || movieSearchDTO.Response == null || movieSearchDTO.Response.equals("False")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NO MOVIES FOUND");
        }
        return movieSearchDTO;
    }

    private void giveUserOnePoint(String userName) {
        User user = userRepository.findOneByUserName(userName);
        user.setPoints(user.getPoints() + 1);
        this.updateUserAuthorities(user);
        userRepository.save(user);
    }

    private void updateUserAuthorities(User user) {
        int points = user.getPoints();
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
    }

    private void addAuthorityToUser(User user, String role) {
        Authority authority = authorityRepository.save(Authority.convert(user, role));
        List<Authority> userAuthorities = user.getAuthorities();
        userAuthorities.add(authority);
        user.setAuthorities(userAuthorities);
    }
}