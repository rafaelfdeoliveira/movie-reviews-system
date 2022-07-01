package com.company.usersapi.service;

import com.company.usersapi.config.JwtTokenUtil;
import com.company.usersapi.dto.*;
import com.company.usersapi.model.Authority;
import com.company.usersapi.model.User;
import com.company.usersapi.repository.AuthorityRepository;
import com.company.usersapi.repository.UserRepository;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCachePut;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GatewayService {
    private final WebClient reviewsApiClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserService userService;
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
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE NOT FOUND");
                });
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
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE NOT FOUND");
                });
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
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NO MOVIES FOUND");
                });
    }

    public Mono<RestPage<GradeDTO>> getMovieGrades(String movieId, Pageable pageable) {
        return reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/grade")
                        .queryParam("movieId", movieId)
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .queryParam("sort", this.getSortParametersArray(pageable))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RestPage<GradeDTO>>() {})
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
                });
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

    public Mono<RestPage<CommentaryDTO>> getMovieCommentaries(String movieId, Pageable pageable) {
        return reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/commentary")
                        .queryParam("movieId", movieId)
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .queryParam("sort", this.getSortParametersArray(pageable))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RestPage<CommentaryDTO>>() {})
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
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

    public Mono<CommentaryDTO> deleteMovieCommentary(Long commentaryId) {
        return reviewsApiClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/commentary")
                        .queryParam("commentaryId", commentaryId)
                        .build())
                .retrieve()
                .bodyToMono(CommentaryDTO.class)
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commentary with provided commentaryId was not found");
                });
    }

    public Mono<RestPage<CommentaryReplyDTO>> getCommentaryReplies(Long commentaryId, Pageable pageable) {
        return reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/commentary/reply")
                        .queryParam("commentaryId", commentaryId)
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .queryParam("sort", this.getSortParametersArray(pageable))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RestPage<CommentaryReplyDTO>>() {})
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid query parameter");
                })
                .map(commentaryReplyPage -> {
                    commentaryReplyPage.getContent().forEach(commentaryReplyDTO -> commentaryReplyDTO.setCommentaryId(commentaryId));
                    return commentaryReplyPage;
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

    public Mono<RestPage<CommentaryEvaluationDTO>> getCommentaryEvaluations(Long commentaryId, Pageable pageable) {
        return reviewsApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("commentary/evaluation")
                        .queryParam("commentaryId", commentaryId)
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .queryParam("sort", this.getSortParametersArray(pageable))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RestPage<CommentaryEvaluationDTO>>() {})
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid query parameters");
                })
                .map(commentaryEvaluationPage -> {
                    commentaryEvaluationPage.getContent().forEach(commentaryEvaluationDTO -> commentaryEvaluationDTO.setCommentaryId(commentaryId));
                    return commentaryEvaluationPage;
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

    private Object[] getSortParametersArray(Pageable pageable) {
        List<String> sorts = new ArrayList<>();
        pageable.getSort().forEach(order -> sorts.add(String.join(",", order.getProperty(), order.getDirection().toString())));
        return sorts.toArray();
    }

    @ReactiveRedisCachePut(cacheName = "userByUserName", key = "#userName", timeout = 1800)
    private User giveUserOnePoint(String userName) {
        User user = this.userService.getUserByUserName(userName);
        user.setPoints(user.getPoints() + 1);
        this.updateUserAuthorities(user);
        return userRepository.save(user);
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