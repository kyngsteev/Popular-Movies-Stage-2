package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapter.GridLayoutAdapter;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.networkutils.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;

public class PopularMoviesActivity extends AppCompatActivity implements GridLayoutAdapter.GridLayoutAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private GridLayoutAdapter gridLayoutAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private GridLayoutManager gridLayoutManager;
    MovieData[] movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        if(NetworkUtils.isNetworkAvailable(this)) {
            recyclerView = (RecyclerView) findViewById(R.id.rv_image);
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

            gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);

            gridLayoutAdapter = new GridLayoutAdapter(this, getApplicationContext());
            recyclerView.setAdapter(gridLayoutAdapter);

            loadMovieData("popular");
        }else {
            alertUserOfError();
        }

    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie poster is visible */
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error when data is unavailable */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadMovieData(String sortMovie) {
        showMovieDataView();
        String apiAddressKey = ""; /* Place your API KEY here*/
        String sortParam = sortMovie;
        new FetchMovieTask().execute(apiAddressKey, sortParam);
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

    public class FetchMovieTask extends AsyncTask<String, Void, MovieData[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieData[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String apiUrl = params[0];
            String sortUrl = params[1];
            URL movieRequestUrl = NetworkUtils.buildUrl(apiUrl, sortUrl);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);


                JSONObject movieResponse = new JSONObject(jsonMovieResponse);
                JSONArray movieResults = movieResponse.getJSONArray("results");
                movieData = new MovieData[movieResults.length()];

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
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieData[] moviePosters) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviePosters != null) {
                showMovieDataView();
                gridLayoutAdapter.setMovieData(moviePosters);
            }else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(NetworkUtils.isNetworkAvailable(this) && item.getItemId() == R.id.popular_item) {
            loadMovieData("popular");
        }else if(NetworkUtils.isNetworkAvailable(this) && item.getItemId() == R.id.top_rated_item) {
            loadMovieData("top_rated");
        }else {
            alertUserOfError();
        }

        return super.onOptionsItemSelected(item);
    }
}
