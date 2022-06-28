package com.company.usersapi.dto;

import java.util.List;

public class MovieSearchDTO {
    public List<SimpleMovieDTO> Search;
    public String totalResults;
    public String Response;


    public static class SimpleMovieDTO {
        public String imdbID;
        public String Title;
        public String Year;
        public String Type;
        public String Poster;

        public List<MovieDTO.Commentary> commentaries;
        public List<MovieDTO.Grade> grades;
    }
}
