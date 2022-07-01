package com.company.usersapi.controller;

import com.company.usersapi.config.JwtTokenUtil;
import com.company.usersapi.dto.*;
import com.company.usersapi.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GatewayController {
    private final GatewayService gatewayService;
    private final JwtTokenUtil jwtTokenUtil;

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

    @GetMapping("/grade")
    public Mono<RestPage<GradeDTO>> getMovieGrades(@RequestParam String movieId, Pageable pageable) {
        return gatewayService.getMovieGrades(movieId, pageable);
    }

    @PostMapping("/grade")
    public Mono<GradeDTO> registerMovieGrade(
            @RequestHeader(name = "Authorization") String accessToken,
            @Valid @RequestBody GradeDTO gradeDTO
    ) {
        String userName = this.getUserNameFromAccessToken(accessToken);
        return gatewayService.registerMovieGrade(userName, gradeDTO);
    }

    @GetMapping("/commentary")
    public Mono<RestPage<CommentaryDTO>> getMovieCommentaries(@RequestParam String movieId, Pageable pageable) {
        return gatewayService.getMovieCommentaries(movieId, pageable);
    }

    @PostMapping("/commentary")
    public Mono<CommentaryDTO> registerMovieCommentary(
            @RequestHeader(name = "Authorization") String accessToken,
            @Valid @RequestBody CommentaryDTO commentaryDTO
    ) {
        String userName = this.getUserNameFromAccessToken(accessToken);
        return gatewayService.registerMovieCommentary(userName, commentaryDTO);
    }

    @DeleteMapping("/commentary")
    public Mono<CommentaryDTO> deleteMovieCommentary(@NotNull @Positive @RequestParam Long commentaryId) {
        return gatewayService.deleteMovieCommentary(commentaryId);
    }

    @GetMapping("/commentary/reply")
    public Mono<RestPage<CommentaryReplyDTO>> getCommentaryReplies(@NotNull @Positive @RequestParam Long commentaryId, Pageable pageable) {
        return gatewayService.getCommentaryReplies(commentaryId, pageable);
    }

    @PostMapping("/commentary/reply")
    public Mono<CommentaryReplyDTO> registerCommentaryReply(
            @RequestHeader(name = "Authorization") String accessToken,
            @Valid @RequestBody CommentaryReplyDTO commentaryReplyDTO
    ) {
        String userName = this.getUserNameFromAccessToken(accessToken);
        return gatewayService.registerCommentaryReply(userName, commentaryReplyDTO);
    }

    @GetMapping("/commentary/evaluation")
    public Mono<RestPage<CommentaryEvaluationDTO>> getCommentaryEvaluations(@NotNull @Positive @RequestParam Long commentaryId, Pageable pageable) {
        return gatewayService.getCommentaryEvaluations(commentaryId, pageable);
    }

    @PostMapping("/commentary/evaluation")
    public Mono<CommentaryEvaluationDTO> registerCommentaryEvaluation(
        @RequestHeader(name = "Authorization") String accessToken,
        @Valid @RequestBody CommentaryEvaluationDTO commentaryEvaluationDTO
    ) {
        String userName = this.getUserNameFromAccessToken(accessToken);
        return gatewayService.registerCommentaryEvaluation(userName, commentaryEvaluationDTO);
    }

    private String getUserNameFromAccessToken(String accessToken) {
        return jwtTokenUtil.getUsernameFromToken(accessToken.substring(7));
    }
}