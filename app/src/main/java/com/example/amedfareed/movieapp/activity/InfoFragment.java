package com.example.amedfareed.movieapp.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.data.MoviesContract;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoFragment extends Fragment implements OnLikeListener {

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
    private PopularMovie movie;
    private String movieTitle, overView, releaseDate, movieUserRating, posterPath;
    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_card_view, container, false);
        ButterKnife.bind(this, view);
        favoriteButton.setOnLikeListener(this);
        initializeViews();
        return view;
    }

    public void initializeViews() {
        movieList = new ArrayList<>();
        movie = new PopularMovie();
        Intent intent = getActivity().getIntent();
        if(intent.getExtras() != null) {
            posterPath = intent.getExtras().getString("poster_path");
            movieId = intent.getExtras().getInt("id");
            movieTitle = intent.getExtras().getString("original_title");
            overView = intent.getExtras().getString("over_view");
            releaseDate = intent.getExtras().getString("release_date");
            movieUserRating = intent.getExtras().getString("user_rating");


            movieName.setText(movieTitle);
            movieReview.setText(overView);
            userRatingTV.setText(movieUserRating);
            movieReleaseDate.setText(releaseDate);
            Picasso.with(mContext)
                    .load(BASE_POSTER_URL + posterPath)
                    .into(detailPoster);
        }
          /*  posterPath = BASE_POSTER_URL + intent.getExtras().getString("m_poster_path");
            movieId = intent.getExtras().getInt("m_id");
            movieTitle = intent.getExtras().getString("m_original_title");
            overView = intent.getExtras().getString("m_over_view");
            releaseDate = intent.getExtras().getString("m_release_date");
            movieName.setText(movieTitle);
            movieReview.setText(overView);
            userRatingTV.setText(movieUserRating);
            movieReleaseDate.setText(releaseDate);
            Picasso.with(mContext)
                    .load(posterPath)
                    .into(detailPoster);*/



        if (!movieExists(movieTitle)) {
            favoriteButton.setLiked(false);
        } else{
            favoriteButton.setLiked(true);
        }
    }

    @Override
    public void liked(LikeButton likeButton) {
        addFavoriteMovie();
        Snackbar.make(likeButton, "Added to favorites!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        deleteFavoriteMovie(movieId);
        Snackbar.make(likeButton, "Removed from favorites", Snackbar.LENGTH_LONG).show();
    }

    public boolean movieExists(String movieName) {
        String selection = MoviesContract.MoviesDateBase.MOVIE_TITLE + "=?";
        String[] selectionArgs = new String[]{movieName};
        Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MoviesDateBase.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public void addFavoriteMovie() {
        ContentValues values = new ContentValues();
            values.put(MoviesContract.MoviesDateBase.MOVIE_ID, movieId);
            values.put(MoviesContract.MoviesDateBase.MOVIE_TITLE, movieTitle);
            values.put(MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH, posterPath);
            values.put(MoviesContract.MoviesDateBase.VOTE_AVERAGE, movieUserRating);
            values.put(MoviesContract.MoviesDateBase.MOVIE_OVER_VIEW, overView);
            values.put(MoviesContract.MoviesDateBase.MOVIE_RELEASE_DATE, releaseDate);
        getActivity().getContentResolver().insert(MoviesContract.MoviesDateBase.CONTENT_URI, values);
    }

    public void deleteFavoriteMovie(int id) {
        String selection = MoviesContract.MoviesDateBase.MOVIE_ID + "=" + id;
        getActivity().getContentResolver().delete(MoviesContract.MoviesDateBase.CONTENT_URI,
                selection,
                null);
    }
}