package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.CommentaryDTO;
import com.company.reviewsapi.dto.RestPage;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.repository.CommentaryRepository;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheEvict;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheable;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCaching;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentaryService {
    private final CommentaryRepository commentaryRepository;
    private final MovieService movieService;

    @ReactiveRedisCacheable(cacheName = "movieCommentaries", key = "#movieId + '_' + #pageable", timeout = 1800)
    public Mono<RestPage<CommentaryDTO>> fetchMovieCommentaries(String movieId, Pageable pageable) {
        return Mono.just(movieId)
                .publishOn(Schedulers.boundedElastic())
                .map(id -> getMovieCommentaries(movieId, pageable))
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
                });
    }

    @ReactiveRedisCacheEvict(cacheName = "'movieCommentaries_' + #commentaryDTO.movieId", allEntries = true)
    public Mono<CommentaryDTO> registerMovieCommentary(CommentaryDTO commentaryDTO) {
        return movieService.getMovieById(commentaryDTO.getMovieId())
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(movieDTO -> this.saveMovieCommentary(commentaryDTO));
    }

    @ReactiveRedisCaching(evict = {
            @ReactiveRedisCacheEvict(cacheName = "movieCommentaries", allEntries = true),
            @ReactiveRedisCacheEvict(cacheName = "commentaryById", key = "#commentaryId.toString()"),
            @ReactiveRedisCacheEvict(cacheName = "'commentaryReplies_' + #commentaryId.toString()", allEntries = true),
            @ReactiveRedisCacheEvict(cacheName = "'commentaryEvaluations_' + #commentaryId.toString()", allEntries = true)
    })
    public Mono<CommentaryDTO> deleteMovieCommentary(Long commentaryId) {
        return Mono.just(commentaryId)
                .publishOn(Schedulers.boundedElastic())
                .map(this::getAndDeleteCommentaryById);
    }

    @ReactiveRedisCacheable(cacheName = "commentaryById", key = "#commentaryId.toString()", timeout = 1800)
    public Commentary getCommentaryById(Long commentaryId) {
        Commentary commentary = commentaryRepository.findById(commentaryId).orElse(null);
        if (commentary == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CommentaryId");
        return commentary;
    }

    private RestPage<CommentaryDTO> getMovieCommentaries(String movieId, Pageable pageable) {
        Page<Commentary> commentaryPage = commentaryRepository.findByMovieId(movieId, pageable);
        return new RestPage<>(
                commentaryPage.getContent().stream().map(CommentaryDTO::convert).collect(Collectors.toList()),
                commentaryPage.getNumber(),
                commentaryPage.getSize(),
                commentaryPage.getTotalElements()
        );
    }

    private CommentaryDTO saveMovieCommentary(CommentaryDTO commentaryDTO) {
        Commentary commentary = commentaryRepository.save(Commentary.convert(commentaryDTO));
        return CommentaryDTO.convert(commentary);
    }

    private CommentaryDTO getAndDeleteCommentaryById(Long commentaryId) {
        Commentary commentary = this.getCommentaryById(commentaryId);
        commentaryRepository.delete(commentary);
        return CommentaryDTO.convert(commentary);
    }
}