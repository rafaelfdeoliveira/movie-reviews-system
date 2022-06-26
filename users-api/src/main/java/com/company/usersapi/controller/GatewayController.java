package com.company.usersapi.controller;

import com.company.usersapi.dto.GradeDTO;
import com.company.usersapi.dto.MovieDTO;
import com.company.usersapi.dto.MovieSearchDTO;
import com.company.usersapi.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GatewayController {
    private final GatewayService gatewayService;

    @GetMapping("/movie")
    public Mono<MovieDTO> getMovieByTitle(@NotEmpty @RequestParam String title, @RequestParam(required = false) String year) {
        return gatewayService.getMovieByTitle(title, year);
    }

    @GetMapping("/movie/id")
    public Mono<MovieDTO> getMovieById(@RequestParam String movieId) {
        return gatewayService.getMovieById(movieId);
    }

    @GetMapping("/movie/search")
    public Mono<MovieSearchDTO> getMovieSearch(@RequestParam String title, @RequestParam(required = false) String year) {
        return gatewayService.getMovieSearch(title, year);
    }

    @PostMapping("/grade")
    public Mono<GradeDTO> registerMovieGrade(@RequestHeader(name = "Authorization") String accessToken, @Valid @RequestBody GradeDTO gradeDTO) {
        return gatewayService.registerMovieGrade(accessToken, gradeDTO);
    }
}