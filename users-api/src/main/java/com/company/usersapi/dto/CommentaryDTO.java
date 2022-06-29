package com.company.usersapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CommentaryDTO {
    private Long id;
    private String userName;

    @NotEmpty(message = "movieId must be provided")
    private String movieId;

    @NotEmpty(message = "Commentary text must not be empty")
    private String text;
}