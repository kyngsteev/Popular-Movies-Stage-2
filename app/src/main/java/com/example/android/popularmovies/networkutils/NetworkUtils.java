package com.example.android.popularmovies.networkutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Omoarukhe on 11/04/2017.
 */

public class NetworkUtils {


    private final static String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";
    private final static String PARAM_QUERY = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String MOVIE = "movie";
    private final static String TAG = NetworkUtils.class.getSimpleName();


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    public static URL buildUrl(String sortParam){

        sortParam = sortParam == null ? "" : sortParam;

        Uri uri;
        switch (sortParam) {
            case "popular":
                uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(MOVIE)
                        .appendPath(sortParam)
                        .appendQueryParameter(PARAM_QUERY, API_KEY)
                        .build();
                break;
            case "top_rated":
               uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(MOVIE)
                        .appendPath(sortParam)
                        .appendQueryParameter(PARAM_QUERY, API_KEY)
                        .build();
                break;
            default:
                uri = latestMovies();
                break;
        }

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    private static Uri latestMovies () {
        return Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(MOVIE)
                .appendPath("popular")
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
