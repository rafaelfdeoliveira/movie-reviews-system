package com.company.reviewsapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "commentaries")
public class Commentary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "movieid")
    private String movieId;

    @Column(name = "username")
    private String userName;

    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy = "commentary", cascade = CascadeType.ALL)
    private List<CommentaryReply> commentaryReplies = new ArrayList<>();
}
