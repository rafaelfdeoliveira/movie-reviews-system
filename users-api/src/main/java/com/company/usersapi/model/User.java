package com.company.usersapi.model;

import com.company.usersapi.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "points")
    private int points;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Authority> authorities = new ArrayList<>();

    public static User convert(UserDTO dto) {
        User user = new User();
        user.setPoints(dto.getPoints());
        user.setEnabled(dto.getEnabled());
        user.setPassword(dto.getPassword());
        user.setUserName(dto.getUserName());
        return user;
    }

    public List<String> getRoles() {
        return this.getAuthorities()
                .stream()
                .map((authority -> authority.getAuthorityKey().getAuthority()))
                .collect(Collectors.toList());
    }
}