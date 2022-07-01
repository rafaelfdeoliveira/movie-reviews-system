package com.company.reviewsapi.controller;

import com.company.reviewsapi.dto.CommentaryReplyDTO;
import com.company.reviewsapi.dto.RestPage;
import com.company.reviewsapi.model.CommentaryReply;
import com.company.reviewsapi.service.CommentaryReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commentary/reply")
public class CommentaryReplyController {
    private final CommentaryReplyService commentaryReplyService;

    @GetMapping
    public Mono<RestPage<CommentaryReply>> fetchCommentaryReplies(@RequestParam Long commentaryId, Pageable pageable) {
        return commentaryReplyService.fetchCommentaryReplies(commentaryId, pageable);
    }

    @PostMapping
    public Mono<CommentaryReply> registerCommentaryReply(@RequestBody CommentaryReplyDTO commentaryReplyDTO) {
        return commentaryReplyService.registerCommentaryReply(commentaryReplyDTO);
    }
}