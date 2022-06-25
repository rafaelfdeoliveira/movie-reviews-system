package com.company.reviewsapi.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private Boolean enabled;
    private List<String> roles;
}