package com.company.reviewsapi.dto;

import com.company.reviewsapi.model.Commentary;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentaryDTO {
    private Long id;
    private String userName;
    private String movieId;
    private String text;

    public static CommentaryDTO convert(Commentary commentary) {
        CommentaryDTO dto = new CommentaryDTO();
        dto.setId(commentary.getId());
        dto.setUserName(commentary.getUserName());
        dto.setMovieId(commentary.getMovieId());
        dto.setText(commentary.getText());
        return dto;
    }
}