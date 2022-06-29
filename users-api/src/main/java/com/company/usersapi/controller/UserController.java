package com.company.usersapi.controller;

import com.company.usersapi.dto.UserDTO;
import com.company.usersapi.model.JwtRequest;
import com.company.usersapi.model.JwtResponse;
import com.company.usersapi.dto.UserDTOPageData;
import com.company.usersapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @PostMapping("/sign_up")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<JwtResponse>> fetchAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest) {
        return userService.fetchAuthenticationToken(authenticationRequest);
    }

    @GetMapping("/user")
    public Mono<UserDTO> fetchLoggedUser(@RequestHeader(name = "Authorization") String accessToken) {
        return userService.fetchLoggedUser(accessToken);
    }

    @GetMapping("/user/all")
    public Mono<UserDTOPageData> fetchAllUsers(Pageable pageable) {
        return userService.fetchAllUsers(pageable);
    }

    @GetMapping("/user/find")
    public Mono<UserDTO> fetchUser(@NotNull @NotBlank @RequestParam String userName) {
        return userService.fetchUserByUserName(userName);
    }

    @PatchMapping("/user/admin")
    public Mono<UserDTO> makeUserAdmin(@NotNull @NotBlank @RequestParam String userName) {
        return userService.makeUserAdmin(userName);
    }
}