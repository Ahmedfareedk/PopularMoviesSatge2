package com.example.amedfareed.movieapp.MovieAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.activity.DetailsActivity;
import com.example.amedfareed.movieapp.activity.InfoFragment;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by amedfareed on 16/07/18.
 */

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.MyViewHolder>{
    private List<PopularMovie> favoriteList;
    private Context mContext;

    public FavoriteMoviesAdapter( Context mContext, List<PopularMovie> favoriteList) {
        this.mContext = mContext;
        this.favoriteList = favoriteList;
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
        String userRating = Double.toString(favoriteList.get(position).getVoteAverage());
        String movieTitle = favoriteList.get(position).getOriginalTitle();
        String posterPath = favoriteList.get(position).getPosterPath();
        holder.favoriteMovieTitle.setText(movieTitle);
        holder.favoriteMovieRating.setText(userRating);
        Picasso.with(mContext)
                .load(posterPath)
                .into(holder.favoriteMoviePoster);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
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
                    int position = getAdapterPosition();
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra("original_title", favoriteList.get(position).getOriginalTitle());
                    intent.putExtra("poster_path", favoriteList.get(position).getPosterPath());
                    intent.putExtra("over_view", favoriteList.get(position).getOverview());
                    intent.putExtra("user_rating", Double.toString(favoriteList.get(position).getVoteAverage()));
                    intent.putExtra("release_date", favoriteList.get(position).getReleaseDate());
                    intent.putExtra("id", favoriteList.get(position).getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
