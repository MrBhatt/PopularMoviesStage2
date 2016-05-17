package com.mrbhatt.popularmoviesstage2.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mrbhatt.popularmoviesstage2.R;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.MovieReviews;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GetMovieReviewsTask extends AsyncTask<String, Void, MovieReviews> {

    private final Context mContext;
    private boolean exceptionOccurred = false;

    public GetMovieReviewsTask(Context context) {
        mContext = context;
    }

    @Override
    protected MovieReviews doInBackground(String... params) {
        try {
            final String url = String.format(params[0], params[1]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(url, MovieReviews.class);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            exceptionOccurred = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieReviews movieReviews) {
        super.onPostExecute(movieReviews);
        if (exceptionOccurred) {
            Toast.makeText(mContext, R.string.exceptionMessageReviews, Toast.LENGTH_SHORT).show();
        }
    }
}
