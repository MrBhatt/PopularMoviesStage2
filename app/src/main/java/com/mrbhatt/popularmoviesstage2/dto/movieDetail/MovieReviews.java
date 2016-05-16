package com.mrbhatt.popularmoviesstage2.dto.movieDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by anupambhatt on 16/05/16.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieReviews {
    private String id;

    @JsonProperty("results")
    private ReviewResult[] results;
}
