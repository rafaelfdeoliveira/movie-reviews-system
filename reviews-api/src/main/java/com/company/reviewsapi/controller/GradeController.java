package com.company.reviewsapi.controller;

import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;

    @GetMapping
    public Mono<Page<Grade>> getMovieGrades(@RequestParam String movieId, Pageable pageable) {
        return gradeService.getMovieGrades(movieId, pageable);
    }

    @PostMapping
    public Mono<Grade> registerMovieGrade(@RequestBody Grade grade) {
        return gradeService.registerMovieGrade(grade);
    }
}