package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.MovieDTO;
import com.company.reviewsapi.dto.MovieSearchDTO;
import com.company.reviewsapi.repository.CommentaryRepository;
import com.company.reviewsapi.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final WebClient omdbApiClient;
    private final CommentaryRepository commentaryRepository;
    private final GradeRepository gradeRepository;

    @Value("${omdbApiKey}")
    private String omdbApiKey;

    public Mono<MovieDTO> getMovieByTitle(String title, String year) {
        return omdbApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", omdbApiKey)
                        .queryParam("t", title)
                        .queryParam("y", year)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie no found");
                })
                .map(movieDTO -> {
                    if (movieDTO == null || movieDTO.Response == null || movieDTO.Response.equals("False")) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie no found");
                    }
                    return this.addMovieCommentariesAndGrades(movieDTO);
                });
    }

    public Mono<MovieDTO> getMovieById(String movieId) {
        return this.fetchMovieDataById(movieId)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(this::addMovieCommentariesAndGrades);
    }

    public Mono<MovieSearchDTO> getMovieSearch(String title, String year, Integer page) {
        return omdbApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", omdbApiKey)
                        .queryParam("s", title)
                        .queryParam("y", year)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(MovieSearchDTO.class)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NO MOVIES FOUND");
                })
                .map(movieSearchDTO -> {
                    if (movieSearchDTO == null || movieSearchDTO.Response == null || movieSearchDTO.Response.equals("False")) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NO MOVIES FOUND");
                    }
                    return this.addMovieSearchCommentariesAndGrades(movieSearchDTO);
                });
    }

    //    @Cacheable(value = "movies", key = "#movieId")
    public Mono<MovieDTO> fetchMovieDataById(String movieId) {
        return omdbApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", omdbApiKey)
                        .queryParam("i", movieId)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(movieDTO -> {
                    if (movieDTO == null || movieDTO.Response == null || movieDTO.Response.equals("False")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                    }
                    return movieDTO;
                });
    }

    private MovieDTO addMovieCommentariesAndGrades(MovieDTO movieDTO) {
        movieDTO.commentaries = commentaryRepository.findByMovieId(movieDTO.imdbID);
        movieDTO.grades = gradeRepository.findByMovieId(movieDTO.imdbID);
        return movieDTO;
    }

    private MovieSearchDTO addMovieSearchCommentariesAndGrades(MovieSearchDTO movieSearchDTO) {
        movieSearchDTO.Search.forEach(movie -> {
            movie.commentaries = commentaryRepository.findByMovieId(movie.imdbID);
            movie.grades = gradeRepository.findByMovieId(movie.imdbID);
        });
        return movieSearchDTO;
    }
}
