package com.mrbhatt.popularmoviesstage2.dto.movieDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Created by anupambhatt on 16/05/16.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResult {
    private String author;
    private String content;
}