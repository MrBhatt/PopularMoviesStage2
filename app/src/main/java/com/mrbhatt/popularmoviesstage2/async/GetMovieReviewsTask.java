package com.mrbhatt.popularmoviesstage2.async;

import android.os.AsyncTask;
import android.util.Log;

import com.mrbhatt.popularmoviesstage2.dto.movieDetail.MovieReviews;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.MovieTrailers;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GetMovieReviewsTask extends AsyncTask<String, Void, MovieReviews> {

    @Override
    protected MovieReviews doInBackground(String... params) {
        try {
            final String url = String.format(params[0], params[1]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(url, MovieReviews.class);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }

}
