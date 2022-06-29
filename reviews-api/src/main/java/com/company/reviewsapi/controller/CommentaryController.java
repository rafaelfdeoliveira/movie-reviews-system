package com.company.reviewsapi.controller;

import com.company.reviewsapi.dto.CommentaryDTO;
import com.company.reviewsapi.dto.CommentaryDTOPage;
import com.company.reviewsapi.service.CommentaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commentary")
public class CommentaryController {
    private final CommentaryService commentaryService;

    @GetMapping
    public Mono<CommentaryDTOPage> fetchMovieCommentaries(@RequestParam String movieId, Pageable pageable) {
        return commentaryService.fetchMovieCommentaries(movieId, pageable);
    }

    @PostMapping
    public Mono<CommentaryDTO> registerMovieCommentary(@RequestBody CommentaryDTO commentaryDTO) {
        return commentaryService.registerMovieCommentary(commentaryDTO);
    }

    @DeleteMapping
    public Mono<CommentaryDTO> deleteMovieCommentary(@RequestParam Long commentaryId) {
        return commentaryService.deleteMovieCommentary(commentaryId);
    }
}