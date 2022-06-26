package com.company.usersapi.dto;

import java.util.List;

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
        public String userName;
        public String text;
        public List<CommentaryReply> commentaryReplies;
    }

    public static class CommentaryReply {
        public String userName;
        public String text;
    }

    public static class Grade {
        public String userName;
        public Float grade;
    }

    public static class Rating {
        public String Source;
        public String Value;
    }
}