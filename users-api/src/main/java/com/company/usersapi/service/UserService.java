package com.company.usersapi.service;

import com.company.usersapi.config.JwtTokenUtil;
import com.company.usersapi.dto.RestPage;
import com.company.usersapi.dto.UserDTO;
import com.company.usersapi.model.*;
import com.company.usersapi.repository.AuthorityRepository;
import com.company.usersapi.repository.UserRepository;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheEvict;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @ReactiveRedisCacheEvict(cacheName = "userByUserName", key = "#userDTO.userName")
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        return Mono.just(userDTO)
                .publishOn(Schedulers.boundedElastic())
                .map(this::saveUser);
    }

    public Mono<ResponseEntity<JwtResponse>> fetchAuthenticationToken(JwtRequest authenticationRequest) {
        return Mono.just(authenticationRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(this::getAuthenticationToken);
    }

    public Mono<UserDTO> fetchLoggedUser(String accessToken) {
        return Mono.just(accessToken)
                .publishOn(Schedulers.boundedElastic())
                .map(this::getLoggedUser);
    }

    public Mono<RestPage<UserDTO>> fetchAllUsers(Pageable pageable) {
        return Mono.just(pageable)
                .publishOn(Schedulers.boundedElastic())
                .map(this::getAllUsers)
                .onErrorMap(err -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
                });
    }

    public Mono<UserDTO> fetchUserByUserName(String userName) {
        return Mono.just(userName)
                .publishOn(Schedulers.boundedElastic())
                .map(this::getUserDTOByUserName);
    }

    @ReactiveRedisCacheEvict(cacheName = "userByUserName", key = "#userName")
    public Mono<UserDTO> makeUserAdmin(String userName) {
        return Mono.just(userName)
                .publishOn(Schedulers.boundedElastic())
                .map(this::giveUserAdminAuthority);
    }

    @ReactiveRedisCacheable(cacheName = "userByUserName", key = "#userName", timeout = 1800)
    protected User getUserByUserName(String userName) {
        return userRepository.findById(userName).orElse(null);
    }

    private UserDTO saveUser(UserDTO userDTO) {
        User userWithSameUserName = this.getUserByUserName(userDTO.getUserName());
        if (userWithSameUserName != null) throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "UserName already exists");
        if (userDTO.getPassword() == null) throw new ResponseStatusException(HttpStatus.PARTIAL_CONTENT, "A password must be provided");

        userDTO.setEnabled(true);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setPoints(0);
        User user = User.convert(userDTO);
        User userDB = userRepository.save(user);

        Authority authority = authorityRepository.save(Authority.convert(userDB, "LEITOR"));
        userDB.setAuthorities(List.of(authority));
        return UserDTO.convert(userDB);
    }

    private ResponseEntity<JwtResponse> getAuthenticationToken(JwtRequest authenticationRequest) {
        this.authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private UserDTO getLoggedUser(String accessToken) {
        String userName = jwtTokenUtil.getUsernameFromToken(accessToken.substring(7));
        User user = this.getUserByUserName(userName);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LOOGED USER NOT FOUND");
        return UserDTO.convert(user);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "USER DISABLED");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "INVALID CREDENTIALS");
        }
    }

    private RestPage<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return new RestPage<>(
                usersPage.getContent().stream().map(UserDTO::convert).collect(Collectors.toList()),
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements()
        );
    }

    private UserDTO getUserDTOByUserName(String userName) {
        User user = this.getUserByUserName(userName);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with the provided userName was found.");
        return UserDTO.convert(user);
    }

    private UserDTO giveUserAdminAuthority(String userName) {
        User user = this.getUserByUserName(userName);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with the provided userName was found.");
        List<String> currentUserRoles = user.getRoles();
        List.of("BÁSICO", "AVANÇADO", "MODERADOR").forEach(authorityName -> {
            if (currentUserRoles.contains(authorityName)) return;
            Authority authority = new Authority();
            authority.setAuthorityKey(new AuthorityKey(userName, authorityName));
            authority.setUser(user);
            authorityRepository.save(authority);
        });

        UserDTO userDTO = UserDTO.convert(user);
        userDTO.setRoles(List.of("LEITOR", "BÁSICO", "AVANÇADO", "MODERADOR"));
        return userDTO;
    }
}