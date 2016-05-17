package com.mrbhatt.popularmoviesstage2.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mrbhatt.popularmoviesstage2.R;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.MovieTrailers;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GetMovieTrailersTask extends AsyncTask<String, Void, MovieTrailers> {

    private final Context mContext;
    private boolean exceptionOccurred = false;

    public GetMovieTrailersTask(Context context) {
        mContext = context;
    }

    @Override
    protected MovieTrailers doInBackground(String... params) {
        try {
            final String url = String.format(params[0], params[1]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(url, MovieTrailers.class);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            exceptionOccurred = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieTrailers movieTrailers) {
        super.onPostExecute(movieTrailers);
        if (exceptionOccurred) {
            Toast.makeText(mContext, R.string.exceptionMessageTrailers, Toast.LENGTH_SHORT).show();
        }
    }
}
