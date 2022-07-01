package com.company.reviewsapi.controller;

import com.company.reviewsapi.dto.CommentaryEvaluationDTO;
import com.company.reviewsapi.dto.RestPage;
import com.company.reviewsapi.model.CommentaryEvaluation;
import com.company.reviewsapi.service.CommentaryEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commentary/evaluation")
public class CommentaryEvaluationController {
    private final CommentaryEvaluationService commentaryEvaluationService;

    @GetMapping
    public Mono<RestPage<CommentaryEvaluation>> fetchCommentaryEvaluations(@RequestParam Long commentaryId, Pageable pageable) {
        return commentaryEvaluationService.fetchCommentaryEvaluations(commentaryId, pageable);
    }

    @PostMapping
    public Mono<CommentaryEvaluation> registerCommentaryEvaluation(@RequestBody CommentaryEvaluationDTO commentaryEvaluationDTO) {
        return commentaryEvaluationService.registerCommentaryEvaluation(commentaryEvaluationDTO);
    }
}