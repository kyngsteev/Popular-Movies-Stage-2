package com.example.android.popularmovies.ui;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.original_title)
    TextView title;

    @BindView(R.id.over_view)
    TextView overview;

    @BindView(R.id.release_date)
    TextView releaseDate;

    @BindView(R.id.user_rating)
    TextView userRating;

    @BindView(R.id.poster_image)
    ImageView poster;

    private double v_average = 0.0;
    private String posterString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}
