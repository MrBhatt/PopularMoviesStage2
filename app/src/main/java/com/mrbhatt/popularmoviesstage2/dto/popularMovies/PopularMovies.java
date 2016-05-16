package com.mrbhatt.popularmoviesstage2.dto.popularMovies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularMovies {

    @JsonProperty("results")
    private PopularMovieResult[] popularMovieResults;
}
