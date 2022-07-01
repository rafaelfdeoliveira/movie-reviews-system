package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.RestPage;
import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.repository.GradeRepository;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheEvict;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheable;
import lombok.RequiredArgsConstructor;
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

    @ReactiveRedisCacheable(cacheName = "movieGrades", key = "#movieId + '_' + #pageable", timeout = 1800)
    public Mono<RestPage<Grade>> getMovieGrades(String movieId, Pageable pageable) {
        return Mono.just(movieId)
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
                })
                .map(id -> new RestPage<>(gradeRepository.findByMovieId(id, pageable)));
    }

    @ReactiveRedisCacheEvict(cacheName = "'movieGrades_' + #grade.movieId", allEntries = true)
    public Mono<Grade> registerMovieGrade(Grade grade) {
        return movieService.getMovieById(grade.getMovieId())
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(movieDTO -> gradeRepository.save(grade));
    }
}