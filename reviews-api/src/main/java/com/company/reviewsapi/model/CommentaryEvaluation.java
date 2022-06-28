package com.company.reviewsapi.model;

import com.company.reviewsapi.dto.CommentaryEvaluationDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "commentaryEvaluations")
public class CommentaryEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "liked")
    private Boolean liked;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "commentary_id")
    private Commentary commentary;

    public static CommentaryEvaluation convert(CommentaryEvaluationDTO dto) {
        CommentaryEvaluation commentaryEvaluation = new CommentaryEvaluation();
        commentaryEvaluation.setUserName(dto.getUserName());
        commentaryEvaluation.setLiked(dto.getLiked());

        return commentaryEvaluation;
    }
}
