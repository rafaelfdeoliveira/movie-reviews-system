package com.company.usersapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @NotNull
    @NotBlank
    private String userName;

    @NotNull
    @NotBlank
    private String password;

    public JwtRequest(String username, String password) {
        this.setUserName(username);
        this.setPassword(password);
    }

}