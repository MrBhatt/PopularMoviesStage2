package com.mrbhatt.popularmoviesstage2.dto.movieDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrailerResult {
    private String name;

    private String site;

    private String type;

    private String key;
}
