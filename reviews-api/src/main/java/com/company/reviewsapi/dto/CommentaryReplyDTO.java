package com.company.reviewsapi.dto;

import com.company.reviewsapi.model.CommentaryReply;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentaryReplyDTO {
    private Long id;
    private String userName;
    private String text;
    private Long commentaryId;

    public static CommentaryReplyDTO convert(CommentaryReply commentaryReply) {
        CommentaryReplyDTO dto = new CommentaryReplyDTO();
        dto.setId(commentaryReply.getId());
        dto.setUserName(commentaryReply.getUserName());
        dto.setText(commentaryReply.getText());

        return dto;
    }
}