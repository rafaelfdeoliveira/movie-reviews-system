package com.company.reviewsapi.model;

import com.company.reviewsapi.dto.CommentaryReplyDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "commentaryReplies")
public class CommentaryReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "text")
    private String text;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "commentary_id")
    private Commentary commentary;

    public static CommentaryReply convert(CommentaryReplyDTO dto) {
        CommentaryReply commentaryReply = new CommentaryReply();
        commentaryReply.setUserName(dto.getUserName());
        commentaryReply.setText(dto.getText());

        return commentaryReply;
    }
}