package com.example.android.popularmovies.ui.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.data.Reviews;
import com.example.android.popularmovies.utilities.DbUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.android.popularmovies.ui.PopularMoviesActivity.MOVIE;

/**
 * Created by Omoarukhe on 07/05/2017.
 */

public class ReviewFragment extends Fragment{

    private static final String REVIEW_DATA_TAG = "fragmentReview";
    private Reviews[] mReviews;
    private RecyclerView recyclerView;
    private ReviewAdapter mReviewAdapter;
    private MovieData selectedMovie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.review_tab_layout, container, false);

        selectedMovie = getActivity().getIntent().getParcelableExtra(MOVIE);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(NetworkUtils.reviewUrl(selectedMovie.getId()))
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.d(REVIEW_DATA_TAG, jsonData);
                        if (response.isSuccessful()) {
                            mReviews = getReviewData(jsonData);
                            if(getActivity() == null)
                                return;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                                    mReviewAdapter = new ReviewAdapter(getActivity(), mReviews);
                                    recyclerView.setAdapter(mReviewAdapter);
                                }
                            });
                        }
                    } catch (IOException e) {
                        Log.e(REVIEW_DATA_TAG, "Exception caught", e);
                    } catch (JSONException e) {
                        Log.e(REVIEW_DATA_TAG, "JSONException caught", e);
                    }
                }
            });

        return recyclerView;
    }

    public static Reviews[] getReviewData(String jsonData) throws JSONException {
        JSONObject reviews = new JSONObject(jsonData);
        JSONArray reviewResults = reviews.getJSONArray("results");
        Reviews[] reviewTrailers = new Reviews[reviewResults.length()];

        for (int i = 0; i < reviewResults.length(); i++) {
            JSONObject movieReview = reviewResults.getJSONObject(i);
            Reviews data = new Reviews();
            data.setAuthorName(movieReview.getString("author"));
            data.setContent(movieReview.getString("content"));

            reviewTrailers[i] = data;
        }

        return reviewTrailers;
    }
}
