package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieData;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Omoarukhe on 11/04/2017.
 */

public class GridLayoutAdapter extends RecyclerView.Adapter<GridLayoutAdapter.movieHolder> {

    private Context context;
    public MovieData[] movieDatas;
    private GridLayoutAdapterOnClickHandler mClickHandler;

    public GridLayoutAdapter(GridLayoutAdapterOnClickHandler clickHandler, Context c) {
        mClickHandler = clickHandler;
        context = c;
    }

    public interface GridLayoutAdapterOnClickHandler {
        void onClick(MovieData movie);
    }

    @Override
    public movieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image_layout, parent, false);

        return new movieHolder(view);
    }

    @Override
    public void onBindViewHolder(movieHolder holder, int position) {
        String posterPath = "http://image.tmdb.org/t/p/w185/" + movieDatas[position].getPosterPath();
        Picasso.with(context).load(posterPath).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (movieDatas == null) return 0;
            return movieDatas.length;
    }

    public class movieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_image)
        ImageView imageView;

        public movieHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieData movie = movieDatas[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    public void setMovieData(MovieData[] movieData) {
        movieDatas = movieData;
        notifyDataSetChanged();
    }
}
