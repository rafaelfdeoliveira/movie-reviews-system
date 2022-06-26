package com.company.usersapi.dto;

import com.company.usersapi.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDTO {
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private Boolean enabled;
    private int points;
    private List<String> roles;

    public static UserDTO convert(User user) {
        UserDTO dto = new UserDTO();
        dto.setEnabled(user.getEnabled());
        dto.setUserName(user.getUserName());
        dto.setPoints(user.getPoints());
        dto.setRoles(user.getRoles());
        return dto;
    }
}