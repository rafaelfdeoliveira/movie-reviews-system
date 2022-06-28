package com.company.usersapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CommentaryReplyDTO {
    private Long id;
    private String userName;

    @NotEmpty(message = "Commentary reply text must not be empty")
    private String text;

    @NotNull(message = "CommentaryId must be provided")
    @Positive(message = "CommentaryId must be a positive integer")
    private Long commentaryId;
}