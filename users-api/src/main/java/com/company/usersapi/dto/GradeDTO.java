package com.company.usersapi.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GradeDTO {
    private Long id;
    private String userName;

    @NotEmpty(message = "movieId must be provided")
    private String movieId;

    @NotNull(message = "grade must be provided")
    @Range(min = 0, max = 10, message = "grade must be between 0 and 10")
    private Float grade;
}