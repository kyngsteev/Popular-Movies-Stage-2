package com.example.android.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        if(NetworkUtils.isNetworkAvailable(this)) {
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
        new FetchMovieTaskListener.FetchMovieTask(this, new FetchMovieTaskCompleteListener()).execute(apiAddressKey, sortParam);
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

    public class FetchMovieTaskCompleteListener implements FetchMovieTaskListener<MovieData> {

        @Override
        public void onTaskComplete(MovieData[] moviePosters) {

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
