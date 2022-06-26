package com.company.reviewsapi.controller;

import com.company.reviewsapi.dto.MovieDTO;
import com.company.reviewsapi.dto.MovieSearchDTO;
import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    @GetMapping("/movie")
    public Mono<MovieDTO> getMovieByTitle(
            @RequestParam String title,
            @RequestParam(required = false) String year
    ) {
        return reviewsService.getMovieByTitle(title, year);
    }

    @GetMapping("/movie/id")
    public Mono<MovieDTO> getMovieById(@RequestParam String movieId) {
        return reviewsService.getMovieById(movieId);
    }

    @GetMapping("/movie/search")
    public Mono<MovieSearchDTO> getMovieSearch(
            @RequestParam String title,
            @RequestParam(required = false) String year
    ){
        return reviewsService.getMovieSearch(title, year);
    }

    @PostMapping("/grade")
    public Mono<Grade> registerMovieGrade(@RequestBody Grade grade) {
        return reviewsService.registerMovieGrade(grade);
    }
}