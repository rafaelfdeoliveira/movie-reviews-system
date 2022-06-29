package com.company.reviewsapi.service;

import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;
    private final MovieService movieService;

    public Mono<Page<Grade>> getMovieGrades(String movieId, Pageable pageable) {
        return Mono.just(movieId)
                .publishOn(Schedulers.boundedElastic())
                .map(id -> gradeRepository.findByMovieId(movieId, pageable));
    }

    public Mono<Grade> registerMovieGrade(Grade grade) {
        return movieService.getMovieById(grade.getMovieId())
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(movieDTO -> gradeRepository.save(grade));
    }
}