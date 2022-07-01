package com.company.usersapi.service;

import com.company.usersapi.model.User;
import com.company.usersapi.repository.UserRepository;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @ReactiveRedisCacheable(cacheName = "userDetailsByUserName", key = "#userName", timeout = 1800)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findById(userName).orElse(null);
        if (user == null) throw new UsernameNotFoundException("User not found with userName: " + userName);

        return new org.springframework.security.core.userdetails.User(userName, user.getPassword(), user.getAuthorities());
    }
}