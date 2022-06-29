package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.CommentaryReplyDTO;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryReply;
import com.company.reviewsapi.repository.CommentaryReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class CommentaryReplyService {
    private final CommentaryReplyRepository commentaryReplyRepository;
    private final CommentaryService commentaryService;

    public Mono<Page<CommentaryReply>> fetchCommentaryReplies(Long commentaryId, Pageable pageable) {
        return Mono.just(commentaryId)
                .publishOn(Schedulers.boundedElastic())
                .map(id -> this.getCommentaryReplies(id, pageable));
    }

    public Mono<CommentaryReply> registerCommentaryReply(CommentaryReplyDTO commentaryReplyDTO) {
        return Mono.just(commentaryReplyDTO)
                .publishOn(Schedulers.boundedElastic())
                .map(this::saveCommentaryReply);
    }

    private Page<CommentaryReply> getCommentaryReplies(Long commentaryId, Pageable pageable) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryId);

        return commentaryReplyRepository.findByCommentary(commentary, pageable);
    }

    private CommentaryReply saveCommentaryReply(CommentaryReplyDTO commentaryReplyDTO) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryReplyDTO.getCommentaryId());

        CommentaryReply commentaryReply = CommentaryReply.convert(commentaryReplyDTO);
        commentaryReply.setCommentary(commentary);

        return commentaryReplyRepository.save(commentaryReply);
    }
}
