package com.company.usersapi.controller;

import com.company.usersapi.dto.UserDTO;
import com.company.usersapi.model.JwtRequest;
import com.company.usersapi.model.JwtResponse;
import com.company.usersapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @PostMapping("/sign_up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO saveUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> getAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        return userService.getAuthenticationToken(authenticationRequest);
    }
}