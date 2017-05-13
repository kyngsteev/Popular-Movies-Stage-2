package com.example.android.popularmovies.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.PagerAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.utilities.DbUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.ui.PopularMoviesActivity.MOVIE;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.original_title)
    TextView title;

    @BindView(R.id.release_date)
    TextView releaseDate;

    @BindView(R.id.user_rating)
    TextView userRating;

    @BindView(R.id.poster_image)
    ImageView poster;

    @BindView(R.id.star_image)
    ImageView starImage;

    @BindDrawable(R.drawable.heart_outline)
    Drawable emptyStar;

    @BindDrawable(R.drawable.heart_outline_filled)
    Drawable fillStar;

    private String posterString;
    private MovieData selectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        selectedMovie = getIntent().getParcelableExtra(MOVIE);

        title.setText(selectedMovie.getOriginalTitle());
        userRating.setText(selectedMovie.getVoteAverage() + "/10");
        posterString = selectedMovie.getPosterPath();

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + posterString).into(poster);

        String[] r_date = selectedMovie.getReleaseDate().split("-");

        releaseDate.setText(r_date[0]);

        if(isFavorite()){
            starImage.setImageDrawable(fillStar);
        }else{
            starImage.setImageDrawable(emptyStar);
        }
    }

    public void animate(View view) {
        if(isFavorite()){
            starImage.setImageDrawable(emptyStar);
            removeMovieFromFavorites();
        }else{
            starImage.setImageDrawable(fillStar);
            addMovieToFavorites();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent upIntent = getSupportParentActivityIntent();
                if (NavUtils.shouldUpRecreateTask(this, upIntent) && upIntent != null){
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }else{
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeMovieFromFavorites() {
        String selection = MovieContract.MoviesEntry.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(selectedMovie.getId())};
        getContentResolver().delete(MovieContract.buildMovieUriWithId(selectedMovie.getId()),
               selection, selectionArgs);
    }

    synchronized private void addMovieToFavorites() {
        getContentResolver().insert(MovieContract.buildMovieUriWithId(selectedMovie.getId()),
                DbUtils.getMovieDetails(selectedMovie));
    }

    private boolean isFavorite() {
        String[] projection = {MovieContract.MoviesEntry.COLUMN_ID};
        String selection = MovieContract.MoviesEntry.COLUMN_ID + " = " + selectedMovie.getId();

        Cursor cursor = getContentResolver().query(MovieContract.buildMovieUriWithId(selectedMovie.getId()),
                projection,
                selection,
                null,
                null,
                null);

        cursor.close();

        return (cursor != null ? cursor.getCount() : 0) > 0;
    }

}
