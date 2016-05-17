package com.mrbhatt.popularmoviesstage2;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrbhatt.popularmoviesstage2.async.GetPopularMoviesTask;
import com.mrbhatt.popularmoviesstage2.dto.popularMovies.PopularMovieResult;
import com.mrbhatt.popularmoviesstage2.dto.popularMovies.PopularMovies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anupambhatt on 04/05/16.
 */
public class MasterFragment extends Fragment {

    @BindView(R.id.gridview)
    GridView gridview;

    private String currentSortPreference = null;
    private PopularMovieResult[] results = null;
    private ImageAdapter imageAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.master_fragment, container, false);
        ButterKnife.bind(this, rootView);

        ActionBar actionBar = getActivity().getActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        imageAdapter = new ImageAdapter(getActivity().getApplicationContext(), getPosters());
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopularMovieResult result = results[position];

                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.movieId), Long.toString(result.getId()));
                bundle.putString(getString(R.string.movieTitle), result.getTitle());
                bundle.putString(getString(R.string.movieReleaseDate), result.getRelease_date());
                bundle.putString(getString(R.string.moviePosterPath),
                        String.format(getString(R.string.posterImageUrl), result.getPoster_path()));
                bundle.putString(getString(R.string.movieRating), result.getVote_average());
                bundle.putString(getString(R.string.moviePlot), result.getOverview());

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                if (getActivity().findViewById(R.id.detailFrame) != null) {
                    fragmentTransaction.replace(R.id.detailFrame, detailFragment);
                } else {
                    fragmentTransaction.replace(R.id.masterFrame, detailFragment);
                }

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return rootView;
    }

    private List<String> getPosters() {
        try {
            String url = null;
            boolean isFavourite = false;
            currentSortPreference = getSortPreference();
            /* Select based on preference */
            switch (currentSortPreference.toLowerCase()) {
                case "popularity":
                    url = getString(R.string.popularMovieUrl);
                    break;
                case "top_rated":
                    url = getString(R.string.topRatedMovieUrl);
                    break;
                case "favourites":
                    isFavourite = true;
                    break;
                default:
                    break;
            }

            if (!isFavourite) {
                /* Popular or Highest rated mode selected. Get from TMDB API */
                GetPopularMoviesTask getPopularMoviesTask = new GetPopularMoviesTask(getActivity().getApplicationContext());
                PopularMovies popularMovies = getPopularMoviesTask.execute(url).get();
                results = popularMovies.getPopularMovieResults();
            } else {
                /* Favourite mode selected. Get stored favourites */
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Set<String> favouritesSet = prefs.getStringSet(getString(R.string.favouritesList), null);

                if (favouritesSet == null) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.noFavourites, Toast.LENGTH_SHORT).show();
                    return Collections.EMPTY_LIST;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                List<PopularMovieResult> resultsList = new ArrayList<>();
                for (String favouriteMovie : favouritesSet) {
                    PopularMovieResult popularMovieResult = objectMapper.readValue(favouriteMovie,
                            PopularMovieResult.class);
                    resultsList.add(popularMovieResult);
                }
                results = resultsList.toArray(new PopularMovieResult[resultsList.size()]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> urls = new ArrayList<>();

        // There could be situations where we could not get results from the APIs and
        // hence results will be null. Do not crash.
        if (results == null) {
            return Collections.EMPTY_LIST;
        }

        for (PopularMovieResult result : results) {
            String posterPath = result.getPoster_path();
            if (posterPath != null)
                urls.add(String.format(getString(R.string.posterImageUrl), posterPath));
        }

        return urls;
    }

    private String getSortPreference() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return prefs.getString(getString(R.string.sortingPreference),
                getString(R.string.DefaultPreference));
    }

    @Override
    public void onResume() {
        super.onResume();

        String updatedSortPreference = getSortPreference();
        if (!currentSortPreference.equalsIgnoreCase(updatedSortPreference)) {
            imageAdapter.setUrls(getPosters());
            currentSortPreference = updatedSortPreference;
            imageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ImageAdapter to be associated with the ImageView (which in turn is associated with a GridView)
     */
    private class ImageAdapter extends BaseAdapter {
        private List<String> urls = new ArrayList<>();
        private final Context mContext;

        public ImageAdapter(Context c, List<String> urls) {
            mContext = c;
            this.urls = urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }
            if (position < urls.size())
                Picasso.with(mContext).load(urls.get(position)).into(imageView);
            return imageView;
        }
    }
}