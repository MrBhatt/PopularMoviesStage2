package com.mrbhatt.popularmoviesstage2.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mrbhatt.popularmoviesstage2.R;
import com.mrbhatt.popularmoviesstage2.dto.popularMovies.PopularMovies;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GetPopularMoviesTask extends AsyncTask<String, Void, PopularMovies> {

    private final Context mContext;
    private boolean exceptionOccurred = false;

    public GetPopularMoviesTask(Context context) {
        mContext = context;
    }

    @Override
    protected PopularMovies doInBackground(String... params) {
        try {
            final String url = String.format(params[0]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(url, PopularMovies.class);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            exceptionOccurred = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(PopularMovies popularMovies) {
        super.onPostExecute(popularMovies);
        if (exceptionOccurred) {
            Toast.makeText(mContext, R.string.exceptionMessagePopular, Toast.LENGTH_SHORT).show();
        }
    }
}