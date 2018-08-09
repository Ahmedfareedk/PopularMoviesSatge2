package com.example.amedfareed.movieapp.MovieAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.activity.DetailsActivity;
import com.example.amedfareed.movieapp.activity.FavoritesActivity;
import com.example.amedfareed.movieapp.activity.InfoFragment;
import com.example.amedfareed.movieapp.data.MoviesContract;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by amedfareed on 16/07/18.
 */

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.MyViewHolder> {
    private Context mContext;
    Cursor cursor;
    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    public FavoriteMoviesAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.cursor = mCursor;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorites_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String userRating = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.VOTE_AVERAGE));
        String movieTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_TITLE));
        String posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH
        ));
        holder.favoriteMovieTitle.setText(movieTitle);
        holder.favoriteMovieRating.setText(userRating);
        Picasso.with(mContext)
                .load(BASE_POSTER_URL + posterPath)
                .into(holder.favoriteMoviePoster);
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView favoriteMoviePoster;
        TextView favoriteMovieTitle, favoriteMovieRating;

        public MyViewHolder(View itemView) {
            super(itemView);
            favoriteMoviePoster = itemView.findViewById(R.id.favorite_movie_card_poster);
            favoriteMovieRating = itemView.findViewById(R.id.favorite_user_rating);
            favoriteMovieTitle = itemView.findViewById(R.id.favorite_movie_card_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra("id", cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_ID)));
                    intent.putExtra("original_title", cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_TITLE)));
                    intent.putExtra("poster_path", cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_POSTER_PATH)));
                    intent.putExtra("user_rating", cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.VOTE_AVERAGE)));
                    intent.putExtra("over_view", cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_OVER_VIEW)));
                    intent.putExtra("release_date", cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesDateBase.MOVIE_RELEASE_DATE)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
