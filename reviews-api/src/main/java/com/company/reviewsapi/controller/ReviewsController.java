package com.company.reviewsapi.controller;

import com.company.reviewsapi.dto.CommentaryEvaluationDTO;
import com.company.reviewsapi.dto.CommentaryReplyDTO;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryEvaluation;
import com.company.reviewsapi.model.CommentaryReply;
import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    @PostMapping("/grade")
    public Mono<Grade> registerMovieGrade(@RequestBody Grade grade) {
        return reviewsService.registerMovieGrade(grade);
    }

    @PostMapping("/commentary")
    public Mono<Commentary> registerMovieCommentary(@RequestBody Commentary commentary) {
        return reviewsService.registerMovieCommentary(commentary);
    }

    @PostMapping("/commentary/reply")
    public Mono<CommentaryReply> registerCommentaryReply(@RequestBody CommentaryReplyDTO commentaryReplyDTO) {
        return reviewsService.registerCommentaryReply(commentaryReplyDTO);
    }

    @PostMapping("/commentary/evaluation")
    public Mono<CommentaryEvaluation> registerCommentaryEvaluation(@RequestBody CommentaryEvaluationDTO commentaryEvaluationDTO) {
        return reviewsService.registerCommentaryEvaluation(commentaryEvaluationDTO);
    }
}