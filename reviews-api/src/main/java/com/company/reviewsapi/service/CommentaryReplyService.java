package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.CommentaryReplyDTO;
import com.company.reviewsapi.dto.RestPage;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryReply;
import com.company.reviewsapi.repository.CommentaryReplyRepository;
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
public class CommentaryReplyService {
    private final CommentaryReplyRepository commentaryReplyRepository;
    private final CommentaryService commentaryService;

    @ReactiveRedisCacheable(cacheName = "commentaryReplies", key = "#commentaryId.toString() + '_' + #pageable", timeout = 1800)
    public Mono<RestPage<CommentaryReply>> fetchCommentaryReplies(Long commentaryId, Pageable pageable) {
        return Mono.just(commentaryId)
                .publishOn(Schedulers.boundedElastic())
                .map(id -> this.getCommentaryReplies(id, pageable))
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameter");
                });
    }

    @ReactiveRedisCacheEvict(cacheName = "'commentaryReplies_' + #commentaryReplyDTO.commentaryId.toString()", allEntries = true)
    public Mono<CommentaryReply> registerCommentaryReply(CommentaryReplyDTO commentaryReplyDTO) {
        return Mono.just(commentaryReplyDTO)
                .publishOn(Schedulers.boundedElastic())
                .map(this::saveCommentaryReply);
    }

    private RestPage<CommentaryReply> getCommentaryReplies(Long commentaryId, Pageable pageable) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryId);

        return new RestPage<>(commentaryReplyRepository.findByCommentary(commentary, pageable));
    }

    private CommentaryReply saveCommentaryReply(CommentaryReplyDTO commentaryReplyDTO) {
        Commentary commentary = commentaryService.getCommentaryById(commentaryReplyDTO.getCommentaryId());

        CommentaryReply commentaryReply = CommentaryReply.convert(commentaryReplyDTO);
        commentaryReply.setCommentary(commentary);

        return commentaryReplyRepository.save(commentaryReply);
    }
}
