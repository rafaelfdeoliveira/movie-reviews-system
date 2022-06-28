package com.company.usersapi.controller;

import com.company.usersapi.dto.*;
import com.company.usersapi.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GatewayController {
    private final GatewayService gatewayService;

    @GetMapping("/movie/title")
    public Mono<MovieDTO> getMovieByTitle(
            @NotEmpty @RequestParam String title,
            @RequestParam(required = false) String year
    ) {
        return gatewayService.getMovieByTitle(title, year);
    }

    @GetMapping("/movie/id")
    public Mono<MovieDTO> getMovieById(@RequestParam String movieId) {
        return gatewayService.getMovieById(movieId);
    }

    @GetMapping("/movie/search")
    public Mono<MovieSearchDTO> getMovieSearch(
            @NotEmpty @RequestParam String title,
            @RequestParam(required = false) String year,
            @Positive @RequestParam(required = false) Integer page
    ) {
        return gatewayService.getMovieSearch(title, year, page);
    }

    @PostMapping("/grade")
    public Mono<GradeDTO> registerMovieGrade(
            @RequestHeader(name = "Authorization") String accessToken,
            @Valid @RequestBody GradeDTO gradeDTO
    ) {
        return gatewayService.registerMovieGrade(accessToken, gradeDTO);
    }

    @PostMapping("/commentary")
    public Mono<CommentaryDTO> registerMovieCommentary(
            @RequestHeader(name = "Authorization") String accessToken,
            @Valid @RequestBody CommentaryDTO commentaryDTO
    ) {
        return gatewayService.registerMovieCommentary(accessToken, commentaryDTO);
    }

    @PostMapping("/commentary/reply")
    public Mono<CommentaryReplyDTO> registerCommentaryReply(
            @RequestHeader(name = "Authorization") String accessToken,
            @Valid @RequestBody CommentaryReplyDTO commentaryReplyDTO
    ) {
        return gatewayService.registerCommentaryReply(accessToken, commentaryReplyDTO);
    }

    @PostMapping("/commentary/evaluation")
    public Mono<CommentaryEvaluationDTO> registerCommentaryEvaluation(
        @RequestHeader(name = "Authorization") String accessToken,
        @Valid @RequestBody CommentaryEvaluationDTO commentaryEvaluationDTO
    ) {
        return gatewayService.registerCommentaryEvaluation(accessToken, commentaryEvaluationDTO);
    }
}