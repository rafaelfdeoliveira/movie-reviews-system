package com.company.reviewsapi.service;

import com.company.reviewsapi.dto.MovieDTO;
import com.company.reviewsapi.model.Grade;
import com.company.reviewsapi.repository.CommentaryRepository;
import com.company.reviewsapi.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final WebClient omdbApiClient;
    private final CommentaryRepository commentaryRepository;
    private final GradeRepository gradeRepository;

    @Value("${omdbApiKey}")
    private String omdbApiKey;

    public MovieDTO getMovie(String title, String year) {
        MovieDTO movieDTO = omdbApiClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", omdbApiKey)
                        .queryParam("t", title)
                        .queryParam("y", year)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block();

        if (movieDTO == null || movieDTO.Response == null || movieDTO.Response.equals("False")) {
            return new MovieDTO("False");
        }
        movieDTO.commentaries = commentaryRepository.findByMovieId(movieDTO.imdbID);
        movieDTO.grades = gradeRepository.findByMovieId(movieDTO.imdbID);

        return movieDTO;
    }

    public Grade registerMovieGrade(Grade grade) {
        return gradeRepository.save(grade);
    }
}