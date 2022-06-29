package com.company.usersapi.dto;

import com.company.usersapi.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    @NotNull
    @NotBlank
    private String userName;
    @NotNull
    @NotBlank
    private String password;
    private Boolean enabled;
    private int points;
    private List<String> roles;

    public static UserDTO convert(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserName(user.getUserName());
        dto.setEnabled(user.getEnabled());
        dto.setPoints(user.getPoints());
        dto.setRoles(user.getRoles());
        return dto;
    }
}