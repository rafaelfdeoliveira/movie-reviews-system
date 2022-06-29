package com.company.reviewsapi.model;

import com.company.reviewsapi.dto.CommentaryDTO;
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
    @OneToMany(mappedBy = "commentary", cascade = CascadeType.ALL)
    private Set<CommentaryReply> commentaryReplies = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "commentary", cascade = CascadeType.ALL)
    private Set<CommentaryEvaluation> commentaryEvaluations = new HashSet<>();

    public static Commentary convert(CommentaryDTO dto) {
        Commentary commentary = new Commentary();
        commentary.setUserName(dto.getUserName());
        commentary.setMovieId(dto.getMovieId());
        commentary.setText(dto.getText());
        return commentary;
    }
}