package com.company.reviewsapi.controller;

import com.company.reviewsapi.dto.MovieDTO;
import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    @GetMapping("/movie")
    public MovieDTO getMovie(@Valid @RequestParam String title, @RequestParam String year) {
        return reviewsService.getMovie(title, year);
    }

    @PostMapping("/movie/grade")
    public Grade registerMovieGrade(@RequestBody Grade grade) {
        return reviewsService.registerMovieGrade(grade);
    }
}