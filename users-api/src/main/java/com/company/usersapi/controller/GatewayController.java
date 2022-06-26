package com.company.usersapi.controller;

import com.company.usersapi.dto.GradeDTO;
import com.company.usersapi.dto.MovieDTO;
import com.company.usersapi.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GatewayController {
    private final GatewayService gatewayService;

    @GetMapping("/movie")
    public MovieDTO getMovie(@RequestParam String title, @RequestParam(required = false) String year) {
        return gatewayService.getMovie(title, year);
    }

    @PostMapping("/movie/grade")
    public GradeDTO registerMovieGrade(@RequestHeader(name = "Authorization") String accessToken, @Valid @RequestBody GradeDTO gradeDTO) {
        return gatewayService.registerMovieGrade(accessToken, gradeDTO);
    }
}