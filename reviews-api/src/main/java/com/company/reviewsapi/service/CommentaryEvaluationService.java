package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.CommentaryEvaluationDTO;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryEvaluation;
import com.company.reviewsapi.repository.CommentaryEvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class CommentaryEvaluationService {
    private final CommentaryEvaluationRepository commentaryEvaluationRepository;
    private final CommentaryService commentaryService;

    public Mono<Page<CommentaryEvaluation>> fetchCommentaryEvaluations(Long commentaryId, Pageable pageable) {
        return Mono.just(commentaryId)
                .publishOn(Schedulers.boundedElastic())
                .map(id -> this.getCommentaryEvaluations(id, pageable));
    }

    public Mono<CommentaryEvaluation> registerCommentaryEvaluation(CommentaryEvaluationDTO commentaryEvaluationDTO) {
        return Mono.just(commentaryEvaluationDTO)
                .publishOn(Schedulers.boundedElastic())
                .map(this::saveCommentaryEvaluation);
    }

    private Page<CommentaryEvaluation> getCommentaryEvaluations(Long commentaryId, Pageable pageable) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryId);

        return commentaryEvaluationRepository.findByCommentary(commentary, pageable);
    }

    private CommentaryEvaluation saveCommentaryEvaluation(CommentaryEvaluationDTO commentaryEvaluationDTO) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryEvaluationDTO.getCommentaryId());

        CommentaryEvaluation commentaryEvaluation = CommentaryEvaluation.convert(commentaryEvaluationDTO);
        commentaryEvaluation.setCommentary(commentary);

        return commentaryEvaluationRepository.save(commentaryEvaluation);
    }
}