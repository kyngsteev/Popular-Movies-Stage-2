package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView title;
    private TextView overview;
    private TextView releaseDate;
    private TextView userRating;
    private ImageView poster;
    private double v_average = 0.0;
    private String posterString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        title = (TextView) findViewById(R.id.original_title);
        overview = (TextView) findViewById(R.id.over_view);
        releaseDate = (TextView) findViewById(R.id.release_date);
        userRating = (TextView) findViewById(R.id.user_rating);
        poster = (ImageView) findViewById(R.id.poster_image);

        Intent intent = getIntent();
        if(intent.hasExtra("Title") || intent.hasExtra("Plot") || intent.hasExtra("Rating") || intent.hasExtra("Date") || intent.hasExtra("Poster")) {
            title.setText(intent.getStringExtra("Title"));
            overview.setText(intent.getStringExtra("Plot"));
            userRating.setText(intent.getDoubleExtra("Rating", v_average) + "/10");
            posterString = intent.getStringExtra("Poster");

            Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + posterString).into(poster);

            String[] r_date = intent.getStringExtra("Date").split("-");

            releaseDate.setText(r_date[0]);

        }

    }
}
