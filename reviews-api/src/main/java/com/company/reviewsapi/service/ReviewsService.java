package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.CommentaryEvaluationDTO;
import com.company.reviewsapi.dto.CommentaryReplyDTO;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryEvaluation;
import com.company.reviewsapi.model.CommentaryReply;
import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.repository.CommentaryEvaluationRepository;
import com.company.reviewsapi.repository.CommentaryReplyRepository;
import com.company.reviewsapi.repository.CommentaryRepository;
import com.company.reviewsapi.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final CommentaryRepository commentaryRepository;
    private final CommentaryReplyRepository commentaryReplyRepository;
    private final CommentaryEvaluationRepository commentaryEvaluationRepository;
    private final GradeRepository gradeRepository;
    private final MovieService movieService;

    public Mono<Grade> registerMovieGrade(Grade grade) {
        return movieService.fetchMovieDataById(grade.getMovieId())
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(movieDTO -> gradeRepository.save(grade));
    }

    public Mono<Commentary> registerMovieCommentary(Commentary commentary) {
        return movieService.fetchMovieDataById(commentary.getMovieId())
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(movieDTO -> commentaryRepository.save(commentary));
    }

    public Mono<CommentaryReply> registerCommentaryReply(CommentaryReplyDTO commentaryReplyDTO) {
        return Mono.just(commentaryReplyDTO)
                .publishOn(Schedulers.boundedElastic())
                .map(this::saveCommentaryReply);
    }

    public Mono<CommentaryEvaluation> registerCommentaryEvaluation(CommentaryEvaluationDTO commentaryEvaluationDTO) {
        return Mono.just(commentaryEvaluationDTO)
                .publishOn(Schedulers.boundedElastic())
                .map(this::saveCommentaryEvaluation);
    }

    private CommentaryReply saveCommentaryReply(CommentaryReplyDTO commentaryReplyDTO) {
        Commentary commentary = this.getCommentaryById(commentaryReplyDTO.getCommentaryId());

        CommentaryReply commentaryReply = CommentaryReply.convert(commentaryReplyDTO);
        commentaryReply.setCommentary(commentary);

        return commentaryReplyRepository.save(commentaryReply);
    }

    private CommentaryEvaluation saveCommentaryEvaluation(CommentaryEvaluationDTO commentaryEvaluationDTO) {
        Commentary commentary = this.getCommentaryById(commentaryEvaluationDTO.getCommentaryId());

        CommentaryEvaluation commentaryEvaluation = CommentaryEvaluation.convert(commentaryEvaluationDTO);
        commentaryEvaluation.setCommentary(commentary);

        return commentaryEvaluationRepository.save(commentaryEvaluation);
    }

    private Commentary getCommentaryById(Long commentaryId) {
        Commentary commentary = commentaryRepository.findById(commentaryId).orElse(null);
        if (commentary == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CommentaryId");
        return commentary;
    }
}