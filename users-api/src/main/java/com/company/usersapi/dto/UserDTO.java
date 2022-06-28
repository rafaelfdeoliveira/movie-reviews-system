package com.company.usersapi.dto;

import com.company.usersapi.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    @NotEmpty
    private String userName;
    @NotEmpty
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