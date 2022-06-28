package com.company.usersapi.dto;

import java.util.List;
import java.util.Set;

public class MovieDTO {
    public String imdbID;
    public String Title;
    public String Year;
    public String Rated;
    public String Released;
    public String Runtime;
    public String Genre;
    public String Director;
    public String Writer;
    public String Actors;
    public String Plot;
    public String Language;
    public String Country;
    public String Awards;
    public String Poster;
    public List<Rating> Ratings;
    public String Metascore;
    public String imdbRating;
    public String imdbVotes;
    public String Type;
    public String DVD;
    public String BoxOffice;
    public String Production;
    public String Website;
    public String Response;

    public List<Commentary> commentaries;
    public List<Grade> grades;

    public static class Commentary {
        public Long id;
        public String userName;
        public String text;
        public Set<CommentaryReply> commentaryReplies;
        public Set<CommentaryEvaluation> commentaryEvaluations;
    }

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

    public static class Grade {
        public Long id;
        public String userName;
        public Float grade;
    }

    public static class Rating {
        public String Source;
        public String Value;
    }
}