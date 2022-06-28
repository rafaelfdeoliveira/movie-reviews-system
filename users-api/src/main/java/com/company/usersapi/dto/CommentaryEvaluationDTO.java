package com.company.usersapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CommentaryEvaluationDTO {
    private Long id;
    private String userName;

    @NotNull(message = "The commentary evaluation must be provided.")
    private Boolean liked;

    @NotNull(message = "CommentaryId must be provided")
    @Positive(message = "CommentaryId must be a positive integer")
    private Long commentaryId;
}