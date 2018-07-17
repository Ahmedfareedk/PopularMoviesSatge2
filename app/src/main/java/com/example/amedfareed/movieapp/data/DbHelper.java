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

/**
 * Created by amedfareed on 06/07/18.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favourites.db";
    public static final int DATABASE_VERSION = 1;
    private PopularMovie movie;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String FAVORITE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesDateBase.TABLE_NAME
                + " (" + MoviesContract.MoviesDateBase._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesContract.MoviesDateBase.MOVIE_ID + " INTEGER, "
                + MoviesContract.MoviesDateBase.MOVIE_TITLE + " TEXT, "
                + MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH + " TEXT, "
                + MoviesContract.MoviesDateBase.VOTE_AVERAGE + " REAL"
                + "); ";
        db.execSQL(FAVORITE_MOVIES_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesDateBase.TABLE_NAME);
        onCreate(db);
    }
    public void addFavoriteMovie(PopularMovie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesDateBase.MOVIE_ID, movie.getId());
        values.put(MoviesContract.MoviesDateBase.MOVIE_TITLE, movie.getOriginalTitle());
        values.put(MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MoviesContract.MoviesDateBase.VOTE_AVERAGE, movie.getVoteAverage());

        db.insert(MoviesContract.MoviesDateBase.TABLE_NAME, null, values);
        db.close();
    }
    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MoviesContract.MoviesDateBase.TABLE_NAME, MoviesContract.MoviesDateBase.MOVIE_ID + "=" + id, null);
    }
    public List<PopularMovie> getFavoriteMovies() {
        movie = new PopularMovie();
        String[] favoriteMovies = {
                MoviesContract.MoviesDateBase._ID,
                MoviesContract.MoviesDateBase.MOVIE_ID,
                MoviesContract.MoviesDateBase.MOVIE_TITLE,
                MoviesContract.MoviesDateBase.VOTE_AVERAGE,
                MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH
        };
        String sortOrder = MoviesContract.MoviesDateBase._ID + " ASC";
        List<PopularMovie> favoriteMoviesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MoviesContract.MoviesDateBase.TABLE_NAME,
                favoriteMovies,
                null,
                null,
                null,
                null,
                sortOrder);
        if (cursor.moveToFirst()) {
            do {
                movie = new PopularMovie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_ID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.VOTE_AVERAGE))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH)));

                favoriteMoviesList.add(movie);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoriteMoviesList;
    }
}