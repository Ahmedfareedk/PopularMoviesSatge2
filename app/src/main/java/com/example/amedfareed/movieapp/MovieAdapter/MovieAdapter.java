package com.example.amedfareed.movieapp.MovieAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.media.Image;
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
import com.example.amedfareed.movieapp.activity.MainActivity;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amedfareed on 05/03/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private List<PopularMovie> moviesList;
    private Context context;

    public MovieAdapter(Context mContext, List<PopularMovie> moviesList) {
        this.context = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MyViewHolder holder, int position) {
        String userVote = Double.toString(moviesList.get(position).getVoteAverage());
        String title = moviesList.get(position).getTitle();
        holder.movieTitle.setText(title);
        holder.userRating.setText(userVote);
        Picasso.with(context)
                .load(moviesList.get(position).getPosterPath())
                .into(holder.moviePoster);
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
            movieTitle = itemView.findViewById(R.id.movie_card_title);
            userRating = itemView.findViewById(R.id.card_user_rating);
            moviePoster = itemView.findViewById(R.id.movie_card_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("original_title", moviesList.get(position).getOriginalTitle());
                        intent.putExtra("poster_path", moviesList.get(position).getPosterPath());
                        intent.putExtra("over_view", moviesList.get(position).getOverview());
                        intent.putExtra("user_rating",Double.toString(moviesList.get(position).getVoteAverage()));
                        intent.putExtra("release_date", moviesList.get(position).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}

