package com.company.usersapi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String userName;
    private String password;

    public JwtRequest(String username, String password) {
        this.setUserName(username);
        this.setPassword(password);
    }

}