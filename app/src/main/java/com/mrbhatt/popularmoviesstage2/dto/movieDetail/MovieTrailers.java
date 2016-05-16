package com.mrbhatt.popularmoviesstage2.dto.movieDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieTrailers {
    private String id;

    @JsonProperty("results")
    private TrailerResult[] results;
}
