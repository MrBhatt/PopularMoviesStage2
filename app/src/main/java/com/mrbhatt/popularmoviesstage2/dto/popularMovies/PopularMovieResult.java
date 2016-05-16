package com.mrbhatt.popularmoviesstage2.dto.popularMovies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularMovieResult {
    private long id;

    private String vote_average;

    private String title;

    private String overview;

    private String release_date;

    private String poster_path;
}
