package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Omoarukhe on 09/05/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MoviesEntry.MOVIE_TABLE + " (" +
            MovieContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            MovieContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
            MovieContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MovieContract.MoviesEntry.COLUMN_ID + " INTEGER NOT NULL" +
            ");";
    private static final String CREATE_TRAILERS_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TRAILER_TABLE + " (" +
            MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            MovieContract.TrailerEntry.COLUMN_VIDEO_URL + " TEXT NOT NULL, " +
            MovieContract.MoviesEntry.COLUMN_ID + " INTEGER NOT NULL" +
            ");";
    private static final String CREATE_REVIEWS_TABLE = "CREATE TABLE " + MovieContract.ReviewsEntry.REVIEWS_TABLE + " (" +
            MovieContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
            MovieContract.ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
            MovieContract.MoviesEntry.COLUMN_ID + " INTEGER NOT NULL" +
            ");";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_TRAILERS_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.MOVIE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TRAILER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewsEntry.REVIEWS_TABLE);
        onCreate(db);
    }
}
