package com.company.reviewsapi.controller;

import com.company.reviewsapi.dto.MovieDTO;
import com.company.reviewsapi.dto.MovieSearchDTO;
import com.company.reviewsapi.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/title")
    public Mono<MovieDTO> getMovieByTitle(
            @RequestParam String title,
            @RequestParam(required = false) String year
    ) {
        return movieService.getMovieByTitle(title, year);
    }

    @GetMapping("/id")
    public Mono<MovieDTO> getMovieById(@RequestParam String movieId) {
        return movieService.getMovieById(movieId);
    }

    @GetMapping("/search")
    public Mono<MovieSearchDTO> getMovieSearch(
            @RequestParam String title,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) Integer page
    ) {
        return movieService.getMovieSearch(title, year, page);
    }
}
