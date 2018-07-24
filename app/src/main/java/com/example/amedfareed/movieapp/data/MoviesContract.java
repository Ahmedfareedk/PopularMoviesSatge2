package com.example.amedfareed.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by amedfareed on 05/07/18.
 */

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "movieapp.data.MovieContentProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse( "content://" + CONTENT_AUTHORITY);
    public static final String MOVIES_PATH = "movie";

    public static final class MoviesDateBase implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, MOVIES_PATH);


        public static final String _ID = "id";
        public static final String TABLE_NAME = "favoriteMovies";
        public static final String MOVIE_ID = "movieId";
        public static final String MOVIE_TITLE = "movieTitle";
        public static final String MOVIE_OVER_VIEW = "overView";
        public static final String MOVIE_RELEASE_DATE = "releaseDate";
        public static final String MOVIE_POSTER_PATH = "posterPath";
        public static final String VOTE_AVERAGE = "voteAverage";
    }
}