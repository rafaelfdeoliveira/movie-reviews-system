package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.MovieDTO;
import com.company.reviewsapi.dto.MovieSearchDTO;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheable;
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

    @Value("${omdbApiKey}")
    private String omdbApiKey;

    @ReactiveRedisCacheable(cacheName = "moviesByTitle", key = "#title + '_' + #year", timeout = 1800)
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
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie no found");
                })
                .map(movieDTO -> {
                    if (movieDTO == null || movieDTO.Response == null || movieDTO.Response.equals("False")) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie no found");
                    }
                    return movieDTO;
                });
    }

    @ReactiveRedisCacheable(cacheName = "moviesById", key = "#movieId", timeout = 1800)
    public Mono<MovieDTO> getMovieById(String movieId) {
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

    @ReactiveRedisCacheable(cacheName = "moviesSearch", key = "#title + '_' + #year + '_' + #page", timeout = 1800)
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
                    return movieSearchDTO;
                });
    }
}
