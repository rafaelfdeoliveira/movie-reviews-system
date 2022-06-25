package com.company.reviewsapi.controller;


import com.company.reviewsapi.dto.JwtRequest;
import com.company.reviewsapi.dto.JwtResponse;
import com.company.reviewsapi.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/sign_up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO signUp(@Valid @RequestBody UserDTO userDTO) {

    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> getAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

    }
}