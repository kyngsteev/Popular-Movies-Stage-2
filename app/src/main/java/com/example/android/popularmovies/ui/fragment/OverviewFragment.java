package com.example.android.popularmovies.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.ui.PopularMoviesActivity;

import static com.example.android.popularmovies.ui.PopularMoviesActivity.MOVIE;

/**
 * Created by Omoarukhe on 07/05/2017.
 */

public class OverviewFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.overview_tab_layout, container, false);
        MovieData selectedMovie = getActivity().getIntent().getParcelableExtra(MOVIE);
        String overview_text = selectedMovie.getOverView();

        TextView mOverview = (TextView) view.findViewById(R.id.overviewTextView);
        mOverview.setText(overview_text);

        return view;
    }


}
