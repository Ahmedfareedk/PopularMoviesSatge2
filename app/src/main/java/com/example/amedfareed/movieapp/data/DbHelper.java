package com.example.amedfareed.movieapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;
import android.util.Log;

import com.example.amedfareed.movieapp.model.MovieResponses;
import com.example.amedfareed.movieapp.model.PopularMovie;

import java.util.ArrayList;
import java.util.List;



public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;
    private PopularMovie movie;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String FAVORITE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesDateBase.TABLE_NAME
                + " (" + MoviesContract.MoviesDateBase._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesContract.MoviesDateBase.MOVIE_ID + " INTEGER, "
                + MoviesContract.MoviesDateBase.MOVIE_TITLE + " TEXT NOT NULL, "
                + MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH + " TEXT NOT NULL, "
                + MoviesContract.MoviesDateBase.MOVIE_OVER_VIEW + " TEXT NOT NULL, "
                + MoviesContract.MoviesDateBase.MOVIE_RELEASE_DATE + " TEXT NOT NULL, "
                + MoviesContract.MoviesDateBase.VOTE_AVERAGE + " REAL NOT NULL"
                + "); ";
        db.execSQL(FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesDateBase.TABLE_NAME);
        onCreate(db);
    }
}