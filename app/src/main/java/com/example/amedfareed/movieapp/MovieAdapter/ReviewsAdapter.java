package com.example.amedfareed.movieapp.MovieAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.model.MovieReviews;

import java.util.List;

/**
 * Created by amedfareed on 10/06/18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    Context mContext;
    List<MovieReviews> reviewsList;
    public ReviewsAdapter(Context mContext, List<MovieReviews> reviewsList) {
        this.mContext = mContext;
        this.reviewsList = reviewsList;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_card_view, parent, false);
        return new ReviewViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.authorName.setText(reviewsList.get(position).getAuthorName());
        holder.reviewContent.setText(reviewsList.get(position).getReviewContent());
    }
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView authorName, reviewContent;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author_name);
            reviewContent = itemView.findViewById(R.id.review_content);
        }
    }
}
