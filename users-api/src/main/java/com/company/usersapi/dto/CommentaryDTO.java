package com.company.usersapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
public class CommentaryDTO {
    private Long id;
    private String userName;

    @NotEmpty(message = "movieId must be provided")
    private String movieId;

    @NotEmpty(message = "Commentary text must not be empty")
    private String text;

    private Set<CommentaryReply> commentaryReplies;
    private Set<CommentaryEvaluation> commentaryEvaluations;

    public static class CommentaryReply {
        public Long id;
        public String userName;
        public String text;
    }

    public static class CommentaryEvaluation {
        public Long id;
        public String userName;
        public Boolean liked;
    }
}