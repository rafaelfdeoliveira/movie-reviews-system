package com.company.reviewsapi.dto;

import com.company.reviewsapi.model.CommentaryEvaluation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentaryEvaluationDTO {
    private Long id;
    private String userName;
    private Boolean liked;
    private Long commentaryId;

    public static CommentaryEvaluationDTO convert(CommentaryEvaluation commentaryEvaluation) {
        CommentaryEvaluationDTO dto = new CommentaryEvaluationDTO();
        dto.setId(commentaryEvaluation.getId());
        dto.setUserName(commentaryEvaluation.getUserName());
        dto.setLiked(commentaryEvaluation.getLiked());

        return dto;
    }
}
