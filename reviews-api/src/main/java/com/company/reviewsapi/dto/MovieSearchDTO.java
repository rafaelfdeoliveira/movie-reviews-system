package com.company.reviewsapi.dto;

import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.Grade;

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

        public List<Commentary> commentaries;
        public List<Grade> grades;
    }
}