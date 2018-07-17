package com.example.amedfareed.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by amedfareed on 05/07/18.
 */

public class MoviesContract {

    public static final class MoviesDateBase implements BaseColumns {
        public static final String TABLE_NAME = "favoriteMovies";
        public static final String MOVIE_TITLE = "movieTitle";
        public static final String MOVIE_ID = "movieId";
        public static final String MOVIE_POSTER_PATH = "posterPath";
        public static final String VOTE_AVERAGE = "voteAverage";
    }
}