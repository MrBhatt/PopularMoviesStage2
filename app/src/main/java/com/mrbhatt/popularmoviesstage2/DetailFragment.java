package com.mrbhatt.popularmoviesstage2;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrbhatt.popularmoviesstage2.adapters.ReviewAdapter;
import com.mrbhatt.popularmoviesstage2.adapters.TrailerAdapter;
import com.mrbhatt.popularmoviesstage2.async.GetMovieReviewsTask;
import com.mrbhatt.popularmoviesstage2.async.GetMovieTrailersTask;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.MovieReviews;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.MovieTrailers;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.ReviewResult;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.TrailerResult;
import com.mrbhatt.popularmoviesstage2.dto.popularMovies.PopularMovieResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by anupambhatt on 04/05/16.
 */
public class DetailFragment extends Fragment {

    private String movieId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        getActivity().getActionBar().setTitle(getString(R.string.MovieDetailPageHeading));
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle arguments = this.getArguments();

        setViewValues(rootView, arguments);

        Button button = (Button) rootView.findViewById(R.id.addToFavs);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavourites(arguments);
            }
        });

        return rootView;
    }

    private void setViewValues(View rootView, Bundle arguments) {

        movieId = arguments.getString(getString(R.string.movieId));

        // Get all available view elements
        TextView titleTextView = (TextView) rootView.findViewById(R.id.movieTitle);
        TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.movieReleaseDate);
        ImageView posterImageView = (ImageView) rootView.findViewById(R.id.moviePoster);
        TextView movieRatingTextView = (TextView) rootView.findViewById(R.id.movieRating);
        TextView moviePlotTextView = (TextView) rootView.findViewById(R.id.moviePlot);
        final RecyclerView trailersListView = (RecyclerView) rootView.findViewById(R.id.trailersListView);
        final RecyclerView reviewsListView = (RecyclerView) rootView.findViewById(R.id.reviewsListView);

        if (arguments!= null) {
            // Set all the values retrieved above in to the views
            titleTextView.setText(arguments.getString(getString(R.string.movieTitle)));
            releaseDateTextView.setText(arguments.getString(getString(R.string.movieReleaseDate)));
            Picasso.with(getActivity().getApplicationContext())
                    .load(arguments.getString(getString(R.string.moviePosterPath)))
                    .into(posterImageView);
            movieRatingTextView.setText(arguments.getString(getString(R.string.movieRating)).concat("/10"));
            moviePlotTextView.setText(arguments.getString(getString(R.string.moviePlot)));

            // setup RecyclerView
            setupTrailersView(trailersListView);
            setupReviewsView(reviewsListView);
        }
    }

    private void setupTrailersView(RecyclerView trailersRecyclerView) {
        // Prepare urls for youtube videos (get video keys and append it with base url)
        ArrayList<Spanned> trailerDisplayList = getYouTubeTrailerUrlsList(getTrailerResults(Long.valueOf(movieId)));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(trailersRecyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
        TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity().getApplicationContext(),
                trailerDisplayList);
        trailersRecyclerView.setHasFixedSize(true);
        trailersRecyclerView.setAdapter(trailerAdapter);
    }

    private void setupReviewsView(RecyclerView reviewsRecyclerView) {
        List<ReviewResult> reviewResults = Arrays.asList(getReviewResults(Long.valueOf(movieId)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(reviewsRecyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity().getApplicationContext(),
                reviewResults);
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    private ArrayList<Spanned> getYouTubeTrailerUrlsList(TrailerResult[] trailerResults) {
        String linkPrefix = "<a href=\"https://www.youtube.com/watch?v=%s\">";

        ArrayList<Spanned> trailerDisplayTexts = new ArrayList();

        for (TrailerResult trailerResult : trailerResults) {
            if (trailerResult.getSite().equalsIgnoreCase("YouTube")) {
                trailerDisplayTexts.add(
                        Html.fromHtml(
                                String.format(linkPrefix, trailerResult.getKey())
                                        .concat(trailerResult.getName())
                                        .concat("</a>")));
            }
        }
        return trailerDisplayTexts;
    }

    private TrailerResult[] getTrailerResults(Long movieId) {
        try {
            MovieTrailers movieTrailers = new GetMovieTrailersTask().execute(getResources()
                    .getString(R.string.movieTrailerUrl), Long.toString(movieId)).get();
            return movieTrailers.getResults();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new TrailerResult[0];
    }

    private ReviewResult[] getReviewResults(Long movieId) {
        try {
            MovieReviews movieReviews = new GetMovieReviewsTask().execute(getResources()
                    .getString(R.string.movieReviewUrl), Long.toString(movieId)).get();
            return movieReviews.getResults();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new ReviewResult[0];
    }

    private void addToFavourites(Bundle arguments) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> favouritesSet = prefs.getStringSet(getString(R.string.favouritesList), null);

        if (null == favouritesSet) {
            favouritesSet = new HashSet<>();
        }

        PopularMovieResult popularMovieResult = new PopularMovieResult();
        popularMovieResult.setId(Long.valueOf(arguments.getString(getString(R.string.movieId))));
        popularMovieResult.setOverview(arguments.getString(getString(R.string.moviePlot)));
        popularMovieResult.setPoster_path(arguments.getString(getString(R.string.moviePosterPath)));
        popularMovieResult.setRelease_date(arguments.getString(getString(R.string.movieReleaseDate)));
        popularMovieResult.setTitle(arguments.getString(getString(R.string.movieTitle)));
        popularMovieResult.setVote_average(arguments.getString(getString(R.string.movieRating)));

        ObjectMapper objectMapper = new ObjectMapper();
        String favouriteMovie = null;
        try {
           favouriteMovie = objectMapper.writeValueAsString(popularMovieResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (!favouritesSet.contains(favouriteMovie)) {
            favouritesSet.add(favouriteMovie);
            editor.putStringSet(getString(R.string.favouritesList), favouritesSet);
            editor.apply();
            Toast.makeText(getActivity().getApplicationContext(), R.string.addedToFavourites, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), R.string.alreadyInFavourites, Toast.LENGTH_SHORT).show();
        }
    }
}