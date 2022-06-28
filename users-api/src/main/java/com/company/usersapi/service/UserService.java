package com.company.usersapi.service;

import com.company.usersapi.config.JwtTokenUtil;
import com.company.usersapi.dto.UserDTO;
import com.company.usersapi.model.Authority;
import com.company.usersapi.model.JwtRequest;
import com.company.usersapi.model.JwtResponse;
import com.company.usersapi.model.User;
import com.company.usersapi.repository.AuthorityRepository;
import com.company.usersapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public UserDTO createUser(UserDTO userDTO) {
        User userWithSameUserName = userRepository.findById(userDTO.getUserName()).orElse(null);
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

    public ResponseEntity<JwtResponse> getAuthenticationToken(JwtRequest authenticationRequest) {
        this.authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public UserDTO getLoggedUser(String accessToken) {
        String userName = jwtTokenUtil.getUsernameFromToken(accessToken.substring(7));
        User user = userRepository.findById(userName).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LOGGED USER NOT FOUND");
        return UserDTO.convert(user);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "INVALID_CREDENTIALS");
        }
    }
}