package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.networkutils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Omoarukhe on 14/04/2017.
 */

public interface FetchMovieTaskListener<T> {

        void onTaskComplete(T[] result);

class FetchMovieTask extends AsyncTask<String, Void, MovieData[]> {

    MovieData[] movieData;
    private static final String TAG = "FetchMovieTask";

    private Context context;
    private FetchMovieTaskListener<MovieData> listener;

    public FetchMovieTask(Context ctx, FetchMovieTaskListener<MovieData> listener)
    {
        this.context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        super.onPostExecute(moviePosters);
        listener.onTaskComplete(moviePosters);
    }
}

}
