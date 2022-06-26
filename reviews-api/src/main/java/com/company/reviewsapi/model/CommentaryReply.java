package com.company.reviewsapi.model;

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
    private int id;

    @Column(name = "username")
    private String userName;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    private Commentary commentary;
}
