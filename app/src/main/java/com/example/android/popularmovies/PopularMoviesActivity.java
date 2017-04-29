package com.example.android.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.GridLayoutAdapter;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.utilities.AlertDialogFragment;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class PopularMoviesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MovieData[]>,
        GridLayoutAdapter.GridLayoutAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private GridLayoutAdapter gridLayoutAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    public static final String SORT_DATA_KEY = "key";
    private static final int MOVIE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        recyclerView = (RecyclerView) findViewById(R.id.rv_image);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        recyclerView.setHasFixedSize(true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        gridLayoutAdapter = new GridLayoutAdapter(this, getApplicationContext());
        recyclerView.setAdapter(gridLayoutAdapter);

        Bundle sortBundle = new Bundle();
        sortBundle.putString(SORT_DATA_KEY, getResources().getString(R.string.popular));

        LoaderManager.LoaderCallbacks<MovieData[]> callback = PopularMoviesActivity.this;

        if(NetworkUtils.isNetworkAvailable(this)) {

            mLoadingIndicator.setVisibility(View.VISIBLE);

            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, sortBundle, callback);

        }else {
            alertUserOfError();
        }

    }



    @Override
    public Loader<MovieData[]> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<MovieData[]>(this) {

            MovieData[] cachedMovieData;

            @Override
            protected void onStartLoading() {
                if (cachedMovieData == null){
                    forceLoad();
                }else{
                    super.deliverResult(cachedMovieData);
                }
            }

            @Override
            public MovieData[] loadInBackground() {

                URL movieRequestUrl = NetworkUtils.buildUrl(args.getString(SORT_DATA_KEY));

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);


                    JSONObject movieResponse = new JSONObject(jsonMovieResponse);
                    JSONArray movieResults = movieResponse.getJSONArray("results");
                    MovieData[] movieData = new MovieData[movieResults.length()];

                    for (int i = 0; i < movieResults.length(); i++) {
                        JSONObject movieResult = movieResults.getJSONObject(i);
                        MovieData data = new MovieData();
                        data.setPosterPath(movieResult.getString("poster_path"));
                        data.setId(movieResult.getInt("id"));
                        data.setOriginalTitle(movieResult.getString("title"));
                        data.setOverView(movieResult.getString("overview"));
                        data.setReleaseDate(movieResult.getString("release_date"));
                        data.setVoteAverage(movieResult.getDouble("vote_average"));

                        movieData[i] = data;
                    }

                    return movieData;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void deliverResult(MovieData[] data) {
                cachedMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieData[]> loader, MovieData[] data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        gridLayoutAdapter.setMovieData(data);
        if (data == null) {
            showErrorMessage();
        }else{
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieData[]> loader) {
        gridLayoutAdapter.setMovieData(null);
    }


    private void showMovieDataView() {
        recyclerView.setVisibility(View.VISIBLE);

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {

        mErrorMessageDisplay.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void alertUserOfError(){
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(), "error_dialog");
    }

    @Override
    public void onClick(MovieData movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);

        intent.putExtra("Title", movie.getOriginalTitle());
        intent.putExtra("Poster", movie.getPosterPath());
        intent.putExtra("Plot", movie.getOverView());
        intent.putExtra("Rating", movie.getVoteAverage());
        intent.putExtra("Date", movie.getReleaseDate());

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Bundle sortBundle = new Bundle();

        if(NetworkUtils.isNetworkAvailable(this) && item.getItemId() == R.id.popular_item) {

            sortBundle.putString(SORT_DATA_KEY, getResources().getString(R.string.popular));
            mLoadingIndicator.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, sortBundle, this);

        }else if(NetworkUtils.isNetworkAvailable(this) && item.getItemId() == R.id.top_rated_item) {

            sortBundle.putString(SORT_DATA_KEY, getResources().getString(R.string.top_rated));
            mLoadingIndicator.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, sortBundle, this);

        }else {
            alertUserOfError();
        }

        return super.onOptionsItemSelected(item);
    }
}
