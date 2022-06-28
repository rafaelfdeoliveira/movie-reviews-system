package com.company.reviewsapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "commentaries")
public class Commentary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "movie_id")
    private String movieId;

    @Column(name = "text")
    private String text;

    @JsonManagedReference
    @OneToMany(mappedBy = "commentary", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CommentaryReply> commentaryReplies = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "commentary", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CommentaryEvaluation> commentaryEvaluations = new HashSet<>();
}