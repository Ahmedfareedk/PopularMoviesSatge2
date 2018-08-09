package com.example.amedfareed.movieapp.MovieAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.activity.DetailsActivity;
import com.example.amedfareed.movieapp.activity.FavoritesActivity;
import com.example.amedfareed.movieapp.activity.MainActivity;
import com.example.amedfareed.movieapp.data.MoviesContract;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.like.LikeButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private List<PopularMovie> moviesList;
    private Context context;
    //  private OnMovieClick callback;
    PopularMovie movie;
    private Cursor cursor;
    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";


    public MovieAdapter(Context mContext, List<PopularMovie> moviesList) {
        this.context = mContext;
        this.moviesList = moviesList;
    }

    /*public MovieAdapter(Context mContext, OnMovieClick mCallback, Cursor mCursor) {
        this.context = mContext;
        this.callback = mCallback;
        this.cursor = mCursor;
    }*/


    @Override
    @NonNull
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.MyViewHolder holder, int position) {
        if (moviesList != null) {
            holder.movieTitle.setText(moviesList.get(position).getOriginalTitle());
            holder.userRating.setText(String.valueOf(moviesList.get(position).getVoteAverage()));
            Picasso.with(context)
                    .load(BASE_POSTER_URL + moviesList.get(position).getPosterPath())
                    .into(holder.moviePoster);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle, userRating;
        ImageView moviePoster;

        public MyViewHolder(View itemView) {
            super(itemView);
            movie = new PopularMovie();
            movieTitle = itemView.findViewById(R.id.movie_card_title);
            userRating = itemView.findViewById(R.id.card_user_rating);
            moviePoster = itemView.findViewById(R.id.movie_card_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("id", moviesList.get(position).getId());
                        intent.putExtra("original_title", moviesList.get(position).getOriginalTitle());
                        intent.putExtra("poster_path", moviesList.get(position).getPosterPath());
                        intent.putExtra("over_view", moviesList.get(position).getOverview());
                        intent.putExtra("user_rating", Double.toString(moviesList.get(position).getVoteAverage()));
                        intent.putExtra("release_date", moviesList.get(position).getReleaseDate());
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, moviePoster, "thumbnail");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent, options.toBundle());
                    }
                }
            });
        }
    }
}