package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.CommentaryDTO;
import com.company.reviewsapi.dto.CommentaryDTOPage;
import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.repository.CommentaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class CommentaryService {
    private final CommentaryRepository commentaryRepository;
    private final MovieService movieService;

    public Mono<CommentaryDTOPage> fetchMovieCommentaries(String movieId, Pageable pageable) {
        return Mono.just(movieId)
                .publishOn(Schedulers.boundedElastic())
                .map(id -> getMovieCommentaries(movieId, pageable));
    }

    public Mono<CommentaryDTO> registerMovieCommentary(CommentaryDTO commentaryDTO) {
        return movieService.getMovieById(commentaryDTO.getMovieId())
                .publishOn(Schedulers.boundedElastic())
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieId");
                })
                .map(movieDTO -> this.saveMovieCommentary(commentaryDTO));
    }

    public Mono<CommentaryDTO> deleteMovieCommentary(Long commentaryId) {
        return Mono.just(commentaryId)
                .publishOn(Schedulers.boundedElastic())
                .map(this::getAndDeleteCommentaryById);
    }

    public Commentary getCommentaryById(Long commentaryId) {
        Commentary commentary = commentaryRepository.findById(commentaryId).orElse(null);
        if (commentary == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CommentaryId");
        return commentary;
    }

    private CommentaryDTOPage getMovieCommentaries(String movieId, Pageable pageable) {
        Page<Commentary> commentaryPage = commentaryRepository.findByMovieId(movieId, pageable);
        return CommentaryDTOPage.convertCommentariesPageToCommentaryDTOPage(commentaryPage);
    }

    private CommentaryDTO saveMovieCommentary(CommentaryDTO commentaryDTO) {
        Commentary commentary = commentaryRepository.save(Commentary.convert(commentaryDTO));
        return CommentaryDTO.convert(commentary);
    }

    private CommentaryDTO getAndDeleteCommentaryById(Long commentaryId) {
        Commentary commentary = this.getCommentaryById(commentaryId);
        commentaryRepository.deleteById(commentaryId);
        return CommentaryDTO.convert(commentary);
    }
}