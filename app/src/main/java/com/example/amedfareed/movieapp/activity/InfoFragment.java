package com.example.amedfareed.movieapp.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.data.DbHelper;
import com.example.amedfareed.movieapp.data.MoviesContract;
import com.example.amedfareed.movieapp.model.MovieResponses;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amedfareed on 20/04/18.
 */

public class InfoFragment extends Fragment implements OnLikeListener {
    /*@BindView(R.id.favorite_button)
    MaterialFavoriteButton materialFavoriteButton;*/
    @BindView(R.id.info_movie_title)
    public TextView movieName;
    @BindView(R.id.info_review)
    TextView movieReview;
    @BindView(R.id.info_user_rating_tv)
    TextView userRatingTV;
    @BindView(R.id.info_release_date_tv)
    TextView movieReleaseDate;
    @BindView(R.id.info_detail_poster)
    ImageView detailPoster;
    @BindView(R.id.like_button_detail)
    LikeButton favoriteButton;
    List<PopularMovie> movieList;
    Context mContext;
    int movieId;
    SQLiteDatabase mdb;
    private PopularMovie favoriteMovie;
    private DbHelper favoriteDbHelper;
    private String movieTitle, overView, releaseDate, movieUserRating, posterPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_card_view, container, false);
        ButterKnife.bind(this, view);
        favoriteButton.setOnLikeListener(this);
        DbHelper dbHelper = new DbHelper(getContext());
        mdb = dbHelper.getWritableDatabase();
        initializeViews();
        return view;
    }

    public void initializeViews() {
        movieList = new ArrayList<>();
        favoriteDbHelper = new DbHelper(getContext());
        favoriteMovie = new PopularMovie();
        mdb = favoriteDbHelper.getWritableDatabase();


        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null ) {
          //  favoriteMovie = intent.getParcelableExtra("movies");

            movieId = extras.getInt("id");
            movieTitle = extras.getString("original_title");
            overView = extras.getString("over_view");
           mai releaseDate = extras.getString("release_date");
            movieUserRating = extras.getString("user_rating");
            posterPath = extras.getString("poster_path");
            movieName.setText(movieTitle);
            movieReview.setText(overView);
            userRatingTV.setText(movieUserRating);
            movieReleaseDate.setText(releaseDate);
            Picasso.with(mContext)
                    .load(posterPath)
                    .into(detailPoster);

            if(movieExists(movieTitle))
                favoriteButton.setLiked(true);
        }
    }

    @Override
    public void liked(LikeButton likeButton) {
        saveFavoriteMovie();
        Snackbar.make(likeButton, "Added to favorites!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        favoriteDbHelper.deleteFavorite(movieId);
        Snackbar.make(likeButton, "Removed from favorites", Snackbar.LENGTH_LONG).show();
    }

    public boolean movieExists(String movieName) {
        String[] projection = {
                MoviesContract.MoviesDateBase._ID,
                MoviesContract.MoviesDateBase.MOVIE_ID,
                MoviesContract.MoviesDateBase.MOVIE_TITLE,
                MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH,
                MoviesContract.MoviesDateBase.VOTE_AVERAGE
        };
        String selection = MoviesContract.MoviesDateBase.MOVIE_TITLE + " =?";
        String[] selectionArgs = {movieName};
        String limit = "1";

        Cursor cursor = mdb.query(MoviesContract.MoviesDateBase.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void saveFavoriteMovie() {
        favoriteMovie = new PopularMovie();
        favoriteDbHelper = new DbHelper(getContext());

        favoriteMovie.setOriginalTitle(movieTitle);
        favoriteMovie.setId(movieId);
        favoriteMovie.setPosterPath(posterPath);
        favoriteMovie.setVoteAverage(Double.parseDouble(movieUserRating));
        favoriteDbHelper.addFavoriteMovie(favoriteMovie);
    }
}