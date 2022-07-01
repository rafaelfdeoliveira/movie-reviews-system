package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.CommentaryEvaluationDTO;
import com.company.reviewsapi.dto.RestPage;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryEvaluation;
import com.company.reviewsapi.repository.CommentaryEvaluationRepository;
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
public class CommentaryEvaluationService {
    private final CommentaryEvaluationRepository commentaryEvaluationRepository;
    private final CommentaryService commentaryService;

    @ReactiveRedisCacheable(cacheName = "commentaryEvaluations", key = "#commentaryId.toString() + '_' + #pageable", timeout = 1800)
    public Mono<RestPage<CommentaryEvaluation>> fetchCommentaryEvaluations(Long commentaryId, Pageable pageable) {
        return Mono.just(commentaryId)
                .publishOn(Schedulers.boundedElastic())
                .map(id -> this.getCommentaryEvaluations(id, pageable))
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
                });
    }

    @ReactiveRedisCacheEvict(cacheName = "'commentaryEvaluations_' + #commentaryEvaluationDTO.commentaryId.toString()", allEntries = true)
    public Mono<CommentaryEvaluation> registerCommentaryEvaluation(CommentaryEvaluationDTO commentaryEvaluationDTO) {
        return Mono.just(commentaryEvaluationDTO)
                .publishOn(Schedulers.boundedElastic())
                .map(this::saveCommentaryEvaluation);
    }

    private RestPage<CommentaryEvaluation> getCommentaryEvaluations(Long commentaryId, Pageable pageable) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryId);

        return new RestPage<>(commentaryEvaluationRepository.findByCommentary(commentary, pageable));
    }

    private CommentaryEvaluation saveCommentaryEvaluation(CommentaryEvaluationDTO commentaryEvaluationDTO) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryEvaluationDTO.getCommentaryId());

        CommentaryEvaluation commentaryEvaluation = CommentaryEvaluation.convert(commentaryEvaluationDTO);
        commentaryEvaluation.setCommentary(commentary);

        return commentaryEvaluationRepository.save(commentaryEvaluation);
    }
}